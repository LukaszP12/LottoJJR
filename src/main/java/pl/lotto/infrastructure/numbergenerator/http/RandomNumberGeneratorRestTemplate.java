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
import pl.lotto.domain.numbergenerator.RandomNumberGenerable;
import pl.lotto.domain.numbergenerator.SixRandomNumbersDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Primary
@Component
public class RandomNumberGeneratorRestTemplate implements RandomNumberGenerable {

    public static final int MAXIMAL_WINNING_NUMBERS = 6;
    public static final String RANDOM_NUMBER_SERVICE_PATH = "/api/v1.0/random";

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

//    private final String baseUrl; // full URI from properties
//
//    public RandomNumberGeneratorRestTemplate(RestTemplate restTemplate,
//                                             @Value("${lotto.number-generator.http.client.config.uri}") String baseUrl) {
//        this.restTemplate = restTemplate;
//        this.baseUrl = baseUrl;
//    }

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        log.info("Started fetching winning numbers using http client");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        try {
            final ResponseEntity<List<Integer>> response = makeGetRequest(count, lowerBand, upperBand, requestEntity);
            Set<Integer> sixDistinctNumbers = getSixRandomDistinctNumbers(response);

            if (sixDistinctNumbers.size() != MAXIMAL_WINNING_NUMBERS) {
                log.error("Set is less than: {} Have to request one more time", count);
                return generateSixRandomNumbers(count, lowerBand, upperBand);
            }

            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctNumbers)
                    .build();
        } catch (ResourceAccessException e) {
            log.error("Error while fetching winning numbers using http client: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int count, int lowerBand, int upperBand, HttpEntity<HttpHeaders> requestEntity) {
        try {
            ResponseEntity<List<Integer>> response = restTemplate.exchange(
                    uri + "" + port + "/api/v1.0/random?min=" + lowerBand + "&max=" + upperBand + "&count=" + count,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Integer>>() {}
            );

            // Return 204 as is
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return response;
            }

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(response.getStatusCode());
            }

            // Treat empty body as 500 only if status is 2xx other than 204
            if (response.getBody() == null || response.getBody().isEmpty()) {
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
            log.error("Response Body was null");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return numbers.stream()
                .distinct()
                .limit(MAXIMAL_WINNING_NUMBERS)
                .collect(Collectors.toSet());
    }
}
