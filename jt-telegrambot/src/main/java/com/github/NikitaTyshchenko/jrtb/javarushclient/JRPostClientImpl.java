package com.github.NikitaTyshchenko.jrtb.javarushclient;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.post.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JRPostClientImpl implements JRPostClient {

    private final String javaRushApiPostPath;

    public JRPostClientImpl(@Value("${javarush.api.path}") String javaRushApiPostPath) {
        this.javaRushApiPostPath = javaRushApiPostPath + "/posts";
    }

    @Override
    public List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId) {
        List<PostInfo> lastPostsByGroup = Unirest.get(javaRushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupId)
                .queryString("limit", 15)
                .asObject(new GenericType<List<PostInfo>>() {
                }).getBody();
        List<PostInfo> newPosts = new ArrayList<>();
        for (PostInfo post : lastPostsByGroup) {
            if (lastPostId.equals(post.getId())) {
                return newPosts;
            }
            newPosts.add(post);
        }
        return newPosts;
    }
}
