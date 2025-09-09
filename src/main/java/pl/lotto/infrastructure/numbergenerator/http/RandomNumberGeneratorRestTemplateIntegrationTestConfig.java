package pl.lotto.infrastructure.numbergenerator.http;

import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbergenerator.RandomNumberGenerable;

public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {

    public RandomNumberGenerable remoteNumberGeneratorClient() {
        RestTemplate restTemplate = restTemplate(
                1000, 1000, restTemplateResponseErrorHandler()
        );
        return remoteNumberGeneratorClient();
    }

}
