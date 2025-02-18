package com.laundrygo.shorturl.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.laundrygo.shorturl.entity.Url;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.*;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Builder
@Getter
@JsonInclude(NON_NULL)
public class UrlResponseDto {

    private String originalUrl;
    private String shortUrl;
    private Long accessCount;

    public static UrlResponseDto fromShortUrl(Url url) {
        return UrlResponseDto.builder()
                .shortUrl(url.getShortUrl())
                .build();
    }

    public static UrlResponseDto fromOriginalUrl(Url url) {
        return UrlResponseDto.builder()
                .originalUrl(url.getShortUrl())
                .build();
    }

    public static UrlResponseDto from(Url url) {
        return UrlResponseDto.builder()
                .originalUrl(url.getOriginalUrl())
                .shortUrl(url.getShortUrl())
                .accessCount(url.getAccessCount())
                .build();
    }
}
