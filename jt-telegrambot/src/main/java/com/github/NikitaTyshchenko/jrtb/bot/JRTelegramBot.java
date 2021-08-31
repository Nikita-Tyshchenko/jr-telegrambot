package com.github.NikitaTyshchenko.jrtb.bot;

import com.github.NikitaTyshchenko.jrtb.command.CommandContainer;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageServiceImpl;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.NikitaTyshchenko.jrtb.command.CommandName.NO;


@Component
public class JRTelegramBot extends TelegramLongPollingBot {

    public static final String COMMAND_PREFIX = "/";

    @Value("${bot.username}")
    private String username;

    @Value(("${bot.token}"))
    private String token;

    private final CommandContainer commandContainer;

    public JRTelegramBot(TelegramUserService telegramUserService){
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this), telegramUserService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText();
            if(message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
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
