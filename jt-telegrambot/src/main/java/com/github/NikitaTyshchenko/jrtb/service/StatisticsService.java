package com.github.NikitaTyshchenko.jrtb.service;

import com.github.NikitaTyshchenko.jrtb.javarushclient.dto.statistics.StatisticDTO;

public interface StatisticsService {
    StatisticDTO countBotStatistics();
}
