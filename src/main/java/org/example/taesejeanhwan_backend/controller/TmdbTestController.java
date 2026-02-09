package org.example.taesejeanhwan_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class TmdbTestController {

    @Value("${tmdb.api-key}")
    private String apiKey;

    @Value("${tmdb.base-url}")
    private String baseUrl;

    private final ContentRepository contentRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";


    @PostMapping("/popular/import")
    @Transactional
    public ResponseEntity<String> importPopularMovies(
            @RequestParam(defaultValue = "50") int count
    ) throws Exception {

        int saved = 0;
        int page = 1;

        while (saved < count) {
            String url = baseUrl
                    + "/movie/popular"
                    + "?api_key=" + apiKey
                    + "&language=ko-KR"
                    + "&page=" + page;

            String json = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(json);
            JsonNode results = root.path("results");

            // 더 이상 결과가 없으면 종료
            if (!results.isArray() || results.isEmpty()) break;

            for (JsonNode m : results) {
                if (saved >= count) break;

                String title = m.path("title").asText(null);
                String overview = m.path("overview").asText(null);

                String releaseDate = m.path("release_date").asText("");
                int year = 0;
                if (releaseDate.length() >= 4) {
                    year = Integer.parseInt(releaseDate.substring(0, 4));
                }

                String posterPath = m.path("poster_path").asText(null);
                String posterUrl = (posterPath == null || "null".equals(posterPath))
                        ? null
                        : IMAGE_BASE_URL + posterPath;


                Content content = Content.builder()
                        .title(title)
                        .overview(overview)
                        .year(year)
                        .poster(posterUrl)
                        .build();

                contentRepository.save(content);
                saved++;
            }

            page++;
        }

        return ResponseEntity.ok("success");
    }
}
