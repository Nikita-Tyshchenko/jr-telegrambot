package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.javarushclient.JRPostClient;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.post.PostInfo;
import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;
import com.github.NikitaTyshchenko.jrtb.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindNewArticleServiceImpl implements FindNewArticleService{

    public static final String JAVARUSH_WEB_POST_FORMAT = "https://javarush.ru/groups/posts/%s";

    private final GroupSubService groupSubService;
    private final JRPostClient jrPostClient;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewArticleServiceImpl(GroupSubService groupSubService, JRPostClient jrPostClient, SendBotMessageService sendBotMessageService) {
        this.groupSubService = groupSubService;
        this.jrPostClient = jrPostClient;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewArticles() {
        groupSubService.findAll().forEach(groupSub -> {
            List<PostInfo> newPosts = jrPostClient.findNewPosts(groupSub.getId(), groupSub.getLastArticleId());
            setNewLastArticleId(groupSub, newPosts);
            notifySubscribersAboutNewArticles(groupSub, newPosts);
        });
    }

    private void notifySubscribersAboutNewArticles(GroupSub groupSub, List<PostInfo> newPosts){
        Collections.reverse(newPosts);
        List<String> messagesWithNewArticles = newPosts.stream().map(post -> String.format("Вышла новая статья <b>%s</b> в группе <b>%s</b>. \n\n" +
                "<b>Описание:</b> %s\n\n" +
                "<b>Ссылка:</b> %s\n",
                post.getTitle(), groupSub.getTitle(), post.getDescription(), getPostUrl(post.getKey()))).collect(Collectors.toList());

        groupSub.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(user -> sendBotMessageService.sendMessage(Long.valueOf(user.getChatId()), messagesWithNewArticles));
    }

    private void setNewLastArticleId(GroupSub groupSub, List<PostInfo> newPosts){
        newPosts.stream().mapToInt(PostInfo::getId).max().ifPresent(id -> {
            groupSub.setLastArticleId(id);
            groupSubService.save(groupSub);
        });
    }

    private String getPostUrl(String key){
        return String.format(JAVARUSH_WEB_POST_FORMAT, key);
    }
}
