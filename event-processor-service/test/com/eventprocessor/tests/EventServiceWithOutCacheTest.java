package com.eventprocessor.tests;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.EventServiceImpl;
import com.eventprocessor.processor.EventsCacheImpl;
import com.eventprocessor.processor.EventsDao;
import com.eventprocessor.processor.interfaces.EventsCache;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;
import com.eventprocessor.utils.DbCreator;
import com.eventprocessor.utils.TestablePropertiesProvider;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public class EventServiceWithOutCacheTest extends EventServiceTest {
    @Before
    public void setUp() throws Exception {
        EventsCache eventsCache = new EventsCache() {
            @Override
            public void insert(Collection<Event> events) {
            }

            @Override
            public EventsCountResponse getCount(EventsCountRequest request) throws RemoteException {
                return EventsCountResponse.FAILED;
            }
        };

        DbCreator.createDB();
        EventsDao eventsDao = new EventsDao();

        eventService = new EventServiceImpl(eventsDao, eventsCache);
    }
}
