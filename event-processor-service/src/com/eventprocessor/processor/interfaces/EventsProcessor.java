package com.eventprocessor.processor.interfaces;

import com.eventprocessor.api.Event;

import java.util.Collection;

/**
 * Events processor. Can obtain events from remote side by JMS or RMI, or can locally generate events and process
 * them
 *
 * @author Artem
 * @since 08.07.2017.
 */
public interface EventsProcessor extends AutoCloseable {
    void process(Collection<Event> event);
}
