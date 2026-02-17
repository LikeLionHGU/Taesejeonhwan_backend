package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.UserContent;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetUserContent;
import org.example.taesejeanhwan_backend.repository.UserContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserContentService {
    private final UserContentRepository userContentRepository;

    public List<FeedResponseGetUserContent> getUserContents(Long userId) {

        List<UserContent> userContents = userContentRepository.findByUserId(userId);

        return userContents.stream()
                .map(uc -> FeedResponseGetUserContent.builder()
                        .content_id(uc.getContent().getId())
                        .title(uc.getContent().getTitle())
                        .poster(uc.getContent().getPoster())
                        .rating(null) //일단 rating이 어디서 오는지 확인하고 교체하기
                        .year(uc.getContent().getYear())
                        .build()
                )
                .toList();
    }

}

