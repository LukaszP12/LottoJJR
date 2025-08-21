package pl.lotto;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.AdjustableClock;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
@Profile("integration_UserPlayed")
public class UserPlayedLottoIntegrationConfiguration {

    @Bean
    public AdjustableClock adjustableClock() {
        return AdjustableClock.ofLocalDateAndLocalTime(
                LocalDate.of(2023, 2, 19),
                LocalTime.of(12, 0),
                ZoneId.systemDefault()
        );
    }
    @Bean
    public RestTemplate integrationRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(1000))
                .setReadTimeout(Duration.ofMillis(1000))
                .build();
    }
}

//    @Bean
//    @Primary
//    public Clock clock() {
//        return adjustableClock();
//    }
