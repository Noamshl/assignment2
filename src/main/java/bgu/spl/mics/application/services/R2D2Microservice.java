package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
    }


    @Override

    protected void initialize() {
        subscribeEvent(DeactivationEvent.class,(deactivationEvent)->{
            /**
             * {@link R2D2Microservice} deactivate the generator and when {@link R2D2Microservice} finish,the diary is updated.
             */
            try{
                Thread.sleep(duration);}
            catch (InterruptedException ignore){}
            complete(deactivationEvent,true);
            Diary.getInstance().setR2D2Deactivate(System.currentTimeMillis());
        });

        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{
            /**
             * When {@link R2D2Microservice} get the terminateBroadcast she terminate and updates the diary.
             */
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
            terminate();
        });
        Main.latch.countDown();
    }
}
