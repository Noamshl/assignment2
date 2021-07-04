package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    List<Future> futureList;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futureList = new ArrayList<>();
    }



    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, (terminateBroadcast) -> {
            /**
             * When {@link LeiaMicroservice} get the terminateBroadcast she terminate and updates the diary.
             */
            Diary.getInstance().setLieaTerminate(System.currentTimeMillis());
            terminate();
        });
        /**
         * {@link LandoMicroservice} waits until the other Microservices subscribe to events.
         */
        try {
            Main.latch.await();
        } catch (Exception e) {
        }

        /**
         * {@link LeiaMicroservice} send attackEvent messages to {@link C3POMicroservice} and {@link HanSoloMicroservice}.
         */
        for (Attack attack : attacks) {
            AttackEvent attackEvent = new AttackEvent(attack);
            futureList.add(sendEvent(attackEvent));

        }
        /**
         * {@link LeiaMicroservice} wait until the futures of the attackEvent messages resolve,
         * and send {@link DeactivationEvent} to {@link R2D2Microservice}.
         * Leia wait until {@link R2D2Microservice} finish and the send {@link BombDestroyerEvent} to {@link LandoMicroservice}.
         */
        for (Future future : futureList) {
            future.get();
        }
        Future deActivateFuture = sendEvent(new DeactivationEvent());
        deActivateFuture.get();
        sendEvent(new BombDestroyerEvent());

    }

}
