package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier")
public class TimeWatchTest {

    //
    // Constants
    //

    // the amount of time passed
    private static Long time = 4L;

    //
    // Attributes
    //

    private static TimeWatch timer;


    /**
     * The component-under-test (CuT).
     * <p>
     * This is a stateless component so we only need one.
     * The {@link TimeWatch} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private TimeWatch CuT;

    @BeforeEach
    void setup() {

        CuT = new TimeWatch();
    }

    @Test
    void test_creating_timer() throws InterruptedException {
        assertEquals(0, TimeWatch.start().time());
        try {
            wait(5000);
            assertEquals(5, CuT.time());
            assertEquals(0, CuT.reset().time());
        } catch (Exception ex) {
            //squash
        }

        assertEquals(0, CuT.reset().time());
    }
}