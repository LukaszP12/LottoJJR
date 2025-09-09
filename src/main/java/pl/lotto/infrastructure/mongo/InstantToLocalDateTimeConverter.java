package pl.lotto.infrastructure.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class InstantToLocalDateTimeConverter implements Converter<Instant, LocalDateTime> {
    @Override
    public LocalDateTime convert(Instant source) {
        return LocalDateTime.ofInstant(source, ZoneOffset.UTC);
    }
}
