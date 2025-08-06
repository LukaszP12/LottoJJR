package pl.lotto.feature.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pl.lotto.domain.AdjustableClock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@TestConfiguration
public class AdjustableClockTestConfig {

    @Bean
    public AdjustableClock adjustableClock() {
        return AdjustableClock.ofLocalDateAndLocalTime(
                LocalDate.of(2022, 11, 16), LocalTime.of(10, 0),
                ZoneId.systemDefault());
    }
}
