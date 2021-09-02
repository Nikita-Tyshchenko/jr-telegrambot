package com.github.NikitaTyshchenko.jrtb.javarushclient;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupInfo;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupRequestArgs;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.GroupsCountRequestArgs;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JRGroupClientImpl implements JRGroupClient{

    private final String jrApiGroupPath;

    public JRGroupClientImpl(@Value("${javarush.api.path}") String jrApiGroupPath) {
        this.jrApiGroupPath = jrApiGroupPath + "/groups";
    }

    @Override
    public List<GroupInfo> getGroupList(GroupRequestArgs requestArgs) {
        return Unirest.get(jrApiGroupPath)
                .queryString(requestArgs.populateQueries())
                .asObject(new GenericType<List<GroupInfo>>() {
                })
                .getBody();
    }

    @Override
    public List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs){
        return Unirest.get(jrApiGroupPath)
                .queryString(requestArgs.populateQueries())
                .asObject(new GenericType<List<GroupDiscussionInfo>>() {
                })
                .getBody();
    }

    @Override
    public Integer getGroupCount(GroupsCountRequestArgs countRequestArgs) {
        return Integer.valueOf(
                Unirest.get(String.format("%s/count", jrApiGroupPath))
                        .queryString(countRequestArgs.populateQueries())
                        .asString()
                        .getBody()
        );
    }

    @Override
    public GroupDiscussionInfo getGroupById(Integer id) {
        return Unirest.get(String.format("%s/group%s", jrApiGroupPath ,id.toString()))
                .asObject(GroupDiscussionInfo.class)
                .getBody();
    }
}
