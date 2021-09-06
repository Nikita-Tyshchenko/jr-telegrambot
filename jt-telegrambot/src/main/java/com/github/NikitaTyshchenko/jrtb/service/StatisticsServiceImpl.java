package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.statistics.GroupStatDTO;
import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.statistics.StatisticDTO;
import com.github.NikitaTyshchenko.jrtb.repository.entity.TelegramUser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    public StatisticsServiceImpl(GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public StatisticDTO countBotStatistics() {
        List<GroupStatDTO> groupStatDTOS = groupSubService.findAll().stream()
                .filter(it -> CollectionUtils.isEmpty(it.getUsers()))
                .map(groupSub -> new GroupStatDTO(groupSub.getId(), groupSub.getTitle(), groupSub.getUsers().size()))
                .collect(Collectors.toList());
        List<TelegramUser> allInactiveUsers = telegramUserService.findAllInActiveUsers();
        List<TelegramUser> allActiveUsers = telegramUserService.findAllActiveUsers();

        double groupsPerUser = getGroupsPerUser(allActiveUsers);
        return new StatisticDTO(allActiveUsers.size(), allInactiveUsers.size(), groupStatDTOS, groupsPerUser);
    }

    private double getGroupsPerUser(List<TelegramUser> allActiveUsers){
        return (double) allActiveUsers.stream().mapToInt(it -> it.getGroupSubs().size()).sum();
    }
}
