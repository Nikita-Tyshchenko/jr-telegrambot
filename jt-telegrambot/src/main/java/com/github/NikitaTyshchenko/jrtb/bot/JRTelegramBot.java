package com.github.NikitaTyshchenko.jrtb.bot;

import com.github.NikitaTyshchenko.jrtb.command.CommandContainer;
import com.github.NikitaTyshchenko.jrtb.javarushclient.JRGroupClient;
import com.github.NikitaTyshchenko.jrtb.service.GroupSubService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageServiceImpl;
import com.github.NikitaTyshchenko.jrtb.service.StatisticsService;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.NikitaTyshchenko.jrtb.command.CommandName.NO;


@Component
public class JRTelegramBot extends TelegramLongPollingBot {

    public static final String COMMAND_PREFIX = "/";

    @Value("${bot.username}")
    private String username;

    @Value(("${bot.token}"))
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public JRTelegramBot(TelegramUserService telegramUserService, JRGroupClient jrGroupClient, GroupSubService groupSubService,
                         StatisticsService statisticsService, @Value("${bot.admins}") List<String> admins){
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), telegramUserService, jrGroupClient,
                groupSubService, statisticsService, admins);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            String username = update.getMessage().getFrom().getUserName();
            if(message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier, username).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName(), username).execute(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
