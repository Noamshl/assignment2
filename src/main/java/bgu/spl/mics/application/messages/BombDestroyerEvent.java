package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import  bgu.spl.mics.application.services.LandoMicroservice;
/**
 * Marker class that implements {@link Event}.It used by {@link LandoMicroservice }  to destroy.
 */
public class BombDestroyerEvent implements Event<Boolean> {
}
