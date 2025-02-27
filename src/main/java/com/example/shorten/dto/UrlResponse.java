package com.example.shorten.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UrlResponse {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdDate;
    private LocalDateTime expiryDate;
}
