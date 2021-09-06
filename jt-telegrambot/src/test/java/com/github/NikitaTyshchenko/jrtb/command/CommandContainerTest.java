package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.command.Command;
import com.github.NikitaTyshchenko.jrtb.command.CommandContainer;
import com.github.NikitaTyshchenko.jrtb.command.CommandName;
import com.github.NikitaTyshchenko.jrtb.command.UnknownCommand;
import com.github.NikitaTyshchenko.jrtb.javarushclient.JRGroupClient;
import com.github.NikitaTyshchenko.jrtb.service.GroupSubService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import com.github.NikitaTyshchenko.jrtb.service.StatisticsService;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;

@DisplayName("Unit-level testing for CommandContainer")
public class CommandContainerTest {

    private CommandContainer commandContainer;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        JRGroupClient groupClient = Mockito.mock(JRGroupClient.class);
        GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);
        commandContainer = new CommandContainer(sendBotMessageService,
                telegramUserService,
                groupClient,
                groupSubService,
                statisticsService,
                singletonList("username"));
    }

    @Test
    public void shouldGetAllExistingCommands(){
        Arrays.stream(CommandName.values()).forEach(commandName -> {
            Command command = commandContainer.retrieveCommand(commandName.getCommandName(), "username");
            Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
        });
    }

    @Test
    public void shouldReturnUnknownCommand(){
        String unknownCommand = "sdfijpfafa";

        Command command = commandContainer.retrieveCommand(unknownCommand, "username");

        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }
}
