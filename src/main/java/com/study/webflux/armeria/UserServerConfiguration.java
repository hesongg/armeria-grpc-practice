package com.study.webflux.armeria;

import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.study.webflux.grpc.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServerConfiguration {
    // A user can configure the server by providing an ArmeriaServerConfigurator bean.
    @Bean
    public ArmeriaServerConfigurator userServerConfigurator(UserService userService) {
        return builder -> {
            // Add DocService that enables you to send Thrift and gRPC requests
            // from web browser.
            builder.serviceUnder("/docs", new DocService());

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), false);

            //bind grpc user service
            builder.service(GrpcService.builder()
                                       .addService(userService)
                                       .enableUnframedRequests(true)
                                       .build());
        };
    }
}
