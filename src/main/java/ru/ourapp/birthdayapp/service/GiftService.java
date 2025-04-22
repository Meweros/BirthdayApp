package ru.ourapp.birthdayapp.service;

import org.springframework.stereotype.Service;
import ru.ourapp.birthdayapp.model.Gift;
import ru.ourapp.birthdayapp.model.User;
import ru.ourapp.birthdayapp.repository.GiftRepository;

import java.util.List;

@Service
public class GiftService {
    private final GiftRepository giftRepository;

    public GiftService(GiftRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    public List<Gift> getAllGifts() {
        return giftRepository.findAll();
    }

    public Gift createGift(Gift gift, User user) {
        gift.setUser(user);
        return giftRepository.save(gift);
    }

    public List<Gift> getUserGifts(User user) {
        return giftRepository.findByUser(user);
    }

    public List<Gift> getUserGiftsByGiftedStatus(User user, Boolean isGifted) {
        return giftRepository.findByUserAndIsGifted(user, isGifted);
    }

    public Gift updateGift(Gift gift) {
        return giftRepository.save(gift);
    }

    public void deleteGift(Long id) {
        giftRepository.deleteById(id);
    }

    public Gift getGiftById(Long id) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gift not found with id: " + id));
    }
} 