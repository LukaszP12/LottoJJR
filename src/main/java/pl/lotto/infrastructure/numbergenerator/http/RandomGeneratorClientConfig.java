package pl.lotto.infrastructure.numbergenerator.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbergenerator.RandomNumberGenerable;

import java.time.Duration;

@Configuration
public class RandomGeneratorClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(
            @Value("${lotto.number-generator.http.client.config.connectionTimeout:1000}") long connectionTimeout,
            @Value("${lotto.number-generator.http.client.config.readTimeout:1000}") long readTimeout,
            RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {

        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    public RandomNumberGenerable remoteNumberGeneratorClient(
            RestTemplateBuilder restTemplateBuilder,
            RandomNumberGeneratorRestTemplateConfigurationProperties properties) {
        return new RandomNumberGeneratorRestTemplate(restTemplateBuilder.build(), properties.uri());
    }
}
