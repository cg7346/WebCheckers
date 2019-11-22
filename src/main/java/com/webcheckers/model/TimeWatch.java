package com.webcheckers.model;

import java.util.concurrent.TimeUnit;

/**
 * Timer class for spectator mode
 */
public class TimeWatch {
    long starts;

    private TimeWatch() {
        reset();
    }

    public static TimeWatch start() {
        return new TimeWatch();
    }

    public TimeWatch reset() {
        starts = System.currentTimeMillis();
        return this;
    }

    public long time() {
        long ends = System.currentTimeMillis();
        return ends - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
