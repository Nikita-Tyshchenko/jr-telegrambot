package com.github.NikitaTyshchenko.jrtb.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    NO("/no"),
    STAT("/stat");

    private final String commandName;

    CommandName(String commandName){
        this.commandName = commandName;
    }


    public String getCommandName() {
        return commandName;
    }
}
