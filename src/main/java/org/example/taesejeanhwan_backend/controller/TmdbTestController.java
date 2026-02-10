package org.example.taesejeanhwan_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.Genre;
import org.example.taesejeanhwan_backend.domain.ContentGenre;

import org.example.taesejeanhwan_backend.repository.ContentGenreRepository;
import org.example.taesejeanhwan_backend.repository.ContentRepository;
import org.example.taesejeanhwan_backend.repository.GenreRepository;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/tv")
@RequiredArgsConstructor
public class TmdbTestController {

    @Value("${tmdb.api-key}")
    private String apiKey;

    @Value("${tmdb.base-url}")
    private String baseUrl;

    private final ContentRepository contentRepository;
    private final GenreRepository genreRepository;
    private final ContentGenreRepository contentGenreRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @PostMapping("/popular/import")
    @Transactional
    public ResponseEntity<String> importPopularMovies(
            @RequestParam(defaultValue = "50") int count
    ) throws Exception {

        int saved = 0;
        int page = 6;

        while (saved < count) {
            String url = baseUrl
                    + "/tv/popular"
                    + "?api_key=" + apiKey
                    + "&language=ko-KR"
                    + "&page=" + page;

            String json = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(json);
            JsonNode results = root.path("results");

            if (!results.isArray() || results.isEmpty()) break;

            for (JsonNode m : results) {
                if (saved >= count) break;

                String title = m.path("name").asText(null);
                String overview = m.path("overview").asText(null);

                String releaseDate = m.path("first_air_date").asText("");
                int year = 0;
                if (releaseDate.length() >= 4) {
                    year = Integer.parseInt(releaseDate.substring(0, 4));
                }

                String posterPath = m.path("poster_path").asText(null);
                String posterUrl = (posterPath == null || "null".equals(posterPath))
                        ? null
                        : IMAGE_BASE_URL + posterPath;

                if (contentRepository.existsByTitleAndYearAndPoster(title, year, posterUrl)) {
                    continue;
                }

                Content content = Content.builder()
                        .title(title)
                        .overview(overview)
                        .year(year)
                        .poster(posterUrl)
                        .build();



                Content savedContent = contentRepository.save(content);

                // 2) 장르 연결 저장 (content_genres)
                JsonNode genreIds = m.path("genre_ids");
                if (genreIds.isArray()) {
                    for (JsonNode gidNode : genreIds) {
                        int tmdbGenreId = gidNode.asInt();

                        Genre genre = genreRepository.findByTmdbId(tmdbGenreId).orElse(null);
                        if (genre == null) continue;

                        // 중복 방지
                        if (contentGenreRepository.existsByContentIdAndGenreId(
                                savedContent.getId(), genre.getId()
                        )) continue;

                        contentGenreRepository.save(new ContentGenre(savedContent, genre));
                    }
                }

                saved++;
            }

            page++;
        }

        return ResponseEntity.ok("success");
    }
}
