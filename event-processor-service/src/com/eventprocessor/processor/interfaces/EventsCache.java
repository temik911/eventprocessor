package com.eventprocessor.processor.interfaces;

import com.eventprocessor.api.Event;
import com.eventprocessor.service.EventService;

import java.util.Collection;

/**
 * Events cache
 *
 * @author Artem
 * @since 08.07.2017.
 */
public interface EventsCache extends EventService {
    void insert(Collection<Event> events);
}
