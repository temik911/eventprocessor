package com.eventprocessor.tests;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.EventServiceImpl;
import com.eventprocessor.processor.EventsDao;
import com.eventprocessor.processor.interfaces.EventsCache;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;
import com.eventprocessor.utils.DbCreator;
import org.junit.Before;

import java.rmi.RemoteException;
import java.util.Collection;

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
