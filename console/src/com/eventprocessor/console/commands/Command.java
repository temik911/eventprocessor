package com.eventprocessor.console.commands;

/**
 * @author Artem
 * @since 11.06.2017.
 */
public abstract class Command {
    public final void execute(String[] args) {
        try {
            executeImpl(args);
        } catch (Exception exception) {
            System.out.println("Something went wrong: " + exception.getMessage());
            System.out.println("Command usage: " + getUsage());
        }
    }

    protected abstract void executeImpl(String[] args) throws Exception;
    public abstract String getUsage();
}
