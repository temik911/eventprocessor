package com.eventprocessor.tests;

import com.eventprocessor.processor.EventServiceImpl;
import com.eventprocessor.processor.EventsCacheImpl;
import com.eventprocessor.processor.EventsDao;
import com.eventprocessor.utils.DbCreator;
import com.eventprocessor.utils.TestablePropertiesProvider;
import org.junit.Before;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public class EventServiceWithCacheTest extends EventServiceTest {
    private static int cacheTimeRange = 1000;
    private static int invalidateDelay = 1000;

    @Before
    public void setUp() throws Exception {
        TestablePropertiesProvider propertiesProvider = new TestablePropertiesProvider();
        propertiesProvider.putProperties("eventsCache.invalidateDelay", String.valueOf(invalidateDelay));
        propertiesProvider.putProperties("eventsCache.cacheTimeRange", String.valueOf(cacheTimeRange));

        EventsCacheImpl eventsCache = new EventsCacheImpl(propertiesProvider);
        eventsCache.start();

        DbCreator.createDB();
        EventsDao eventsDao = new EventsDao();

        eventService = new EventServiceImpl(eventsDao, eventsCache);
    }
}
