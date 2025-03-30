package com.study.webflux.grpc.service;

import com.google.protobuf.Empty;
import com.study.webflux.grpc.repository.UserRepository;
import com.study.webflux.grpc.repository.Users;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository repository;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<User> responseObserver) {
        var newUser = Users.fromRequest(request.getName(), request.getEmail());
        repository.save(newUser)
                  .doOnNext(user -> {
                      responseObserver.onNext(user.toProto());
                      responseObserver.onCompleted();
                  })
                  .doOnError(responseObserver::onError)
                  .subscribe();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        repository.findByName(request.getName())
                  .doOnNext(user -> { // responseObserver 의 onNext / onCompleted 를 doOnNext 에서 묶는다
                      responseObserver.onNext(user.toProto());
                      responseObserver.onCompleted();
                  })
                  .doOnError(responseObserver::onError)
                  .subscribe();
    }

    @Override
    public void listAllUsers(Empty request, StreamObserver<ListUsersResponse> responseObserver) {
        repository.findAll()
                  .map(Users::toProto)
                  .collectList()
                  .doOnNext(users -> {
                      responseObserver.onNext(ListUsersResponse.newBuilder()
                                                               .addAllUsers(users)
                                                               .build());
                      responseObserver.onCompleted();
                  })
                  .doOnError(responseObserver::onError)
                  .subscribe();
    }
}
