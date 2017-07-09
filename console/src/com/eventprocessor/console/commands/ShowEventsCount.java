package com.eventprocessor.console.commands;

import com.eventprocessor.service.EventService;
import com.eventprocessor.service.EventsCountRequest;
import com.eventprocessor.service.EventsCountResponse;

/**
 * @author Artem
 * @since 09.07.2017.
 */
public class ShowEventsCount extends Command {
    private final EventService eventService;

    public ShowEventsCount(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    protected void executeImpl(String[] args) throws Exception {
        long fromTime = Long.parseLong(args[0]);
        long toTime = Long.parseLong(args[1]);

        EventsCountResponse response = eventService.getCount(new EventsCountRequest(fromTime, toTime));
        System.out.println(response);
    }

    @Override
    public String getUsage() {
        return "eventsCount <fromTime> <toTime>";
    }
}
