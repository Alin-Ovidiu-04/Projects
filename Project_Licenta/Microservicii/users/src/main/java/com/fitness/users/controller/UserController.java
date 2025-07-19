package com.fitness.users.controller;

import com.fitness.users.dto.UserDTO;
import com.fitness.users.hateoas.UserHateoas;
import com.fitness.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fitness/users")
public class UserController {

    private final IUserService userService;
    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    private Map<String, List<Link>> generateNotFoundResponse() {
        Map<String, List<Link>> links = new HashMap<>();
        List<Link> arrayList = new ArrayList<>();
        Link parentLink = linkTo(methodOn(UserController.class).getAllUsers()).withRel("parent");
        arrayList.add(parentLink);
        links.put("_links", new ArrayList<>(arrayList));
        return links;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<EntityModel<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.findAll();

            List<EntityModel<UserDTO>> userEntities = new ArrayList<>();

            for (UserDTO user : users) {
                userEntities.add(new UserHateoas().toModel(user));
            }

            return new ResponseEntity<>(userEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @GetMapping(params = "first_name")
    public ResponseEntity<?> getUserByName(@RequestParam String firstName) {
        try {
            List<UserDTO> users = userService.findByName(firstName);
            List<EntityModel<UserDTO>> userEntities = new ArrayList<>();

            for (UserDTO user : users) {
                userEntities.add(new UserHateoas().toModel(user));
            }

            return userEntities.isEmpty()
                    ? new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(userEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "activity_level")
    public ResponseEntity<?> getUsersByActivity(@RequestParam String activity_level) {
        try {
            List<UserDTO> users = userService.findBy_activity(activity_level);
            List<EntityModel<UserDTO>> userEntities = new ArrayList<>();

            for (UserDTO user : users) {
                userEntities.add(new UserHateoas().toModel(user));
            }

            return userEntities.isEmpty()
                    ? new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(userEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(params = "email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            Optional<UserDTO> user = userService.findByEmail(email);
            if (!user.isEmpty()) {
                return new ResponseEntity<>(new UserHateoas().toModel(user.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping(params = "id_user")
    public ResponseEntity<?> getUserById(@RequestParam Integer id_user) {
        try {
            Optional<UserDTO> user = userService.findById(id_user);
            if (!user.isEmpty()) {
                return new ResponseEntity<>(new UserHateoas().toModel(user.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.save(userDTO);
            EntityModel<UserDTO> userEntity = new UserHateoas().toModel(savedUser);

            return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }


    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            List<UserDTO> usersList = userService.getAllUsers();
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } catch (HttpClientErrorException.BadRequest badRequestException) {
            return new ResponseEntity<>("{\"error\": \"" + badRequestException.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id_user}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id_user,
            @RequestBody UserDTO updatedUserDTO
    ) {
        try {
            userService.updateUser(id_user, updatedUserDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id_user}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id_user) {

        try {
            userService.deleteById(id_user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id_user}")
    public ResponseEntity<String> getFitnessPlan(@PathVariable Long id_user) {
        String fitnessPlanServiceUrl = "http://localhost:8080/api/fitness/fitnessplan?id_user=";

        String planUrl = fitnessPlanServiceUrl +id_user;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(planUrl, String.class);
            String planCreateUrl = "http://localhost:8080/api/fitness/fitnessplan";

            String infoMessage = "Pentru a crea  un plan de fitness sau pentru a face update folositi endpoint-ul: " + planCreateUrl ;

            if (response.getStatusCode() == HttpStatus.OK) {
                infoMessage += "\nUtilizatorul are deja un plan de fitness stabilit.";
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                infoMessage += "\nUtilizatorul nu are un plan de fitness stabilit.";
            }

            return ResponseEntity.ok(infoMessage);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
