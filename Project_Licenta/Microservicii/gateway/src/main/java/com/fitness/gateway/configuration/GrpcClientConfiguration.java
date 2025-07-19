package com.fitness.gateway.configuration;

import com.fitness.gateway.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfiguration {

    @Value("${grpc.idm.host}")
    private String grpcIdmHost;

    @Value("${grpc.idm.port}")
    private int grpcIdmPort;

    @Bean
    public TokenServiceGrpc.TokenServiceStub tokenServiceStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcIdmHost, grpcIdmPort)
                .usePlaintext()
                .build();
        return TokenServiceGrpc.newStub(channel);
    }

    @Bean
    public UserServiceGrpc.UserServiceStub userServiceStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcIdmHost, grpcIdmPort)
                .usePlaintext()
                .build();
        return UserServiceGrpc.newStub(channel);
    }
}
