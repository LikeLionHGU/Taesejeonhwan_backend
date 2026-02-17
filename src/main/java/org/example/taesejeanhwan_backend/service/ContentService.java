package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.Genre;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetGenre;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseContent;
import org.example.taesejeanhwan_backend.repository.ContentRepository;
import org.example.taesejeanhwan_backend.repository.GenreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final ContentRepository contentRepository;
    private final GenreRepository genreRepository;

    public List<UserResponseContent> getContentsForSelection() {
        List<Content> contents = contentRepository.findAll();
        return contents.stream()
                .map(UserResponseContent::from)
                .limit(100)
                .toList();
    }

    public List<FeedResponseGetGenre> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(FeedResponseGetGenre::from)
                .toList();
    }

}
