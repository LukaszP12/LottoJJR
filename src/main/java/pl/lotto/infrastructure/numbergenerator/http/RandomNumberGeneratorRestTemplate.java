package pl.lotto.infrastructure.numbergenerator.http;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbergenerator.RandomNumberGenerable;
import pl.lotto.domain.numbergenerator.SixRandomNumbersDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Primary
@Component
public class RandomNumberGeneratorRestTemplate implements RandomNumberGenerable {

    public static final int MAXIMAL_WINNING_NUMBERS = 6;

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    private static final int MAX_RETRIES = 3;

    public RandomNumberGeneratorRestTemplate(
            RestTemplate restTemplate,
            @Value("${lotto.number-generator.http.client.config.uri}") String uri,
            @Value("${lotto.number-generator.http.client.config.port}") int port) {
        this.restTemplate = restTemplate;
        this.uri = uri;
        this.port = port;
    }

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        log.info("Started fetching winning numbers using HTTP client");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                ResponseEntity<List<Integer>> response = makeGetRequest(count, lowerBand, upperBand, requestEntity);
                Set<Integer> sixDistinctNumbers = getSixRandomDistinctNumbers(response);

                if (sixDistinctNumbers.size() == MAXIMAL_WINNING_NUMBERS) {
                    return SixRandomNumbersDto.builder()
                            .numbers(sixDistinctNumbers)
                            .build();
                }

                log.warn("Received less than {} numbers, retrying… (attempt {}/{})",
                        MAXIMAL_WINNING_NUMBERS, attempts + 1, MAX_RETRIES);
                attempts++;

            } catch (ResourceAccessException e) {
                log.error("Error while fetching winning numbers: {}", e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        log.error("Failed to fetch {} distinct winning numbers after {} attempts", MAXIMAL_WINNING_NUMBERS, MAX_RETRIES);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int count, int lowerBand, int upperBand, HttpEntity<Void> requestEntity) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(uri)
                    .port(port)
                    .path("/api/v1.0/random")
                    .queryParam("min", lowerBand)
                    .queryParam("max", upperBand)
                    .queryParam("count", count)
                    .toUriString();

            ResponseEntity<List<Integer>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Integer>>() {}
            );

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return response;
            }

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null || response.getBody().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return response;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ResponseStatusException(e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Set<Integer> getSixRandomDistinctNumbers(ResponseEntity<List<Integer>> response) {
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        List<Integer> numbers = response.getBody();
        if (numbers == null) {
            log.error("Response body was null");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return numbers.stream()
                .distinct()
                .limit(MAXIMAL_WINNING_NUMBERS)
                .collect(Collectors.toSet());
    }
}
