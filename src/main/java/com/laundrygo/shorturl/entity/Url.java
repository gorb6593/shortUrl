package com.laundrygo.shorturl.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "urls")
public class Url {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String originalUrl;
    private String shortUrl;
    private Long accessCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private Url(String originalUrl, String shortUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.accessCount = 0L;
    }

    public static Url createShortUrl(String originalUrl, String shortUrl) {
        return Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .build();
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }
}
