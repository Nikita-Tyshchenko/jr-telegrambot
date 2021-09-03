package com.github.NikitaTyshchenko.jrtb.command;

import com.github.NikitaTyshchenko.jrtb.javarushclient.JRGroupClient;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupRequestArgs;
import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;
import com.github.NikitaTyshchenko.jrtb.service.GroupSubService;
import com.github.NikitaTyshchenko.jrtb.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.NikitaTyshchenko.jrtb.command.CommandName.ADD_GROUP_SUB;
import static com.github.NikitaTyshchenko.jrtb.command.CommandUtils.getChatId;
import static com.github.NikitaTyshchenko.jrtb.command.CommandUtils.getMessage;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class AddGroupSubCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final JRGroupClient jrGroupClient;
    private final GroupSubService groupSubService;

    public AddGroupSubCommand(SendBotMessageService sendBotMessageService, JRGroupClient jrGroupClient, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.jrGroupClient = jrGroupClient;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {
        if(getMessage(update).equalsIgnoreCase(ADD_GROUP_SUB.getCommandName())){
            sendGroupIdList(getChatId(update).toString());
            return;
        }
        String groupId = getMessage(update).split(SPACE)[1];
        Long chatId = getChatId(update);
        if(isNumeric(groupId)){
            GroupDiscussionInfo groupById = jrGroupClient.getGroupById(Integer.parseInt(groupId));
            if(isNull(groupById.getId())){
                sendGroupNotFound(chatId, groupId);
            }
            GroupSub savedGroupSub = groupSubService.save(chatId.toString(), groupById);
            sendBotMessageService.sendMessage(chatId.toString(), "Подписался на группу " + savedGroupSub.getTitle());
        } else {
            sendGroupNotFound(chatId, groupId);
        }
    }

    private void sendGroupNotFound(Long chatId, String groupId) {
        String groupNotFoundMessage = "Нет группы с ID = \"%s\"";
        sendBotMessageService.sendMessage(chatId.toString(), String.format(groupNotFoundMessage, groupId));
    }

    private void sendGroupIdList(String chatId){
        String groupIds = jrGroupClient.getGroupList(GroupRequestArgs.builder().build()).stream()
                .map(group -> String.format("%s - %s \n", group.getTitle(), group.getId()))
                .collect(Collectors.joining());

        String message = "Чтобы подписаться на группу - передай комадну вместе с ID группы. \n" +
                "Например: /addGroupSub 16. \n\n" +
                "я подготовил список всех групп - выберай какую хочешь :) \n\n" +
                "имя группы - ID группы \n\n" +
                "%s";

        sendBotMessageService.sendMessage(chatId.toString(), String.format(message, groupIds));
    }
}
