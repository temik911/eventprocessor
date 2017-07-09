package com.eventprocessor.api;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Artem
 * @since 08.07.2017.
 */
public class Event {
    private final long timestamp;

    public Event(long timestamp) {
        checkArgument(timestamp >= 0, "timestamp should be positive value");
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "timestamp=" + timestamp +
                '}';
    }
}
