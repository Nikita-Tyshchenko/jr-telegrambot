package com.github.NikitaTyshchenko.jrtb.command;


import com.github.NikitaTyshchenko.jrtb.command.AbstractCommandTest;
import com.github.NikitaTyshchenko.jrtb.command.Command;
import com.github.NikitaTyshchenko.jrtb.command.UnknownCommand;
import org.junit.jupiter.api.DisplayName;

import static com.github.NikitaTyshchenko.jrtb.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit-level testing for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return "dlfksjdflk";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}
