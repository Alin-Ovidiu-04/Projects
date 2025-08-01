package com.medical_ofiice.gateway.workflow;

import com.medical_ofiice.gateway.cors.CORSFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpMethod;

@RestController
public class WorkflowController {

    //completari
    private RestTemplate restTemplate;
    private CORSFilter corsFilter;

    //ulterior
    private String medicalOfficeService = "http://localhost:8082";


    @Autowired
    public WorkflowController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    //functie validate - ulterior
    public UserInformation validate(String s){
        // Logica de validare a tokenului și extragerea informațiilor despre utilizator (roluri)
        UserInformation userInformation = new UserInformation();
        userInformation.setRole("admin"); // Setarea unui rol de exemplu pentru scopul demonstrativ
        return userInformation;
    }
    //functie register
    // cu crossOrigin merge, dar cred ca trebuie sa il fac sa mearga din CORSFilter....
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO){

        String nume = userDTO.getNume();
        String prenume = userDTO.getPrenume();
        String email = userDTO.getEmail();
        String parola = userDTO.getParola();

        System.out.println("Gateway-SignIn");
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    // functie login
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/authenticate")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO){

        String email = userDTO.getEmail();
        String parola = userDTO.getParola();
        System.out.println("Gateway-LogIn");
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/api/medical_office/consultations")
    public ResponseEntity<Object> foo(HttpServletRequest request) throws IOException {

        String token = request.getHeader("Authorization");

        if(Objects.equals(request.getMethod(),"GET")){
            System.out.println("Gateway - GET - " + medicalOfficeService + request.getRequestURI() +"  ");
            return restTemplate.getForEntity(medicalOfficeService + request.getRequestURI() , Object.class);
        }
        else {
            String role;
            try{
                role = validate(token.split(" ")[1]).getRole();
            } catch(Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("AccessnForbiden");

            }

            if("moderator".equals(role)) {
                switch (request.getMethod()){
                    case "PUT":
                    case "POST":
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        String requestData = request.getReader().lines().collect(Collectors.joining());
                        HttpEntity<Object> body = new HttpEntity<>(requestData, headers);
                        String url = medicalOfficeService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : "");
                        //.resolve e pentru SpringFramework -> facem cu getValuesOf si Values pentru Spring Boot
                        //return restTemplate.exchange(url, Objects.requireNonNull(HttpMethod.resolve(request.getMethod())), body, Object.class);
                        return null;
                }
                if("DELETE".equals(request.getMethod())){
                    try{
                        restTemplate.delete(medicalOfficeService + request.getRequestURI() + (request.getQueryString() != null ? ("?" + request.getQueryString()) : ""));
                        return ResponseEntity.ok().build();
                    } catch (Exception e) {
                        return ResponseEntity.notFound().build();
                    }
                    // ......................................
                }
            }
        }

        //ulterior
        return null;
    }

    public void authentificate(){}


    public void addOrder(){}

    public void getOrder(){}



}
