package com.example.shorten.service;

import com.example.shorten.entity.UrlEntity;
import com.example.shorten.exception.NotFoundException;
import com.example.shorten.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlShortenService urlShortenService;

    private static final String LONG_URL = "https://example.com/very/long/url";
    private static final String SHORT_URL = "abc123";
    private UrlEntity urlEntity;

    @BeforeEach
    void setUp() {
        urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(LONG_URL);
        urlEntity.setShortUrl(SHORT_URL);
        urlEntity.setCreatedDate(LocalDateTime.now());
        urlEntity.setExpiryDate(LocalDateTime.now().plusDays(30));
    }

    @Test
    void generateShortUrl_WhenUrlDoesNotExist_ShouldCreateNewShortUrl() {
        // Arrange
        when(urlRepository.findById(LONG_URL)).thenReturn(Optional.empty());
        when(urlRepository.save(any(UrlEntity.class))).thenReturn(urlEntity);

        // Act
        String result = urlShortenService.generateShortUrl(LONG_URL);

        // Assert
        assertThat(result).isNotNull()
                         .isNotEmpty()
                         .hasSize(10); // Base62 generates 10-char URLs
    }

    @Test
    void generateShortUrl_WhenUrlExists_ShouldReturnExistingShortUrl() {
        // Arrange
        when(urlRepository.findById(LONG_URL)).thenReturn(Optional.of(urlEntity));

        // Act
        String result = urlShortenService.generateShortUrl(LONG_URL);

        // Assert
        assertThat(result).isEqualTo(SHORT_URL);
    }

    @Test
    void getOriginalUrl_WhenUrlDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(urlRepository.findByShortUrl(SHORT_URL)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> urlShortenService.getOriginalUrl(SHORT_URL))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("URL not found or expired");
    }

    @Test
    void getOriginalUrl_WhenUrlIsExpired_ShouldThrowNotFoundException() {
        // Arrange
        urlEntity.setExpiryDate(LocalDateTime.now().minusDays(1));
        when(urlRepository.findByShortUrl(SHORT_URL)).thenReturn(urlEntity);

        // Act & Assert
        assertThatThrownBy(() -> urlShortenService.getOriginalUrl(SHORT_URL))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("URL not found or expired");
    }
}
