package com.github.NikitaTyshchenko.jrtb.javarushclient;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.group.GroupDiscussionInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.group.GroupInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.group.GroupRequestArgs;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.group.GroupsCountRequestArgs;

import java.util.List;

public interface JRGroupClient {

    List<GroupInfo> getGroupList(GroupRequestArgs requestArgs);

    List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs);

    Integer getGroupCount(GroupsCountRequestArgs countRequestArgs);

    GroupDiscussionInfo getGroupById(Integer id);
}
