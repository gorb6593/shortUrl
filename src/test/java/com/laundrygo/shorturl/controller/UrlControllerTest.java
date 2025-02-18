package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.dto.request.UrlRequestDto;
import com.laundrygo.shorturl.dto.response.UrlResponseDto;
import com.laundrygo.shorturl.entity.Url;
import com.laundrygo.shorturl.exception.UrlNotFoundException;
import com.laundrygo.shorturl.repository.UrlRepository;
import com.laundrygo.shorturl.service.UrlService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UrlServiceIntegrationTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private UrlRepository urlRepository;

    @AfterEach
    void cleanup() {
        urlRepository.deleteAll();
    }

    @Test
    @DisplayName("단축 URL 생성")
    void createNewShortUrl() {
        // given
        String originalUrl = "https://www.naver.com";
        UrlRequestDto request = new UrlRequestDto(originalUrl);

        // when
        UrlResponseDto response = urlService.createShortUrl(request);

        // then
        Url savedUrl = urlRepository.findByShortUrl(response.getShortUrl()).orElseThrow();

        assertThat(savedUrl.getOriginalUrl()).isEqualTo(originalUrl);
        assertThat(savedUrl.getShortUrl()).isEqualTo(response.getShortUrl());
    }

    @Test
    @DisplayName("동일한 URL을 여러 번 요청하면 같은 한 개만 저장")
    void returnSameShortUrlForDuplicateRequests() {
        // given
        String originalUrl = "https://www.naver.com";
        UrlRequestDto request = new UrlRequestDto(originalUrl);

        // when
        UrlResponseDto firstResponse = urlService.createShortUrl(request);
        UrlResponseDto secondResponse = urlService.createShortUrl(request);

        // then
        assertThat(secondResponse.getShortUrl()).isEqualTo(firstResponse.getShortUrl());

        // 데이터베이스에 하나의 레코드만 저장되었는지 확인
        assertThat(urlRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("다른 URL은 다른 단축 URL을 생성한다")
    void createDifferentShortUrlsForDifferentUrls() {
        // given
        String firstUrl = "https://www.naver.com";
        String secondUrl = "https://www.naver2.com";

        UrlRequestDto firstRequest = new UrlRequestDto(firstUrl);
        UrlRequestDto secondRequest = new UrlRequestDto(secondUrl);

        // when
        UrlResponseDto firstResponse = urlService.createShortUrl(firstRequest);
        UrlResponseDto secondResponse = urlService.createShortUrl(secondRequest);

        // then
        assertThat(firstResponse.getShortUrl()).isNotEqualTo(secondResponse.getShortUrl());
        assertThat(urlRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("단축 URL로 원본 URL을 조회할 수 있다")
    void getOriginalUrlFromShortUrl() {
        // given
        String originalUrl = "https://www.naver.com";
        UrlRequestDto request = new UrlRequestDto(originalUrl);
        UrlResponseDto createResponse = urlService.createShortUrl(request);

        // 단축 url
        String shortUrl = createResponse.getShortUrl();

        // when (단축 url로 조회)
        UrlResponseDto response = urlService.getOriginalUrl(shortUrl);

        // then (원본 url)
        Url savedUrl = urlRepository.findByShortUrl(shortUrl).orElseThrow();

        assertThat(savedUrl.getOriginalUrl()).isEqualTo(originalUrl);

        // 조회 횟수가 증가했는지 확인
        assertThat(savedUrl.getAccessCount()).isEqualTo(1);
    }
}