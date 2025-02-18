package com.laundrygo.shorturl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDto {
    @NotNull(message = "URL은 필수 입력값입니다")
    @URL(message = "올바른 URL 형식이 아닙니다")
    private String originalUrl;
}
