package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks;
    private  long HanSoloFinish;
    private  long C3POFinish;
    private  long R2D2Deactivate;
    private  long LieaTerminate;
    private  long HanSoloTerminate;
    private  long C3POTerminate;
    private  long R2D2Terminate;
    private  long LandoTerminate;
    private static Diary diaryInstance= null;
   private Diary(){
       totalAttacks = new AtomicInteger(0);
   }

   public static Diary getInstance(){
       if (diaryInstance==null){
           diaryInstance=new Diary();
       }
       return diaryInstance;
    }
    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public  void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public  void setLieaTerminate(long lieaTerminate) {
        LieaTerminate = lieaTerminate;
    }

    public  void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public  void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public  void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }


    public  void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }

    public  void setR2D2Terminate(long r2D2Terminate) { R2D2Terminate = r2D2Terminate;
    }

    public  void setTotalAttacks() {
        totalAttacks.incrementAndGet() ;
    }

}
