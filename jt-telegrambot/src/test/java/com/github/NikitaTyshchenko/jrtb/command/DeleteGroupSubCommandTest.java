package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;
import com.github.NikitaTyshchenko.jrtb.repository.entity.TelegramUser;
import com.github.NikitaTyshchenko.jrtb.service.GroupSubService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.NikitaTyshchenko.jrtb.command.AbstractCommandTest.prepareUpdate;
import static com.github.NikitaTyshchenko.jrtb.command.CommandName.DELETE_GROUP_SUB;
import static java.util.Collections.singletonList;

@DisplayName("Unit-level testing for DeleteGroupSubCommand")
public class DeleteGroupSubCommandTest {

    private Command command;
    private SendBotMessageService sendBotMessageService;
    private GroupSubService groupSubService;
    private TelegramUserService telegramUserService;

    @BeforeEach
    public void init(){
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);
        command = new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService);
    }

    @Test
    public void shouldProperlyReturnEmptySubscription(){

        Long chatId = 23456L;
        Update update = prepareUpdate(chatId, DELETE_GROUP_SUB.getCommandName());

        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(new TelegramUser()));

        String expectedMessage = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addgroupsub";

        //when
        command.execute(update);

        //then
        Mockito.verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    @Test
    public void shouldProperlyReturnSubscriptionList(){

        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, DELETE_GROUP_SUB.getCommandName());
        TelegramUser telegramUser = new TelegramUser();
        GroupSub groupSub = new GroupSub();
        groupSub.setId(123);
        groupSub.setTitle("GS1 Title");
        telegramUser.setGroupSubs(singletonList(groupSub));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));

        String expectedMessage = "Чтобы удалить подписку на группу - передай комадну вместе с ID группы. \n" +
                "Например: /deleteGroupSub 16 \n\n" +
                "я подготовил список всех групп, на которые ты подписан) \n\n" +
                "имя группы - ID группы \n\n" +
                "GS1 Title - 123 \n";

        command.execute(update);

        Mockito.verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    @Test
    public void shouldRejectByInvalidGroupID() {

        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, String.format("%s -%s", DELETE_GROUP_SUB.getCommandName(), "groupSubId"));
        TelegramUser telegramUser = new TelegramUser();
        GroupSub groupSub = new GroupSub();
        groupSub.setId(123);
        groupSub.setTitle("GS1 Title");
        telegramUser.setGroupSubs(singletonList(groupSub));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));

        String expectedMessage = "Неправильный формат ID группы. \n" +
                "ID должно быть целым положительным числом";

        command.execute(update);

        Mockito.verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteByGroupId() {
        //given

        /// prepare update object
        Long chatId = 23456L;
        Integer groupId = 1234;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), groupId));


        GroupSub gs1 = new GroupSub();
        gs1.setId(123);
        gs1.setTitle("GS1 Title");
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId.toString());
        telegramUser.setGroupSubs(singletonList(gs1));
        ArrayList<TelegramUser> users = new ArrayList<>();
        users.add(telegramUser);
        gs1.setUsers(users);
        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.of(gs1));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = "Удалил подписку на группу: GS1 Title";

        //when
        command.execute(update);

        //then
        users.remove(telegramUser);
        Mockito.verify(groupSubService).save(gs1);
        Mockito.verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }

    @Test
    public void shouldDoesNotExistByGroupId() {
        //given
        Long chatId = 23456L;
        Integer groupId = 1234;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), groupId));


        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.empty());

        String expectedMessage = "не нашел такой группы =/";

        //when
        command.execute(update);

        //then
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId, expectedMessage);
    }
}
