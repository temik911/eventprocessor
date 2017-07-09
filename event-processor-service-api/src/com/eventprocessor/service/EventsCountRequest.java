package com.eventprocessor.service;

import java.io.Serializable;

/**
 * Request for events count
 *
 * @author Artem
 * @since 08.07.2017.
 */
public class EventsCountRequest implements Serializable {
    private final long fromTime;
    private final long toTime;

    /**
     * @param fromTime from timestamp events needed to be include
     * @param toTime   to timestamp events needed to be include
     */
    public EventsCountRequest(long fromTime, long toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public long getFromTime() {
        return fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    @Override
    public String toString() {
        return "EventsCountRequest{" +
                "fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }
}
