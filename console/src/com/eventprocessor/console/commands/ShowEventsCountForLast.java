package com.eventprocessor.console.commands;

import com.eventprocessor.service.EventService;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;

import java.util.Arrays;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public class ShowEventsCountForLast extends Command {
    private final EventService eventService;

    public ShowEventsCountForLast(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    protected void executeImpl(String[] args) throws Exception {
        Period period = Period.valueOf(args[0]);

        long currentTime = System.currentTimeMillis();
        EventsCountRequest request = new EventsCountRequest(currentTime - period.timeRange, currentTime);
        EventsCountResponse response = eventService.getCount(request);
        System.out.println(response);
    }

    @Override
    public String getUsage() {
        return "eventsCountForLast <" + Arrays.toString(Period.values()) + ">";
    }

    enum Period {
        MINUTE(60 * 1000),
        HOUR(60 * 60 * 1000),
        DAY(24 * 60 * 60 * 1000);

        private final long timeRange;

        Period(long timeRange) {
            this.timeRange = timeRange;
        }
    }
}
