package com.example.shorten.service;

import com.example.shorten.entity.UrlEntity;
import com.example.shorten.repository.UrlRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class CleanExpiredUrlService {

    private UrlRepository urlRepository;

    public CleanExpiredUrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Async
    @Scheduled(fixedRate = 100000)
    @Transactional
    public void cleanExpiredUrl() {
        System.out.println("Start -> Clean expired url");
        LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<UrlEntity> expiredUrls = urlRepository.findAllByCreatedDateBefore(now.minusMinutes(5));
        urlRepository.deleteAll(expiredUrls);
    }
}
