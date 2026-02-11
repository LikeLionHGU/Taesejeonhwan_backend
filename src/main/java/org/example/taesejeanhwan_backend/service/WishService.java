package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserWish;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestAddWish;
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
        Content wishContent = contentRepository.findByContentId(feedRequestAddWish.getContent_id());
        User wishUser = userRepository.findByUser_id(feedRequestAddWish.getUser_id());

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
        User user = userRepository.findByUser_id(user_id);
        List<Long> contentIds = userWishRepository.findContent_idByUser(user);
        List<Content> contents = contentRepository.findAllById(contentIds);
        return contents.stream()
                .map(FeedResponseGetWish::from)
                .toList();
    }
}
