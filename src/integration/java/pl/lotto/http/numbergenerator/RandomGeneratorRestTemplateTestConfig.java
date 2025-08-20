package pl.lotto.http.numbergenerator;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numbergenerator.RandomNumberGenerable;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplate;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;

@Configuration
public class RandomGeneratorRestTemplateTestConfig {

    private final RandomNumberGeneratorRestTemplateConfigurationProperties properties;

    public RandomGeneratorRestTemplateTestConfig(
            RandomNumberGeneratorRestTemplateConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RandomNumberGenerable remoteNumberGeneratorClient(RestTemplateBuilder builder) {
        // use the configuration properties directly
        return new RandomNumberGeneratorRestTemplate(builder.build(), properties.uri());
    }
}
