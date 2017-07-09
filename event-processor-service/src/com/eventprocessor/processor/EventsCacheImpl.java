package com.eventprocessor.processor;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.interfaces.EventsCache;
import com.eventprocessor.service.EventService;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;
import com.eventprocessor.utils.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cache implementation. Always contains events for timestamps from
 * (the newest event timestamp - {@link #CACHE_TIME_RANGE} value) to (the newest event timestamp).
 *
 *
 * @author Artem
 * @since 08.07.2017.
 */
public class EventsCacheImpl implements EventsCache, EventService, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(EventsCacheImpl.class);

    private static final String INVALIDATE_DELAY = "eventsCache.invalidateDelay";
    private static final String CACHE_TIME_RANGE = "eventsCache.cacheTimeRange";

    private final Queue<Event> eventsQueue = new PriorityQueue<>(Comparator.comparingLong(Event::getTimestamp));
    private long fromTime = Long.MIN_VALUE;
    private long toTime = Long.MIN_VALUE;
    private final Lock lock = new ReentrantLock();
    private final long invalidateDelay;
    private final long cacheTimeRange;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public EventsCacheImpl(@Nonnull PropertiesProvider propertiesProvider) {
        this.invalidateDelay = Long.parseLong(propertiesProvider.getProperty(INVALIDATE_DELAY));
        this.cacheTimeRange = Long.parseLong(propertiesProvider.getProperty(CACHE_TIME_RANGE));
    }

    public void start() {
        executorService.scheduleAtFixedRate(this::invalidate, invalidateDelay, invalidateDelay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void insert(Collection<Event> events) {
        lock.lock();
        try {
            log.debug("Events to save into cache: {}", events);
            events.forEach(event -> {
                if (event.getTimestamp() >= fromTime) {
                    eventsQueue.add(event);
                    if (event.getTimestamp() > toTime) {
                        toTime = event.getTimestamp();
                    }
                }
            });
            log.debug("Events {} saved into cache", events);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public EventsCountResponse getCount(EventsCountRequest request) {
        if (request.getFromTime() < fromTime) {
            return EventsCountResponse.FAILED;
        }
        int count = 0;
        lock.lock();
        try {
            for (Event event : eventsQueue) {
                if (event.getTimestamp() >= request.getFromTime() && event.getTimestamp() <= request.getToTime()) {
                    count++;
                }
            }
        } finally {
            lock.unlock();
        }
        return new EventsCountResponse(count);
    }

    private void invalidate() {
        lock.lock();
        try {
            log.debug("Start cache invalidate");
            if (toTime - cacheTimeRange > fromTime) {
                fromTime = toTime - cacheTimeRange;
                log.debug("Cache size before invalidation: {}", eventsQueue.size());
                while (eventsQueue.peek() != null && eventsQueue.peek().getTimestamp() < fromTime) {
                    eventsQueue.poll();
                }
                log.debug("New fromTime {}, elements: {}", fromTime, eventsQueue);
                log.debug("Cache size after invalidation: {}", eventsQueue.size());
            }
            log.debug("Cache invalidated");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws Exception {
        executorService.shutdownNow();
    }
}
