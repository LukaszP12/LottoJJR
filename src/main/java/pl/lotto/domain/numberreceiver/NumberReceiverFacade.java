package pl.lotto.domain.numberreceiver;

import java.util.List;
import java.util.Set;

/**
 * Klient podaje 6 liczb
 * liczby muszą być w zakresie 1-99
 * liczby nie mogą się powtarzać
 * klient dostaje informacje o dacie losowania
 * klient dostaje informacje o swoim unikalnym identyfikatorze losowania
 **/

public class NumberReceiverFacade {

    public String inputNumbers(Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            return "success";
        }
        return "failed";
    }

    private boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(number -> number >= 1)
                .filter(number -> number <= 99)
                .count() == 6;
    }
}
