package com.fitness.gateway.workflow;

import com.fitness.gateway.cors.CORSFilter;
import com.fitness.gateway.proto.*;
import io.grpc.stub.StreamObserver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@RestController
public class WorkflowController {


    private RestTemplate restTemplate;
    private CORSFilter corsFilter;
    private final TokenServiceGrpc.TokenServiceStub tokenServiceStub;
    private final UserServiceGrpc.UserServiceStub userServiceStub;

    private String userService = "http://localhost:8080";
    private String fitnessAppService = "http://localhost:8082";
    private String dishService = "http://localhost:8083";
    private String exerciseService = "http://localhost:8084";

    @Autowired
    public WorkflowController( RestTemplate restTemplate, TokenServiceGrpc.TokenServiceStub tokenServiceStub, UserServiceGrpc.UserServiceStub userServiceStub) {
        this.restTemplate = restTemplate;
        this.tokenServiceStub = tokenServiceStub;
        this.userServiceStub = userServiceStub;
    }

    public UserInformation validate(String s){

        UserInformation userInformation = new UserInformation();
        userInformation.setRole("admin");
        return userInformation;
    }

    //sign up
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {

        try {

            System.out.println(" Gateway -- User sign up ");

            CountDownLatch latch = new CountDownLatch(1);

            // Construire cererea pentru serviciul UserService
            UserGrpc userGrpc = UserGrpc.newBuilder()
                    .setId(0)  // id este 0 pt. ca va fi generat automat de baza de date
                    .setRole("user")  // utilizatorul inregistrat este un utilizator obisnuit
                    .setUsername(userDTO.getEmail())
                    .setPassword(userDTO.getPassword())
                    .build();


            StreamObserver<UserGrpc> observer = new StreamObserver<UserGrpc>() {

                @Override
                public void onNext(UserGrpc userResponse) {

                    userDTO.setId_user(userResponse.getId());
                    restTemplate.postForEntity(
                            "http://localhost:8080/api/fitness/users", // URL-ul serviciului de utilizatori
                            userDTO, // Corpul cererii
                            String.class // Tipul răspunsului
                    );

                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    latch.countDown(); // inchidere latch în caz de eroare
                }

                @Override
                public void onCompleted() { latch.countDown(); } // inchide latch la finalizare
            };

            // se trimite cererea catre serviciul UserService
            userServiceStub.createUser(userGrpc, observer);

            try {
                latch.await(); //se asteapta pana cand latch-ul este inchis
            } catch (InterruptedException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            // intoarcere raspuns HTTP
            return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occurred: " + e.getMessage());
        }
    }


    // login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> logIn(@RequestBody UserDTO userDTO) {

        System.out.println(" Gateway -- User log in ");

        final String[] token = new String[1];
        CountDownLatch latch = new CountDownLatch(1);


        CredentialsRequest request = CredentialsRequest.newBuilder()
                .setUsername(userDTO.getEmail())
                .setPassword(userDTO.getPassword())
                .build();


        StreamObserver<TokenValidateRequest> observer = new StreamObserver<TokenValidateRequest>() {

            @Override
            public void onNext(TokenValidateRequest tokenValidateRequest) {
                token[0] = tokenValidateRequest.getToken();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                latch.countDown(); // inchidere latch în caz de eroare
            }

            @Override
            public void onCompleted() { latch.countDown(); } // inchide latch la finalizare
        };

        tokenServiceStub.login(request, observer);

        try {
            latch.await(); // se asteapta pana cand latch-ul este inchis
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        if (token[0] == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token[0]);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logOut(@RequestBody Map<String, String> request) {

        System.out.println(" Gateway -- User log out ");

        final boolean[] logoutSuccess = {false};
        CountDownLatch latch = new CountDownLatch(1);

        String token = request.get("token");

        // verificare suplimentara pentru a vedea daca token-ul ormit este null
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing");
        }

        StreamObserver<EmptyGrpcResponse> observer = new StreamObserver<EmptyGrpcResponse>() {

            @Override
            public void onNext(EmptyGrpcResponse emptyGrpcResponse) {
                logoutSuccess[0] = true; // daca primim un raspuns, logout-ul a fost realizat cu succes
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                latch.countDown(); // inchidere latch in caz de eroare
            }

            @Override
            public void onCompleted() {
                latch.countDown(); // inchidere latch la finalizare
            }
        };

        TokenValidateRequest logoutRequest = TokenValidateRequest.newBuilder()
                .setToken(token)
                .build();

        // se trmite un request catre IDM pentru a invalida token-ul
        tokenServiceStub.logout(logoutRequest, observer);

        try {
            latch.await(); // se asteapta pana cand latch-ul este inchis
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }

        if (logoutSuccess[0]) {
            return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @RequestMapping("/api/fitness/users")
    public ResponseEntity<Object> userData(HttpServletRequest request) throws IOException {

        System.out.println(" Gateway -- User data ");

        String token = request.getHeader("Authorization");

        // se verifica daca metoda este GET
        if(Objects.equals(request.getMethod(), "GET")){
            String url = userService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");
            return restTemplate.getForEntity(url, Object.class);
        }


        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method Not Allowed");
    }


    @RequestMapping("/api/fitness/plan")
    public ResponseEntity<Object> userPlan(HttpServletRequest request) throws IOException {

        System.out.println(" Gateway -- User plan ");
        Long id_user = 0L;

        if (request.getParameter("id_user") != null){
            id_user = Long.parseLong(request.getParameter("id_user"));
        }

        try {
            String url;
            // se construieste URL-ul pentru cererea GET catre FitnessPlan service
            if(id_user != 0L){
                url = "http://localhost:8082/api/fitness/plan?id_user=" + id_user;
            }
            else{
                url = "http://localhost:8082/api/fitness/plan";
            }

            // se face cererea folosind RestTemplate
            ResponseEntity<Object> response = restTemplate.exchange(
                    url, // URL-ul cererii
                    HttpMethod.GET, // Metoda HTTP (GET în acest caz)
                    null, // Corpul cererii (în cazul GET, este null)
                    Object.class // Tipul răspunsului așteptat
            );

            if(response != null)
                return response;
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);


        } catch (Exception e) {
            //in caz de alte excepții sau erori generale
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @RequestMapping("/api/fitnessplan/user")
    public ResponseEntity<Object> newPlan(HttpServletRequest request) throws IOException {

        System.out.println(" Gateway -- New user plan ");

        Long id_user = Long.parseLong(request.getParameter("id_user"));

        restTemplate.postForEntity(
                "http://localhost:8082/api/fitness/plan/" + id_user, // URL-ul serviciului de utilizatori
                id_user, // Corpul cererii
                String.class // Tipul răspunsului
        );

        return ResponseEntity.status(HttpStatus.OK).body("null");
    }

    @RequestMapping("/api/fitness/dishes")
    public ResponseEntity<Object> dishes(HttpServletRequest request,  @RequestBody(required = false) String body) throws IOException {

        System.out.println(" Gateway -- Dishes data ");

        String token = request.getHeader("Authorization");

        // se verifica daca metoda este GET
        if(Objects.equals(request.getMethod(), "GET")){
            String url = dishService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");
            return restTemplate.getForEntity(url, Object.class);
        }

        // se verifica daca metoda este POST
        if (Objects.equals(request.getMethod(), "POST")) {
            String url = dishService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method Not Allowed");
    }

    @RequestMapping("/api/fitness/exercises")
    public ResponseEntity<Object> exercises(HttpServletRequest request,  @RequestBody(required = false) String body) throws IOException {

        System.out.println(" Gateway -- Exercises data ");

        String token = request.getHeader("Authorization");

        // se verifica daca metoda este GET
        if(Objects.equals(request.getMethod(), "GET")){
            String url = exerciseService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");
            return restTemplate.getForEntity(url, Object.class);
        }

        // se verifica daca metoda este POST
        if (Objects.equals(request.getMethod(), "POST")) {
            String url = exerciseService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }


        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method Not Allowed");
    }

    @RequestMapping("/api/fitness/dishes/generate_recipe")
    public ResponseEntity<byte[]> recipe(HttpServletRequest request) throws IOException {

        System.out.println(" Gateway -- Recipe data ");

        String token = request.getHeader("Authorization");

        // se verifica daca metoda este GET
        if (Objects.equals(request.getMethod(), "GET")) {

            String recipeParam = request.getQueryString();
            System.out.println("Recipe parameter: " + recipeParam);

            String url = dishService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");

            // Se face apelul GET pentru a obtine raspunsul PDF
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

            // Se returneaza raspunsul primit
            return response;
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method Not Allowed".getBytes());
    }

}
