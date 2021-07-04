package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
   private long duration;
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }


    @Override
    protected void initialize() {

       subscribeBroadcast(TerminateBroadcast.class,(terminateBroadcast)->{
           /**
            * When {@link LandoMicroservice} get the terminateBroadcast he terminate and updates the diary.
            */
           Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
           terminate();
       });

       subscribeEvent(BombDestroyerEvent.class,(bombDestroyerEvent)->{

           /**
            * {@link LandoMicroservice} destroy and then send his friends the mission completed(terminateBroadcast)and
            * update the diary.
            */
           try {
               Thread.sleep(duration);
           }
           catch (InterruptedException ignore){}
           complete(bombDestroyerEvent,true);
           sendBroadcast(new TerminateBroadcast());
       });
       Main.latch.countDown();
    }
}
