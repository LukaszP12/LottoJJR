package pl.lotto.domain.numberreceiver;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class NumberValidator {

    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(number -> number >= 1)
                .filter(number -> number <= 99)
                .count() == 6;
    }

}
