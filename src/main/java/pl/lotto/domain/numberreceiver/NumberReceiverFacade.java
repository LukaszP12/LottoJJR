package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * Klient podaje 6 liczb
 * liczby muszą być w zakresie 1-99
 * liczby nie mogą się powtarzać
 * klient dostaje informacje o dacie losowania
 * klient dostaje informacje o swoim unikalnym identyfikatorze losowania
 **/

@AllArgsConstructor
public class NumberReceiverFacade {

    @Autowired
    private final NumberValidator validator;

    public String inputNumbers(Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            return "success";
        }
        return "failed";
    }

}
