package com.example.duplicateUrl.api.global.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        int status,

        T data,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime serverTime
) {

    public static <E> ApiResponse<E> ok(E data) {
        return new ApiResponse<>(200, data, LocalDateTime.now());
    }

    public static <E> ApiResponse<E> created(E data) {
        return new ApiResponse<>(201, data, LocalDateTime.now());
    }
}
