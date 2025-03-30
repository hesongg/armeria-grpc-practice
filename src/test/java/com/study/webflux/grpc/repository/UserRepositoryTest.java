package com.study.webflux.grpc.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void deleteJohn() {
        userRepository.deleteByName("John").block();
    }

    @Test
    void save_test() {
        //given
        String name = "John";
        String email = "john@naver.com";
        Users user = new Users(null,
                               name,
                               email,
                               Instant.now());

        //when & then
        userRepository.save(user)
                      .flatMap(saved -> userRepository.findByName(saved.getName()))
                      .as(StepVerifier::create)
                      .assertNext(found -> {
                          log.debug("found user : {}", found);
                          assertThat(found.getId()).isGreaterThan(0);
                          assertThat(found.getName()).isEqualTo(name);
                          assertThat(found.getEmail()).isEqualTo(email);
                      })
                      .verifyComplete();
    }
}