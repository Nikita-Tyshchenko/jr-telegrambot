package com.github.NikitaTyshchenko.jrtb.javarushclient;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupRequestArgs;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupsCountRequestArgs;

import java.util.List;

public interface JRGroupClient {

    List<GroupInfo> getGroupList(GroupRequestArgs requestArgs);

    List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs);

    Integer getGroupCount(GroupsCountRequestArgs countRequestArgs);

    GroupDiscussionInfo getGroupById(Integer id);
}
