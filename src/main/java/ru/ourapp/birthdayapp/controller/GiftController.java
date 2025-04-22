package ru.ourapp.birthdayapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.ourapp.birthdayapp.model.Gift;
import ru.ourapp.birthdayapp.model.User;
import ru.ourapp.birthdayapp.service.GiftService;

import java.util.List;

@RestController
@RequestMapping("/api/gifts")
public class GiftController {
    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @GetMapping
    public ResponseEntity<List<Gift>> getAllGifts() {
        return ResponseEntity.ok(giftService.getAllGifts());
    }

    @PostMapping
    public ResponseEntity<Gift> createGift(@RequestBody Gift gift, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(giftService.createGift(gift, user));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Gift>> getMyGifts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(giftService.getUserGifts(user));
    }

    @GetMapping("/my/gifted/{isGifted}")
    public ResponseEntity<List<Gift>> getMyGiftsByGiftedStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Boolean isGifted) {
        return ResponseEntity.ok(giftService.getUserGiftsByGiftedStatus(user, isGifted));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gift> updateGift(@PathVariable Long id, @RequestBody Gift gift) {
        gift.setId(id);
        return ResponseEntity.ok(giftService.updateGift(gift));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGift(@PathVariable Long id) {
        giftService.deleteGift(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gift> getGiftById(@PathVariable Long id) {
        return ResponseEntity.ok(giftService.getGiftById(id));
    }
} 