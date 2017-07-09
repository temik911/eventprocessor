package com.eventprocessor.console;


import com.eventprocessor.console.commands.Command;
import com.eventprocessor.console.commands.ExitCommand;
import com.eventprocessor.console.commands.ShowEventsCount;
import com.eventprocessor.console.commands.ShowEventsCountForLast;
import com.eventprocessor.service.EventService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem
 * @since 11.06.2017.
 */
class ConsoleDispatcher {
    private final Map<String, Command> commands = new HashMap<>();
    private final EventService eventService;

    ConsoleDispatcher() throws RemoteException, NotBoundException, MalformedURLException {
        String objectName = "rmi://localhost:8605/" + EventService.class.getName();
        eventService = (EventService) Naming.lookup(objectName);
    }

    void init() throws RemoteException, NotBoundException, MalformedURLException {
        commands.put("eventsCount", new ShowEventsCount(eventService));
        commands.put("eventsCountForLast", new ShowEventsCountForLast(eventService));
        commands.put("exit", new ExitCommand());
        commands.put("help", new Command() {
            @Override
            protected void executeImpl(String[] args) {
                commands.forEach((s, command) -> {
                    System.out.println("Command: " + command.getUsage());
                });
            }

            @Override
            public String getUsage() {
                return "help";
            }
        });
    }

    Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    void welcomeMessage() {
        System.out.println("Welcome to console. Use 'help' to see all commands");
    }
}
