package com.github.NikitaTyshchenko.jrtb.repository;

import com.github.NikitaTyshchenko.jrtb.repository.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {
    List<TelegramUser> findAllByActiveTrue();
}
