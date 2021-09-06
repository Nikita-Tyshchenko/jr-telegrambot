package com.github.NikitaTyshchenko.jrtb.javarushclient.dto.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GroupStatDTO {
    private final Integer id;
    private final String title;
    private final Integer activeUserCount;
}
