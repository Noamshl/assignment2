package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;
import  bgu.spl.mics.application.services.*;

/**
 * AttackEvent is  class that implements {@link Event}/This class saves specific attack information,
 * that will be used by {@link HanSoloMicroservice} or {@link C3POMicroservice}.
 */
public class AttackEvent implements Event<Boolean> {
    private Attack attack;
    public AttackEvent(){}

    public AttackEvent(Attack attack){
        this.attack = attack;
    }

    public Attack getAttack() {
        return attack;
    }
}