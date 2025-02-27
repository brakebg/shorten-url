package com.example.shorten.controller;

import com.example.shorten.dto.ApiResponse;
import com.example.shorten.dto.UrlRequest;
import com.example.shorten.service.UrlShortenService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UrlController {

    private final UrlShortenService urlShortenService;

    @PostMapping
    @RateLimiter(name = "apiRateLimiter", fallbackMethod = "rateLimitFallback")
    public ResponseEntity<ApiResponse<String>> shortenUrl(@Valid @RequestBody UrlRequest request) {
        log.info("Received request to shorten URL: {}", request.getLongUrl());
        String response = urlShortenService.generateShortUrl(request.getLongUrl());
        return ResponseEntity.ok(new ApiResponse<>(true, "URL shortened successfully", response));
    }

    @GetMapping("/{shortUrl}")
    @RateLimiter(name = "apiRateLimiter", fallbackMethod = "rateLimitFallback")
    public ResponseEntity<ApiResponse<String>> getOriginalUrl(
            @PathVariable("shortUrl") String shortUrl) {
        log.info("Received request to expand short URL: {}", shortUrl);
        String response = urlShortenService.getOriginalUrl(shortUrl);
        return ResponseEntity.ok(new ApiResponse<>(true, "URL retrieved successfully", response));
    }

    public ResponseEntity<ApiResponse<String>> rateLimitFallback(String shortUrl, Throwable ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ApiResponse<>(
                        false,
                        "You have exceeded the rate limit within a minute. Please try again later.",
                        shortUrl
                ));
    }


}
