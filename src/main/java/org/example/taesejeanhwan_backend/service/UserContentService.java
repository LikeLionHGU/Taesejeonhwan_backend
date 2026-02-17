package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.Review;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserContent;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetUserContent;
import org.example.taesejeanhwan_backend.repository.ReviewRepository;
import org.example.taesejeanhwan_backend.repository.UserContentRepository;
import org.example.taesejeanhwan_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserContentService {
    private final UserContentRepository userContentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<FeedResponseGetUserContent> getUserContents(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<UserContent> userContents = userContentRepository.findByUserId(userId);

        List<Content> contents = userContents.stream()
                .map(UserContent::getContent)
                .toList();

        List<Review> reviews = reviewRepository.findByUserAndContentIn(user, contents);

        Map<Long, Float> ratingMap = reviews.stream()
                .collect(Collectors.toMap(
                        r -> r.getContent().getId(),
                        Review::getRating
                ));

        return userContents.stream()
                .map(uc -> FeedResponseGetUserContent.builder()
                        .content_id(uc.getContent().getId())
                        .title(uc.getContent().getTitle())
                        .poster(uc.getContent().getPoster())
                        .rating(ratingMap.getOrDefault(uc.getContent().getId(), null))
                        .year(uc.getContent().getYear())
                        .build()
                )
                .toList();
    }

}

