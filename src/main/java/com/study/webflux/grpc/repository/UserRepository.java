package com.study.webflux.grpc.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<Users, Long> {

    Mono<Users> findByName(String name);

    Mono<Void> deleteByName(String name);
}
