package com.example.shorten.repository;

import com.example.shorten.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {
     UrlEntity findByShortUrl(String shortUrl);

     @Query("SELECT u FROM UrlEntity u WHERE u.createdDate < :createdDate")
     List<UrlEntity> findAllByCreatedDateBefore(@Param("createdDate") LocalDateTime createdDate);
}
