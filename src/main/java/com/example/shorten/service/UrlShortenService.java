package com.example.shorten.service;

import com.example.shorten.entity.UrlEntity;
import com.example.shorten.exception.NotFoundException;
import com.example.shorten.repository.UrlRepository;
import com.example.shorten.utility.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortenService {

    private static final int URL_EXPIRY_DAYS = 30;

    private final UrlRepository urlRepository;

    @Transactional
    public String generateShortUrl(String longUrl) {
        return urlRepository.findById(longUrl)
                .map(UrlEntity::getShortUrl)
                .orElseGet(() -> createNewShortUrl(longUrl));
    }

    @Transactional(readOnly = true)
    public String getOriginalUrl(String shortUrl) {
        UrlEntity urlEntity = urlRepository.findByShortUrl(shortUrl);
        return getOriginalUrlOrThrow(urlEntity, shortUrl);
    }

    private String createNewShortUrl(String longUrl) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(longUrl);
        urlEntity.setShortUrl(Base62.generateShortUrl(longUrl));
        urlEntity.setCreatedDate(LocalDateTime.now());
        urlEntity.setExpiryDate(LocalDateTime.now().plus(URL_EXPIRY_DAYS, ChronoUnit.DAYS));

        urlRepository.save(urlEntity);
        log.info("Created new short URL: {} for original URL: {}", urlEntity.getShortUrl(), longUrl);

        return urlEntity.getShortUrl();
    }

    private String getOriginalUrlOrThrow(UrlEntity urlEntity, String shortUrl) {
        return Optional.ofNullable(urlEntity)
                .filter(this::isNotExpired)
                .map(UrlEntity::getOriginalUrl)
                .orElseThrow(() -> new NotFoundException("URL not found or expired for short URL: " + shortUrl));
    }

    private boolean isNotExpired(UrlEntity entity) {
        return entity.getExpiryDate() == null ||
                entity.getExpiryDate().isAfter(LocalDateTime.now());
    }
}
