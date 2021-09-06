package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.bot.JRTelegramBot;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@Component
public class SendBotMessageServiceImpl implements SendBotMessageService{


    private final JRTelegramBot jrTelegramBot;

    @Autowired
    public SendBotMessageServiceImpl(JRTelegramBot jrTelegramBot) {
        this.jrTelegramBot = jrTelegramBot;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try{
            jrTelegramBot.execute(sendMessage);
        } catch (TelegramApiException apiException) {
            apiException.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Long chatId, List<String> messages) {
        if (isEmpty(messages)) return;

        messages.forEach(m -> sendMessage(chatId, m));
    }
}
