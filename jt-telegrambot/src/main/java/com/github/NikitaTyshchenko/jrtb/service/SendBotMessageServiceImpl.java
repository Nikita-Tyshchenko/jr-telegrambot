package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.bot.JRTelegramBot;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageServiceImpl implements SendBotMessageService{


    private final JRTelegramBot jrTelegramBot;

    @Autowired
    public SendBotMessageServiceImpl(JRTelegramBot jrTelegramBot) {
        this.jrTelegramBot = jrTelegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try{
            jrTelegramBot.execute(sendMessage);
        } catch (TelegramApiException apiException) {
            apiException.printStackTrace();
        }
    }
}
