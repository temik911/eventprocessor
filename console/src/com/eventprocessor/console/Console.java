package com.eventprocessor.console;

import com.eventprocessor.console.commands.Command;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Artem
 * @since 11.06.2017.
 */
public class Console {
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
        ConsoleDispatcher dispatcher = new ConsoleDispatcher();
        dispatcher.init();
        dispatcher.welcomeMessage();

        Scanner scanner = new Scanner(System.in);
        String commandLine = scanner.nextLine();
        //noinspection InfiniteLoopStatement should exit by specific command
        while (true) {
            commandLine = commandLine.trim();
            if (!commandLine.isEmpty()) {
                String[] split = commandLine.trim().split(" ");
                Command command = dispatcher.getCommand(split[0]);
                if (command != null) {
                    command.execute(Arrays.copyOfRange(split, 1, split.length));
                } else {
                    System.out.println("Command not found");
                }
            }
            commandLine = scanner.nextLine();
        }
    }
}
