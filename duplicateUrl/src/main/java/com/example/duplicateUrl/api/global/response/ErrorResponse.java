package com.example.duplicateUrl.api.global.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
        String message,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<ValidationError> errors,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime serverDateTime
) {
    private static final String ARGUMENT_EXCEPTION_MESSAGE = "입력 값에 오류가 있습니다.";

    public static ErrorResponse from(
            final Exception exception
    ) {
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .serverDateTime(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse from(
            final BindException exception
    ) {
        return ErrorResponse.builder()
                .message(ARGUMENT_EXCEPTION_MESSAGE)
                .errors(ValidationError.of(exception.getBindingResult().getFieldErrors()))
                .serverDateTime(LocalDateTime.now())
                .build();
    }

    @Builder
    public record ValidationError(
            String field,
            String message
    ) {
        private static ValidationError from(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

        private static List<ValidationError> of(final List<FieldError> fieldErrors) {
            return fieldErrors.stream()
                    .map(ValidationError::from)
                    .toList();
        }
    }
}
