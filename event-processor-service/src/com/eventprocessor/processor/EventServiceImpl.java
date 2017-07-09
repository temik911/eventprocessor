package com.eventprocessor.processor;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.interfaces.EventsCache;
import com.eventprocessor.processor.interfaces.LocalEventService;
import com.eventprocessor.service.EventService;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * Service side eventService implementation
 *
 * @author Artem
 * @since 08.07.2017.
 */
public class EventServiceImpl extends UnicastRemoteObject implements EventService, LocalEventService {
    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventsDao eventsDao;
    private final EventsCache eventsCache;

    public EventServiceImpl(@Nonnull EventsDao eventsDao, @Nonnull EventsCache eventsCache) throws RemoteException {
        super();
        this.eventsDao = requireNonNull(eventsDao, "eventsDao");
        this.eventsCache = requireNonNull(eventsCache, "eventsCache");
    }

    @Override
    public void insertEvents(Collection<Event> events) {
        if (events.isEmpty()) {
            return;
        }
        log.debug("Event to insert: {}", events);
        try {
            eventsDao.insert(events);
            eventsCache.insert(events);
            log.debug("Events inserted: {}", events);
        } catch (SQLException e) {
            log.error("Events {} processed with error: {}", events, e.getStackTrace());
        }
    }

    @Override
    public EventsCountResponse getCount(EventsCountRequest request) throws RemoteException {
        log.debug("Request {} received", request);
        EventsCountResponse response = eventsCache.getCount(request);
        if (response != EventsCountResponse.FAILED) {
            return response;
        }

        int count;
        try {
            count = eventsDao.getCount(request.getFromTime(), request.getToTime());
        } catch (SQLException e) {
            log.error("Error while process request {}: {}", request, e.getStackTrace());
            return EventsCountResponse.FAILED;
        }
        return new EventsCountResponse(count);
    }
}
