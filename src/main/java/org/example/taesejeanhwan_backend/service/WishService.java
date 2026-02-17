package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserWish;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestAddWish;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestDeleteWish;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetWish;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseResult;
import org.example.taesejeanhwan_backend.repository.ContentRepository;
import org.example.taesejeanhwan_backend.repository.UserRepository;
import org.example.taesejeanhwan_backend.repository.UserWishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final UserWishRepository userWishRepository;

    public FeedResponseResult addWish(FeedRequestAddWish feedRequestAddWish) {
        if (feedRequestAddWish.getContent_id() == null|| feedRequestAddWish.getUser_id() == null) {
            throw new IllegalArgumentException("content_id and user_id cannot be null");
        }
        Content wishContent = contentRepository.findById(feedRequestAddWish.getContent_id()).orElseThrow(()->new RuntimeException("Content not found"));
        User wishUser = userRepository.findById(feedRequestAddWish.getUser_id()).orElseThrow(()->new RuntimeException("User not found"));

        userWishRepository.save(
                UserWish.builder()
                        .content(wishContent)
                        .user(wishUser)
                        .build()
        );
        if(userWishRepository.existsByContentAndUser(wishContent, wishUser)) {
            return FeedResponseResult.builder()
                    .result("success")
                    .build();
        }
        return FeedResponseResult.builder()
                .result("fail")
                .build();
    }

    public List<FeedResponseGetWish> getWishList(Long user_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("user_id cannot be null");
        }
        User user = userRepository.findById(user_id).orElseThrow(()->new RuntimeException("User not found"));
        List<Long> contentIds = userWishRepository.findContent_idByUser(user);
        List<Content> contents = contentRepository.findAllById(contentIds);
        return contents.stream()
                .map(FeedResponseGetWish::from)
                .toList();
    }

    public FeedResponseResult deleteWish(FeedRequestDeleteWish req) {
        User user = userRepository.findById(req.getUser_id()).orElseThrow(()->new RuntimeException("User not found"));
        if (user == null) {
            return FeedResponseResult.builder().result("fail").build();
        }

        Content content = contentRepository.findByContentId(req.getContent_id());
        if (content == null) {
            return FeedResponseResult.builder().result("fail").build();
        }

        UserWish wish = userWishRepository.findByContentAndUser(content, user)
                .orElse(null);

        if (wish == null) {
            // 이미 없거나 잘못된 요청
            return FeedResponseResult.builder().result("fail").build();
        }

        userWishRepository.delete(wish);

        return FeedResponseResult.builder()
                .result("success")
                .build();
    }

}
