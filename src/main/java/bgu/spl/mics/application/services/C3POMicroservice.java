package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Ewoks ewoks = Ewoks.getInstacne();


    public C3POMicroservice() {
        super("C3PO");
    }


    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (attackEvent) -> {
            /**
             * synchronized operation use to prevents {@link C3POMicroservice} and {@link HanSoloMicroservice} from acquire the same
             * ewok at the same time, and prevents deadlock.
             */
            synchronized (ewoks) {
                while (!ewoks.checkAvailable(attackEvent.getAttack().getSerials())) {
                    try {
                        ewoks.wait();
                    } catch (InterruptedException ignored) {
                    }
                }

                ewoks.acquireEwoks(attackEvent.getAttack().getSerials());
            }
            /**
             * Once {@link C3POMicroservice} acquire all the ewoks he need for the attack, he attacks.
             */
            try {
                Thread.sleep(attackEvent.getAttack().getDuration());
                ewoks.releaseEwoks(attackEvent.getAttack().getSerials());
            } catch (InterruptedException ignored) {
            }
            /**
             * Once {@link C3POMicroservice} finish its attack,he release the ewoks and updates the future and the diary.
             */
            complete(attackEvent, true);
            Diary.getInstance().setC3POFinish(System.currentTimeMillis());
            Diary.getInstance().setTotalAttacks();
        });

        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast) -> {
            /**
             * When {@link C3POMicroservice} get the terminateBroadcast he terminate and updates the diary.
             */
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            terminate();
        });
        Main.latch.countDown();
    }
}
