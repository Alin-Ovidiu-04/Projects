package com.fitness.idm.service;

import com.fitness.idm.dto.AuthDTO;
import com.fitness.idm.dto.InvalidTokenDTO;
import com.fitness.idm.dto.UserDTO;
import com.fitness.idm.proto.EmptyGrpcResponse;
import com.fitness.idm.proto.TokenServiceGrpc;
import com.fitness.idm.proto.TokenValidateRequest;
import com.fitness.idm.proto.TokenValidateResponse;
import com.fitness.idm.repository.InvalidTokenRepository;
import com.fitness.idm.repository.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl extends TokenServiceGrpc.TokenServiceImplBase {

    @Autowired
    public UserRepository userRepository = new UserRepository();
    @Autowired
    private final InvalidTokenRepository tokenInvalidRepository = new InvalidTokenRepository();

    @Autowired
    private final Token token = new Token();

    @Bean(name = "tokenPasswordEncoder")
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void validateToken(TokenValidateRequest request, StreamObserver<TokenValidateResponse> responseObserver) {
        try {
            String tokenValue = request.getToken().replace("Bearer ", "");

            // verific daca token ul este expirat
            if (tokenInvalidRepository.findByToken(tokenValue) != null) {
                responseObserver.onError(
                        Status.UNAUTHENTICATED
                                .withDescription("Invalid or expired token")
                                .asRuntimeException()
                );
                return;
            }

            // daca nu e expirat aflu inf despre user
            AuthDTO userAuth = this.token.decodeToken(tokenValue);


            TokenValidateResponse response = TokenValidateResponse.newBuilder()
                    .setId(userAuth.getId())
                    .setRole(userAuth.getRole())
                    .build();


            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {

            responseObserver.onError(
                    Status.UNKNOWN
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }


    @Override
    public void logout(TokenValidateRequest request, StreamObserver<EmptyGrpcResponse> responseObserver) {
        try {
            String tokenValue = request.getToken().replace("Bearer ", "");

            this.token.decodeToken(tokenValue);
            System.out.println("Trying to log out -- id :" + this.token.decodeToken(tokenValue).getId());

            //salvez token codificat in baza de date
            InvalidTokenDTO invalidTokenDTO = new InvalidTokenDTO();
            invalidTokenDTO.setToken(tokenValue);
            invalidTokenDTO.setInformation("invalid");
            tokenInvalidRepository.save(invalidTokenDTO);

            EmptyGrpcResponse response = EmptyGrpcResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.ABORTED
                            .withDescription("Logout cu un token invalid")
                            .asRuntimeException()
            );
        }
    }


    @Override
    public void login(com.fitness.idm.proto.CredentialsRequest request, io.grpc.stub.StreamObserver<com.fitness.idm.proto.TokenValidateRequest> responseObserver) {
        UserDTO userDto = this.userRepository.getUserByUsername(request.getUsername());

        System.out.println("Trying to log in -- email :" + request.getUsername());

        try {
            if (userDto != null && this.passwordEncoder().matches(request.getPassword(), userDto.getPassword())) {
                String token = "Bearer " + this.token.createToken(userDto.getId(), userDto.getRole());
                TokenValidateRequest response = TokenValidateRequest.newBuilder()
                        .setToken(token)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(
                        Status.ABORTED
                                .withDescription("Invalid data")
                                .asRuntimeException()
                );
            }
        } catch (Exception e) {
            responseObserver.onError(
                    Status.UNKNOWN
                            .withDescription("Unknown error occurred")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}
