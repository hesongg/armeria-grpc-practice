package com.study.webflux.grpc.service;

import com.study.webflux.grpc.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository repository;

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        repository.findByName(request.getName())
                  .doOnNext(user -> {
                      responseObserver.onNext(user.toProto());
                      responseObserver.onCompleted();
                  })
                  .doOnError(responseObserver::onError)
                  .subscribe();
    }
}
