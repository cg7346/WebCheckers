package com.webcheckers.model;

import java.util.concurrent.TimeUnit;

/**
 * Timer class for spectator mode
 *
 * @author <a href='mailto:cg7346@rit.edu'>Celeste Gambardella</>
 * @author <a href='mailto:kdv6978@rit.edu'>Kelly Vo</>
 */
public class TimeWatch {

    //
    // Attributes
    //

    long starts;

    //
    // Constructor
    //

    /**
     * A constructor for the timer that resets it
     */
    TimeWatch() {
        reset();
    }

    //
    // Static methods
    //

    /**
     * Starts the timer
     *
     * @return the timer
     */
    public static TimeWatch start() {
        return new TimeWatch();
    }

    /**
     * restarts the timer back to zero
     * @return the current time (zero seconds)
     */
    public TimeWatch reset() {
        starts = System.currentTimeMillis();
        return this;
    }

    /**
     * the amount of time passed by between the start and end of the timer
     * @return the time elapsed
     */
    public long time() {
        long ends = System.currentTimeMillis();
        return ends - starts;
    }

    /**
     * the times units you want to be displayed
     * @param unit for the time (milliseconds, seconds, minutes, hours, etc.)
     * @return the amount of time passed using the units passed in
     */
    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
