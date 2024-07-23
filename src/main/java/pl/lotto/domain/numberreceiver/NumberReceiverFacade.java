package pl.lotto.domain.numberreceiver;

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
        return "success";
    }

}
