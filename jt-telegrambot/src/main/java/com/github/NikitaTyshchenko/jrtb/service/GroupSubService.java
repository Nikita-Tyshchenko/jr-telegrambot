package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;

public interface GroupSubService {

    GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);

}
