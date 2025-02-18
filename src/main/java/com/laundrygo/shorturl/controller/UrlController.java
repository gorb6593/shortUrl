package com.laundrygo.shorturl.controller;

import com.laundrygo.shorturl.dto.request.UrlRequestDto;
import com.laundrygo.shorturl.dto.response.UrlResponseDto;
import com.laundrygo.shorturl.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> createShortUrl(@RequestBody @Valid UrlRequestDto request) {
        return ResponseEntity.ok(urlService.createShortUrl(request));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlResponseDto> getOriginalUrl(@PathVariable String shortUrl) {
        return ResponseEntity.ok(urlService.getOriginalUrl(shortUrl));
    }

    @GetMapping
    public ResponseEntity<List<UrlResponseDto>> getAllUrls() {
        return ResponseEntity.ok(urlService.getAllUrls());
    }
}
