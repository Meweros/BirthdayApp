package ru.ourapp.birthdayapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ourapp.birthdayapp.model.Gift;
import ru.ourapp.birthdayapp.model.User;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    List<Gift> findByUser(User user);
    List<Gift> findByUserAndIsGifted(User user, Boolean isGifted);
} 