package com.study.webflux.grpc.repository;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

@Disabled
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 클래스가 한번만 인스턴스화 됨. (테스트 메소드 상태 공유)
@SpringBootTest
class UserRepositoryRollbackTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionFactory connectionFactory;

    private TransactionalOperator transactionalOperator;

    @BeforeAll
    void setup() {
        // 테스트 내에서 트랜잭션 수동 구성
        TransactionalOperator operator = TransactionalOperator.create(
                new R2dbcTransactionManager(connectionFactory)
        );
        this.transactionalOperator = operator;
    }

    @DisplayName("저장 후 트랜잭션 내부 롤백")
    @Test
    void rollback_test() {
        Users user = new Users(null,
                               "Sam",
                               "sam1@naver.com",
                               Instant.now());

        Mono<Void> tx = userRepository.save(user)
                                      .doOnNext(u -> log.debug("저장됨: {}", u))
                                      // 조건 없이 롤백
                                      .flatMap(saved -> Mono.error(new RuntimeException("강제 롤백")))
                                      .as(transactionalOperator::transactional)
                                      .then();

        StepVerifier.create(tx)
                    .expectErrorMatches(e -> e instanceof RuntimeException &&
                            e.getMessage().equals("강제 롤백"))
                    .verify();

        // 확인: 실제 DB에는 아무것도 없어야 함
        StepVerifier.create(userRepository.findAll())
                    .expectNextCount(0)
                    .verifyComplete();
    }
}