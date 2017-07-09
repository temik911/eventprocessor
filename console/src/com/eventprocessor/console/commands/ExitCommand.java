package com.eventprocessor.console.commands;

/**
 * @author Artem
 * @since 11.06.2017.
 */
public class ExitCommand extends Command {
    @Override
    protected void executeImpl(String[] args) {
        System.exit(0);
    }

    @Override
    public String getUsage() {
        return "exit";
    }
}
