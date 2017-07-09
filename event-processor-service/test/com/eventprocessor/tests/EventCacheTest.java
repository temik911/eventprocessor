package com.eventprocessor.tests;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.EventsCacheImpl;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;
import com.eventprocessor.utils.TestablePropertiesProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static com.eventprocessor.utils.Synchronisations.waitFor;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public class EventCacheTest {
    private static EventsCacheImpl eventsCache;
    private static int cacheTimeRange = 1000;
    private static int invalidateDelay = 1000;

    @Before
    public void setUp() throws Exception {
        TestablePropertiesProvider propertiesProvider = new TestablePropertiesProvider();
        propertiesProvider.putProperties("eventsCache.invalidateDelay", String.valueOf(invalidateDelay));
        propertiesProvider.putProperties("eventsCache.cacheTimeRange", String.valueOf(cacheTimeRange));

        eventsCache = new EventsCacheImpl(propertiesProvider);
        eventsCache.start();
    }

    @Test
    public void cacheTest() throws Exception {
        List<Event> events = IntStream.rangeClosed(0, 100).mapToObj(Event::new).collect(toList());
        shuffle(events);
        eventsCache.insert(events);

        assertEquals(101, eventsCache.getCount(new EventsCountRequest(0, 100)).getCount());
        assertEquals(51, eventsCache.getCount(new EventsCountRequest(0, 50)).getCount());
        assertEquals(51, eventsCache.getCount(new EventsCountRequest(50, 100)).getCount());
        assertEquals(26, eventsCache.getCount(new EventsCountRequest(40, 65)).getCount());
    }

    @Test
    public void invalidateCacheTest() throws Exception {
        List<Event> events = IntStream.rangeClosed(0, cacheTimeRange + 500).mapToObj(Event::new).collect(toList());
        shuffle(events);
        eventsCache.insert(events);

        waitFor(() -> {
            EventsCountRequest request = new EventsCountRequest(cacheTimeRange - 500, cacheTimeRange + 500);
            assertEquals(cacheTimeRange + 1, eventsCache.getCount(request).getCount());
        });

        waitFor(() -> {
            EventsCountRequest request = new EventsCountRequest(cacheTimeRange - 501, cacheTimeRange + 500);
            assertEquals(EventsCountResponse.FAILED, eventsCache.getCount(request));
        });

        waitFor(() -> {
            EventsCountRequest request = new EventsCountRequest(cacheTimeRange - 500, Long.MAX_VALUE);
            assertEquals(cacheTimeRange + 1, eventsCache.getCount(request).getCount());
        });
    }
}
