package com.github.NikitaTyshchenko.jrtb.javarushclient.dto.post;

import lombok.Data;

@Data
public class BaseUserInfo {
    private String city;
    private String country;
    private String displayName;
    private Integer id;
    private String job;
    private String key;
    private Integer level;
    private String pictureUrl;
    private String position;
    private UserPublicStatus userPublicStatus;
    private String publicStatusMessage;
    private Integer rating;
    private Integer userId;
}
