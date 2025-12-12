package com.lukaszp12.lotto.e2e;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class LottoE2ETest {

    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @BeforeAll
    static void setup(){
        mongo.start();
    }

}
