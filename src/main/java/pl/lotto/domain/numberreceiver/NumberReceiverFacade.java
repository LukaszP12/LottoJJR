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
        List<Integer> filteredNumbers = filterAllNumbersInOfRange(numbersFromUser);

        if (areAllNumbersInRange(filteredNumbers)) {
            return "success";
        }
        return "failed";
    }

    private static List<Integer> filterAllNumbersInOfRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(number -> number >= 1)
                .filter(number -> number <= 99)
                .toList();
    }

    private boolean areAllNumbersInRange(List<Integer> filteredNumbers) {
        return filteredNumbers.size() == 6;
    }

}
