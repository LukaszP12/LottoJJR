package pl.lotto.feature.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.infrastructure.numbergenerator.scheduler.WinningNumbersScheduler;

import java.time.Clock;

@Component
@Profile("integration_UserPlayed")
@Log4j2
public class TestWinningNumbersScheduler extends WinningNumbersScheduler {

    public TestWinningNumbersScheduler(WinningNumbersGeneratorFacade facade, Clock clock) {
        super(facade, clock);
    }

    @Override
    public void generateWinningNumbers() {
        log.info("Test scheduler manually triggered");
        super.generateWinningNumbers();
    }
}
