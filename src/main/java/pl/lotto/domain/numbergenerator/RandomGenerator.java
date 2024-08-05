package pl.lotto.domain.numbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.dto.OneRandomNumberResponseDto;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class RandomGenerator implements RandomNumberGenerable {

    private static final int LOWER_BAND = 1;
    private static final int UPPER_BAND = 99;

    private final OneRandomNumberFetcher client;

    @Override
    public Set<Integer> generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        Set<Integer> winningNumbers = new HashSet<>();
        while (isAmountOfNumbersLowerThanSix(winningNumbers)) {
            OneRandomNumberResponseDto randomNumberResponseDto = client.retrieveOneRandomNumber(LOWER_BAND, UPPER_BAND);
            int randomNumber = randomNumberResponseDto.number();
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }

    private boolean isAmountOfNumbersLowerThanSix(Set<Integer> winningNumbers) {
        return winningNumbers.size() < 6;
    }
}
