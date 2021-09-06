package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;
import com.github.NikitaTyshchenko.jrtb.repository.entity.TelegramUser;
import com.github.NikitaTyshchenko.jrtb.service.GroupSubService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import com.github.NikitaTyshchenko.jrtb.service.TelegramUserService;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.NikitaTyshchenko.jrtb.command.CommandName.DELETE_GROUP_SUB;
import static com.github.NikitaTyshchenko.jrtb.command.CommandUtils.getChatId;
import static com.github.NikitaTyshchenko.jrtb.command.CommandUtils.getMessage;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class DeleteGroupSubCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final GroupSubService groupSubService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {
        if(getMessage(update).equalsIgnoreCase(DELETE_GROUP_SUB.getCommandName())){
            sendGroupList(getChatId(update).toString());
            return;
        }

        String groupId = getMessage(update).split(SPACE)[1];
        String chatId = getChatId(update).toString();
        if(isNumeric(groupId)){
            Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.valueOf(groupId));
            if(optionalGroupSub.isPresent()){
                GroupSub groupSub = optionalGroupSub.get();
                TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                groupSub.getUsers().remove(telegramUser);
                groupSubService.save(groupSub);
                sendBotMessageService.sendMessage(Long.valueOf(chatId), format("Удалил подписку на группу: %s", groupSub.getTitle()));
            } else {
                sendBotMessageService.sendMessage(Long.valueOf(chatId), "не нашел такой группы =/");
            }
        } else {
            sendBotMessageService.sendMessage(Long.valueOf(chatId), "Неправильный формат ID группы. \n" +
                    "ID должно быть целым положительным числом");
        }
    }

    private void sendGroupList(String chatId){
        String message;
        List<GroupSub> groupSubs = telegramUserService.findByChatId(chatId)
                .orElseThrow(NotFoundException::new).getGroupSubs();

        if(CollectionUtils.isEmpty(groupSubs)){
            message = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addgroupsub";
        } else {
            String userGroupSubData = groupSubs.stream()
                    .map(group -> format("%s - %s \n", group.getTitle(), group.getId()))
                    .collect(Collectors.joining());

            message = String.format("Чтобы удалить подписку на группу - передай комадну вместе с ID группы. \n" +
                    "Например: /deleteGroupSub 16 \n\n" +
                    "я подготовил список всех групп, на которые ты подписан) \n\n" +
                    "имя группы - ID группы \n\n" +
                    "%s", userGroupSubData);
        }
        sendBotMessageService.sendMessage(Long.valueOf(chatId), message);
    }
}
