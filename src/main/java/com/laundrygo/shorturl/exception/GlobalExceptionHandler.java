package com.laundrygo.shorturl.exception;

import com.laundrygo.shorturl.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUrlNotFoundException(UrlNotFoundException e) {
        log.error("URL not found: {}", e.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new ErrorResponse(404, e.getMessage()));
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUrlException(InvalidUrlException e) {
        log.error("Invalid URL: {}", e.getMessage());
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(400, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());

        // 첫 번째 에러 메시지만 가져오기
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("잘못된 요청입니다.");

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(400, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred: ", e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "서버 내부 오류가 발생했습니다."));
    }
}

