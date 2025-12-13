package com.lukaszp12.lotto.e2e;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lotto.LottoSpringBootApplication;

import java.util.List;

import static io.restassured.RestAssured.given;


@Testcontainers
@SpringBootTest(
        classes = LottoSpringBootApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("e2e")
class LottoE2ETest {

    @LocalServerPort
    int port;

    @Container
    static MongoDBContainer mongo =
            new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getConnectionString);
    }

    @Test
    void shouldAnnounceWinnerWhenAllNumbersMatch() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(List.of(1,2,3,4,5,6))
                .when()
                .post("/lotto/play")
                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }
}
