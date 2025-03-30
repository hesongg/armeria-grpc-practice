package com.study.webflux.grpc.repository;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ConnectionTest {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    void 커넥션_확인() {
        Mono.from(connectionFactory.create())
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }
}
