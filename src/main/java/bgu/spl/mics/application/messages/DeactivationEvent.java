package bgu.spl.mics.application.messages;
import  bgu.spl.mics.Event;
import bgu.spl.mics.application.services.R2D2Microservice;
/**
 * Marker class implements {@link Event}. It used by {@link R2D2Microservice} to deactivate the generator.
 */
public class DeactivationEvent implements Event<Boolean> {
}
