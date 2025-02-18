package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.dto.request.UrlRequestDto;
import com.laundrygo.shorturl.dto.response.UrlResponseDto;
import com.laundrygo.shorturl.entity.Url;
import com.laundrygo.shorturl.exception.UrlNotFoundException;
import com.laundrygo.shorturl.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public UrlResponseDto createShortUrl(UrlRequestDto request) {
        return urlRepository.findByOriginalUrl(request.getOriginalUrl())
                .map(UrlResponseDto::fromShortUrl)
                .orElseGet(() -> createNewShortUrl(request.getOriginalUrl()));
    }

    private UrlResponseDto createNewShortUrl(String originalUrl) {

        // 단축 url 생성
        String shortUrl = createShortUrl(originalUrl);

        // 단축 url 저장
        Url url = urlRepository.save(Url.createShortUrl(originalUrl, shortUrl));

        return UrlResponseDto.fromShortUrl(url);
    }

    private String createShortUrl(String url) {
        try {
            // MD5 해시 알고리즘
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(url.getBytes());

            // Base64 인코딩하여 문자열로 변환(8자리)
            return Base64.getUrlEncoder()
                    .encodeToString(hashBytes)
                    .substring(0, 8);

        } catch (Exception e) {
            throw new RuntimeException("URL 단축 중 오류 발생", e);
        }
    }

    @Transactional
    public UrlResponseDto getOriginalUrl(String shortUrl) {

        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL을 찾을 수 없습니다."));

        url.incrementAccessCount();

        return UrlResponseDto.fromOriginalUrl(url);
    }

    public List<UrlResponseDto> getAllUrls() {
        return urlRepository.findAll().stream()
                .map(UrlResponseDto::from)
                .collect(Collectors.toList());
    }

}
