package pl.lotto.infrastructure.numberannoucer.controller.error;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerErrorResponse(
        String message,
        HttpStatus status) {
}
