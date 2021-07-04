package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Ewok;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    private Ewok currEwok;
    @BeforeEach
    void setUp() {
        currEwok=new Ewok(1);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * check that {@link Ewok} Ewok is not available for a {@link MicroService}
     * microservice's attack.
     */
    @Test
    void acquire() {
        assertTrue(currEwok.isAvailable());
        currEwok.acquire();
        assertFalse(currEwok.isAvailable());
    }
    /**
     * check that {@link Ewok} Ewok is available again for a {@link MicroService}
     * microservice's attack.
     */
    @Test
    void release() {
        currEwok.acquire();
        currEwok.release();
        assertTrue(currEwok.isAvailable());
    }
}