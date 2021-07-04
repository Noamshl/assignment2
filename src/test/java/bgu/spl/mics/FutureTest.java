package bgu.spl.mics;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the methods of {@link Future} future.
 */
class FutureTest {
    /**
     * The{@link Future} future we test.
     */
    private Future<String> future;
    @BeforeEach
    void setUp() {
        future=new Future<>();
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * check if future receives result value  and can retrieve it.
     */
    @Test
    void get() {
        assertFalse(future.isDone());
        future.resolve("Done");
        assertTrue(future.isDone());
        assertTrue(future.get().equals("Done"));
    }

    @Test
    void resolve() {
        assertFalse(future.isDone());
        future.resolve("Done");
        assertTrue(future.isDone());
        assertEquals("Done",future.get());
    }

    @Test
    void isDone() {
        assertFalse(future.isDone());
        future.resolve("");
        assertTrue(future.isDone());
    }

    @Test
    void testGet() {
        try {
            assertFalse(future.isDone());
            future.get(100, TimeUnit.MILLISECONDS);
            assertFalse(future.isDone());
            future.resolve("Done");
            assertTrue ( future.get(100, TimeUnit.MILLISECONDS).equals("Done"));
        }
        catch (Exception ex){
            System.out.println("null");
        }
    }
}