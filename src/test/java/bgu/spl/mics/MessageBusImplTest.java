package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    static MessageBusImpl messageB;
    static MicroService m1;
    static MicroService m2;
    static Event<Boolean> mess;
    static TestBroadcast broadcast;

    /**
     * TestBroadcast is implement the Broadcast interface.We create this class for the tests.
     */
    static class TestBroadcast implements Broadcast {

    }

    /**
     * We create this Microservice class for the tests.
     */
    static class TestMicroService extends MicroService {
        public TestMicroService() {
            super("Darth");
        }

        @Override
        protected void initialize() {

        }
    }

    /**
     * Initialize the fields.
     */
    @BeforeAll
    static void setUp() {
        messageB = MessageBusImpl.getInstance();
        m1 = new TestMicroService();
        m2 = new HanSoloMicroservice();
        broadcast = new TestBroadcast();
        mess = new AttackEvent();

    }

    /**
     * Microservices registered before each test.
     */
    @BeforeEach
    void set(){
        messageB.register(m1);
        messageB.register(m2);


    }

    /**
     * Microservices unregistered after each test.
     */
    @AfterEach
    void tearDown(){
        messageB.unregister(m1);
        messageB.unregister(m2);
    }
    /**
     *check that subscribeBroadcast() and sendbroadcast() work.
     */
    @Test
    void sendBroadcast() {
        m2.subscribeBroadcast(broadcast.getClass(), (broadcast) -> {});
        m1.sendBroadcast(broadcast);
        try {
            assertTrue(broadcast.equals(messageB.awaitMessage(m2)));

        } catch (Exception ex) {
            System.out.println("interrupt");
        }

    }

    /**
     * check that subscribeEvent() and sendEvent() work.
     */
    @Test
    void sendEvent() {
        m2.subscribeEvent(mess.getClass(), (mess) -> {});
        m1.sendEvent(mess);
        try {
            assertTrue(mess.equals(messageB.awaitMessage(m2)));
        } catch (Exception ex) {
            System.out.println("interrupt");
        }
    }

    @Test
    void complete() {
        m2.subscribeEvent(mess.getClass(), (mess) -> {});
        Future<Boolean> future = m1.sendEvent(mess);
        try {
            messageB.awaitMessage(m2);
            assertFalse(future.isDone());
            m2.complete(mess, true);
            assertTrue(future.isDone());
            assertTrue(future.get());
        } catch (Exception e) {
            System.out.println("complete did not work");
        }


    }

    @Test
    void awaitMessage() {
        m2.subscribeBroadcast(broadcast.getClass(), (broadcast) -> {});
        m2.subscribeEvent(mess.getClass(), (mess) -> {});
        m1.sendBroadcast(broadcast);
        m1.sendEvent(mess);
        try {
            Message firstMessage = messageB.awaitMessage(m2);
            assertTrue(firstMessage.equals(broadcast));
        } catch (Exception e) {
            System.out.println("there is no broadcast");
        }
        try {
            Message secondMessage = messageB.awaitMessage(m2);
            assertTrue(secondMessage.equals(mess));
        } catch (Exception e) {
            System.out.println("there is no event");
        }
    }
}