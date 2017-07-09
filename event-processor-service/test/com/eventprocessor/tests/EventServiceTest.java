package com.eventprocessor.tests;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.EventServiceImpl;
import com.eventprocessor.service.EventsCountRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public abstract class EventServiceTest {
    static EventServiceImpl eventService;

    @Test
    public void eventServiceTest() throws Exception {
        List<Event> events = IntStream.rangeClosed(0, 100).mapToObj(Event::new).collect(toList());
        shuffle(events);
        eventService.insertEvents(events);

        assertEquals(101, eventService.getCount(new EventsCountRequest(0, 100)).getCount());
        assertEquals(51, eventService.getCount(new EventsCountRequest(0, 50)).getCount());
        assertEquals(51, eventService.getCount(new EventsCountRequest(50, 100)).getCount());
        assertEquals(26, eventService.getCount(new EventsCountRequest(40, 65)).getCount());
    }

    @Test
    public void bigTest() throws Exception {
        int size = 100_000;
        List<Event> events = IntStream.rangeClosed(0, size).mapToObj(i -> new Event(10)).collect(toList());
        eventService.insertEvents(events);

        Event event1 = new Event(5);
        Event event2 = new Event(20);
        Event event3 = new Event(30);
        eventService.insertEvents(Arrays.asList(event1, event2, event3));

        assertEquals(1, eventService.getCount(new EventsCountRequest(0, 9)).getCount());
        assertEquals(size + 2, eventService.getCount(new EventsCountRequest(0, 10)).getCount());
        assertEquals(size + 3, eventService.getCount(new EventsCountRequest(10, Long.MAX_VALUE)).getCount());
        assertEquals(2, eventService.getCount(new EventsCountRequest(11, Long.MAX_VALUE)).getCount());
    }
}
