package pl.lotto.infrastructure.numbergenerator.http;


import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "lotto.number-generator.http.client.config")
@Builder
public record RandomNumberGeneratorRestTemplateConfigurationProperties(
        String uri,
        long connectionTimeout,
        long readTimeout
) {
}
