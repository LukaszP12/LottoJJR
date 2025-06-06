package pl.lotto;

import org.junit.jupiter.api.Test;
import pl.lotto.feature.BaseIntegrationTest;

public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_user_win_and_system_should_generate_winners_() {

        // step 0: external service returns 6 random numbers (1,2,3,4,5,6)

        // step 1: user made POST /inputNumbers with 6 numbers (1,2,3,4,5,6) at 16-11-2022 10:00 and system returned OK(200) with message: "SUCCESS"

        // step 2: system generated winning numbers for draw date: (19.11.2022 12:01)

        // step 3: 3 days and 1 minute passed, and it is 1 minute after the draw date (19.11.2022 12:01)

        // step 4: system generated result TicketId: sampleTicketId with draw date 19.11.2022, and saved it with 6 hits

        // step 5: 3 hours passed, and it is 1 minute after annoucment time (19.11.2022 15:01)

        // step 6: user made GET /results/sampleTicketId and system returned 200 (OK)

    }

}
