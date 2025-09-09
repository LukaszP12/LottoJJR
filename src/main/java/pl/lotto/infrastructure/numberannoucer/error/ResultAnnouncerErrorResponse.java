package pl.lotto.infrastructure.numberannoucer.error;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerErrorResponse(
        String message,
        HttpStatus status) {
}
