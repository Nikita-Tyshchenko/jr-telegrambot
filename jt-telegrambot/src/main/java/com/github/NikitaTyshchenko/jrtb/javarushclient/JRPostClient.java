package com.github.NikitaTyshchenko.jrtb.javarushclient;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.post.PostInfo;

import java.util.List;

public interface JRPostClient {

    List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId);

}
