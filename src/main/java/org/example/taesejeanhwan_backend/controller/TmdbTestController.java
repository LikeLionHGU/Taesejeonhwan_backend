package org.example.taesejeanhwan_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class TmdbTestController {

    @Value("${tmdb.api-key}")
    private String apiKey;

    @Value("${tmdb.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/popular")
    public ResponseEntity<String> getPopularMovies() {

        String url = baseUrl
                + "/movie/popular"
                + "?api_key=" + apiKey
                + "&language=ko-KR"
                + "&page=2";

        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }
}