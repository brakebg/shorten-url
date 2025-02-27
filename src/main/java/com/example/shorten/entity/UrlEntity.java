package com.example.shorten.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Index;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
@Data
@NoArgsConstructor
public class UrlEntity {

    @Id
    @NotBlank(message = "Original URL cannot be blank")
    @Size(max = 2048, message = "URL cannot be longer than 2048 characters")
    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @NotBlank(message = "Short URL cannot be blank")
    @Size(max = 10, message = "Short URL cannot be longer than 10 characters")
    @Column(name = "short_url", nullable = false, length = 10, unique = true)
    @Index(name = "idx_short_url")
    private String shortUrl;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Version
    private Long version;
}
