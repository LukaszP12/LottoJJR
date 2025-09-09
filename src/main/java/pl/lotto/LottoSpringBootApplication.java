package pl.lotto;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import pl.lotto.infrastructure.DummyRecords;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersGeneratorFacadeConfigurationProperties.class,
        RandomNumberGeneratorRestTemplateConfigurationProperties.class})
@EnableMongoRepositories
@EnableScheduling
public class LottoSpringBootApplication implements CommandLineRunner {

    @Autowired
    DummyRecords dummyRecords;

    public static void main(String[] args) {
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) {
        dummyRecords.save(new Ticket("dummy record"));
    }
}
