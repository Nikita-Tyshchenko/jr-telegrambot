package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.command.AbstractCommandTest;
import com.github.NikitaTyshchenko.jrtb.command.Command;
import com.github.NikitaTyshchenko.jrtb.command.NoCommand;
import org.junit.jupiter.api.DisplayName;

import static com.github.NikitaTyshchenko.jrtb.command.CommandName.NO;
import static com.github.NikitaTyshchenko.jrtb.command.NoCommand.NO_MESSAGE;


@DisplayName("Unit-level testing for NoCommand")
public class NoCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return NO.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return NO_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new NoCommand(sendBotMessageService);
    }
}
