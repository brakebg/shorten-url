package com.example.shorten.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UrlRequest {
    @NotBlank(message = "URL cannot be blank")
    @Size(max = 2048, message = "URL cannot be longer than 2048 characters")
    @Pattern(regexp = "^(https?://)?[\\w\\d\\-._~:/?#\\[\\]@!$&'()*+,;=]+$", 
            message = "Invalid URL format")
    private String longUrl;
}
