package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.group.GroupDiscussionInfo;
import com.github.NikitaTyshchenko.jrtb.repository.entity.GroupSub;

import java.util.List;
import java.util.Optional;

public interface GroupSubService {

    GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);

    GroupSub save(GroupSub groupSub);

    Optional<GroupSub> findById(Integer id);

    List<GroupSub> findAll();

}
