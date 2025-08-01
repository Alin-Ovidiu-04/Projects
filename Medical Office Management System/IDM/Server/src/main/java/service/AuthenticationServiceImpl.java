package service;
import com.medical_office.proto.*;
import com.medical_office.proto.AuthenticationServiceGrpc;
import dto.User;
import service.ServerService;
import io.grpc.Status;
import io.jsonwebtoken.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

import java.util.Base64;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;


public class AuthenticationServiceImpl extends AuthenticationServiceGrpc.AuthenticationServiceImplBase {
    //@Autowired
    ServerService serverService;

    //@Autowired
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public void authenticate(
            com.medical_office.proto.AuthenticationRequest  request,
            io.grpc.stub.StreamObserver<com.medical_office.proto.AuthenticationResponse> responseObserver) {

        Optional<User> data = serverService.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (data.isPresent()) {
            String jws = Jwts.builder()
                    .claim("id", data.get().getId_user())
                    .claim("username", data.get().getUserName())
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plusSeconds(3600 * 2)))
                    .signWith(
                            SignatureAlgorithm.HS256, Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
                    )
                    .compact();

            Claims claims = validateToken(jws);

            if (claims != null) {
                AuthenticationResponse result = AuthenticationResponse.newBuilder().setToken(jws).build();
                responseObserver.onNext(result);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token validation failed").asRuntimeException());
            }

        } else {
            responseObserver.onError(
                    Status.UNAUTHENTICATED
                            .withDescription("Autentificare esuata")
                            .augmentDescription("Invalid user credentials")
                            .asRuntimeException()
            );
        }
    }

    private Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    @Override
    public void validate(
            com.medical_office.proto.ValidateRequest request,
            io.grpc.stub.StreamObserver<com.medical_office.proto.ValidateResponse> responseObserver) {
        String tokenToValidate = request.getValid();
        Claims claims = validateToken(tokenToValidate);

        if (claims != null) {
            ValidateResponse result = ValidateResponse.newBuilder().setValid("Token is valid").build();
            responseObserver.onNext(result);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Invalid token").asRuntimeException());
        }
    }

}
