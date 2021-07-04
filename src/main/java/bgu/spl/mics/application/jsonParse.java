package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;

public class jsonParse {
   private int Ewoks;
   private Attack[] attacks;
   private int R2D2;
   private int Lando;
   public jsonParse(int numberOfEwoks,Attack[]attacks,int durationLando,int durationR2D2){
       this.Ewoks=numberOfEwoks;
       this.attacks=attacks;
       this.Lando=durationLando;
       this.R2D2=durationR2D2;

   }

    public int getEwoks() {
        return Ewoks;
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public int getLando() {
        return Lando;
    }

    public int getR2D2() {
        return R2D2;
    }
}
