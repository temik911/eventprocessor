package com.eventprocessor.tests;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.EventsDao;
import com.eventprocessor.utils.DbCreator;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public class EventsDaoTest {
    private static EventsDao eventsDao;

    @Before
    public void setUp() throws FileNotFoundException, SQLException {
        DbCreator.createDB();
        eventsDao = new EventsDao();
    }

    @Test
    public void daoTest() throws Exception {
        List<Event> events = IntStream.rangeClosed(0, 1000).mapToObj(Event::new).collect(toList());
        eventsDao.insert(events);

        assertEquals(1001, eventsDao.getCount(0, 1000));
        assertEquals(501, eventsDao.getCount(0, 500));
        assertEquals(501, eventsDao.getCount(500, 1000));
        assertEquals(251, eventsDao.getCount(400, 650));
    }
}
