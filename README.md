### armeria, grpc, webflux pratice

<br>

#### Armeria
Armeria 란?
- LINE 에서 만든 프레임워크
- 고성능 비동기 마이크로서비스를 쉽게 만들 수 있게 해준다.

<br>

#### gRPC
gRPC 란?
- gRPC Remote Procedure Calls
- Google 이 만든 고성능 원격 프로시저 호출 프레임워크
- Protocol Buffers (`.proto`) 기반
  - 빠르고, 가볍고, 언어중립적이다.
- HTTP/2 기반
- 양방향 스트리밍 지원

<br>

Stub 이란?
- proto 파일 기반으로 자동 생성된 코드
- 클라이언트와 서버가 gRPC 통신을 할 수 있도록 함

proto 파일을 기반으로 세 가지 stub 을 자동 생성한다.
- `BlockingStub`: 단순한 호출, 동기 처리
- `FutureStub`: 조금 더 유연한 비동기 처리
- `Stub (Async Stub)`: WebFlux, 반응형, 스트리밍
- 클라이언트가 이 중 원하는 방식을 선택해서 사용
- 서버는 그냥 ImplBase를 상속하고, 요청을 받는 쪽이다.



