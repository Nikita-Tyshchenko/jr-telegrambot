package com.github.NikitaTyshchenko.jrtb.javarushclient.dto.group;

import lombok.Data;

@Data
public class UserDiscussionInfo {
    private Boolean isBookmarked;
    private Integer lastTime;
    private Integer newCommentsCount;
}
