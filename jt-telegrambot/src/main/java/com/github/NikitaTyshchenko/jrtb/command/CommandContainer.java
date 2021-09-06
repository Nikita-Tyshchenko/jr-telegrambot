package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.command.annotation.AdminCommand;
import com.github.NikitaTyshchenko.jrtb.javarushclient.JRGroupClient;
import com.github.NikitaTyshchenko.jrtb.service.GroupSubService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import com.github.NikitaTyshchenko.jrtb.service.StatisticsService;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import static com.github.NikitaTyshchenko.jrtb.command.CommandName.*;
import static java.util.Objects.nonNull;

public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;
    private final List<String> admins;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService,
                            JRGroupClient jrGroupClient, GroupSubService groupSubService, StatisticsService statisticsService, List<String> admins) {

        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, statisticsService))
                .put(ADD_GROUP_SUB.getCommandName(), new AddGroupSubCommand(sendBotMessageService, jrGroupClient, groupSubService))
                .put(LIST_GROUP_SUB.getCommandName(), new GroupSubListCommand(sendBotMessageService, telegramUserService))
                .put(DELETE_GROUP_SUB.getCommandName(), new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService))
                .put(ADMIN_HELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
        this.admins = admins;
    }

    public Command retrieveCommand(String commandIdentifier, String userName) {
        Command orDefault = commandMap.getOrDefault(commandIdentifier, unknownCommand);
        if(isAdminCommand(orDefault)){
            if(admins.contains(userName)){
                return orDefault;
            } else {
                return unknownCommand;
            }
        }
        return orDefault;
    }

    public boolean isAdminCommand(Command command){
        return nonNull(command.getClass().getAnnotation(AdminCommand.class));
    }
}
