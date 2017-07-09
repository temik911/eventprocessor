package com.eventprocessor.processor.interfaces;

import com.eventprocessor.api.Event;
import com.eventprocessor.service.EventService;

import java.util.Collection;

/**
 * Extension of {@link EventService} to allow save events.
 *
 * @author Artem
 * @since 08.07.2017.
 */
public interface LocalEventService {
    /**
     * Insert events to local storage
     *
     * @param events events to save
     */
    void insertEvents(Collection<Event> events);
}
