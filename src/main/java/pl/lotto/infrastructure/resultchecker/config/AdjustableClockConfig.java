package pl.lotto.infrastructure.resultchecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.AdjustableClock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
public class AdjustableClockConfig {

    @Bean
    public AdjustableClock adjustableClock() {
        return AdjustableClock.ofLocalDateAndLocalTime(
                LocalDate.of(2022, 11, 16),
                LocalTime.of(10, 0),
                ZoneId.systemDefault()
        );
    }
}
