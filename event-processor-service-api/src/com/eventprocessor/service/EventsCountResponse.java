package com.eventprocessor.service;

import java.io.Serializable;

/**
 * Response for {@link EventsCountRequest}
 *
 * @author Artem
 * @since 09.07.2017.
 */
public class EventsCountResponse implements Serializable {
    public static final EventsCountResponse FAILED = new EventsCountResponse(-1);

    private final int count;

    public EventsCountResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "EventsCountResponse{" +
                "count=" + count +
                '}';
    }
}
