package pl.lotto.feature.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.infrastructure.numbergenerator.scheduler.WinningNumbersScheduler;

@Profile("integration_UserPlayed")
@TestConfiguration
public class TestWinningNumbersSchedulerConfig {

    @Bean
    @Primary
    public WinningNumbersScheduler testWinningNumbersScheduler(
            WinningNumbersGeneratorFacade facade,
            AdjustableClock clock
    ) {
        return new WinningNumbersScheduler(facade, clock);
    }
}
