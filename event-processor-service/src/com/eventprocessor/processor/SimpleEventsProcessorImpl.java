package com.eventprocessor.processor;

import com.eventprocessor.api.Event;
import com.eventprocessor.processor.interfaces.EventsProcessor;
import com.eventprocessor.processor.interfaces.LocalEventService;
import com.eventprocessor.utils.PropertiesProvider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * Simple event processor which self generate events with period from properties by {@link #PERIOD} key
 *
 * @author Artem
 * @since 08.07.2017.
 */
public class SimpleEventsProcessorImpl implements EventsProcessor {
    private static final String PERIOD = "eventProcessor.generatePeriod";

    private final LocalEventService eventService;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final int period;

    public SimpleEventsProcessorImpl(@Nonnull PropertiesProvider propertiesProvider, @Nonnull LocalEventService eventService) {
        this.eventService = requireNonNull(eventService, "eventService");
        this.period = Integer.parseInt(propertiesProvider.getProperty(PERIOD));
    }

    public void start() {
        executorService.scheduleAtFixedRate(() -> {
            long timestamp = System.currentTimeMillis();
            Event event1 = new Event(timestamp);
            Event event2 = new Event(timestamp - period);
            Event event3 = new Event(timestamp - period * 2);
            Event event4 = new Event(timestamp - period * 4);
            process(Arrays.asList(event1, event2, event3, event4));
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void process(Collection<Event> events) {
        eventService.insertEvents(events);
    }

    @Override
    public void close() throws Exception {
        executorService.shutdownNow();
    }
}
