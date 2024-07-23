package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class NumberReceiverFacadeTest {

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        //then
        Assertions.assertEquals("success", result);
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 4, 5, 6));
        //then
        Assertions.assertEquals("failed", result);
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6, 7));
        //then
        Assertions.assertEquals("failed", result);
    }

    @Test
    public void should_return_failed_when_user_gave_at_least_one_number_out_of_range_of_1_to_99() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumbers(Set.of(1, 2000, 3000, 3, 4, 5, 6));
        //then
        Assertions.assertEquals("failed", result);
    }
}