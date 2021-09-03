package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.repository.entity.TelegramUser;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;

import java.util.stream.Collectors;

import static com.github.NikitaTyshchenko.jrtb.command.CommandUtils.getChatId;

public class GroupSubListCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public GroupSubListCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        TelegramUser telegramUser = telegramUserService.findByChatId(getChatId(update).toString()).orElseThrow(NotFoundException::new);

        String message = "Я нашел все подписки на группы \n\n";
        String collectedGroups = telegramUser.getGroupSubs().stream()
                .map(sub -> "Группа: " + sub.getTitle() + " , ID = " + sub.getId() + " \n")
                .collect(Collectors.joining());

        sendBotMessageService.sendMessage(telegramUser.getChatId(), message + collectedGroups);
    }
}
