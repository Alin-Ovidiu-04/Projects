package com.fitness.idm.service;

import com.fitness.idm.dto.UserDTO;
import com.fitness.idm.proto.UserGrpc;
import com.fitness.idm.proto.UserServiceGrpc;
import com.fitness.idm.repository.UserRepository;
import io.grpc.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {


    @Autowired
    private UserRepository userRepository=new UserRepository();

    @Autowired
    private Token token=new Token();

    @Bean(name = "userPasswordEncoder")
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void createUser(com.fitness.idm.proto.UserGrpc request, io.grpc.stub.StreamObserver<com.fitness.idm.proto.UserGrpc> responseObserver) {
        try {

            System.out.println("Trying to sign up -- email :" + request.getUsername());

            UserDTO userDto = new UserDTO(request.getId(), request.getRole(), request.getUsername(), request.getPassword());
            userDto.setId(null);

            userDto.setPassword(this.passwordEncoder().encode(userDto.getPassword()));
            UserDTO savedUser = userRepository.save(userDto);

            String token = this.token.createToken(savedUser.getId(), savedUser.getRole());
            savedUser.setPassword(null);
            UserGrpc userDtoGRPC = UserGrpc.newBuilder()
                    .setId(savedUser.getId())
                    .setRole(savedUser.getRole())
                    .setUsername(savedUser.getUsername())
                    .build();

            responseObserver.onNext(userDtoGRPC);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Exception during user creation: " + ex.getMessage());

            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(ex.getMessage())
                    .asRuntimeException());
        }
    }
}
