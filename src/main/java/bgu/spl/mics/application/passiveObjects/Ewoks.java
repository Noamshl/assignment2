package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private Ewok[] ewoks;

    private static class SafeEwoks {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks() {
    }

    public static Ewoks getInstacne(){
        return SafeEwoks.instance;
    }

    public void setEwoks(int number) {
        ewoks = new Ewok[number+1];
        for (int i = 1; i < number+1; i++) {
            ewoks[i]=new Ewok(i);
        }
    }
    public synchronized boolean  checkAvailable(List<Integer> serials){
        for (Integer serial:serials) {
            if(!ewoks[serial].isAvailable())
                return false;
        }
        return true;
    }
    public synchronized void acquireEwoks(List<Integer> serials){
        for (Integer serial:serials) {
            ewoks[serial].acquire();
        }
    }
    public  synchronized void releaseEwoks(List<Integer> serials){
        for (Integer serial:serials) {
            ewoks[serial].release();
        }
        this.notifyAll();
    }
}
