package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.bot.JRTelegramBot;
import com.github.NikitaTyshchenko.jrtb.command.Command;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageServiceImpl;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class AbstractCommandTest {

    protected JRTelegramBot jrTelegramBot = Mockito.mock(JRTelegramBot.class);
    protected SendBotMessageService sendBotMessageService = new SendBotMessageServiceImpl(jrTelegramBot);
    protected TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

    abstract String getCommandName();

    abstract String getCommandMessage();

    abstract Command getCommand();

    @Test
    public void shouldProperlyExecuteCommand() throws TelegramApiException {
        //given id
        Long chatId = 1234566789L;

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(getCommandName());
        update.setMessage(message);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getCommandMessage());
        sendMessage.enableHtml(true);

        //when
        getCommand().execute(update);

        //then
        Mockito.verify(jrTelegramBot).execute(sendMessage);
    }

    public static Update prepareUpdate(Long chatId, String commandName) {
        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getText()).thenReturn(commandName);
        update.setMessage(message);
        return update;
    }
}
