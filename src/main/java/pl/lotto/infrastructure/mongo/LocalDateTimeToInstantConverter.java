package pl.lotto.infrastructure.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeToInstantConverter implements Converter<LocalDateTime, Instant> {
    @Override
    public Instant convert(LocalDateTime source) {
        return source.atZone(ZoneOffset.UTC).toInstant();
    }
}
