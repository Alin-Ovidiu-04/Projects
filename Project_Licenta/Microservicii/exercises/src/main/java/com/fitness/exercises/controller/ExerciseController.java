package com.fitness.exercises.controller;

import com.fitness.exercises.dto.ExerciseDTO;
import com.fitness.exercises.hateoas.ExerciseHateoas;
import com.fitness.exercises.service.IExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/fitness/exercises")
public class ExerciseController {

    private final IExerciseService exerciseService;
    @Autowired
    public ExerciseController(IExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    private Map<String, List<Link>> generateNotFoundResponse() {
        Map<String, List<Link>> links = new HashMap<>();
        List<Link> arrayList = new ArrayList<>();
        Link parentLink = linkTo(methodOn(ExerciseController.class).getAllExercises()).withRel("parent");
        arrayList.add(parentLink);
        links.put("_links", new ArrayList<>(arrayList));
        return links;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<ExerciseDTO>>> getAllExercises() {
        try {
            List<ExerciseDTO> exercises = exerciseService.findAll();

            List<EntityModel<ExerciseDTO>> exerciseEntities = new ArrayList<>();

            for (ExerciseDTO calisthenic : exercises) {
                exerciseEntities.add(new ExerciseHateoas().toModel(calisthenic));
            }

            return new ResponseEntity<>(exerciseEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @GetMapping(params = "name")
    public ResponseEntity<?> getExerciseByName(@RequestParam String name) {
        try {
            List<ExerciseDTO> exercises = exerciseService.findByName(name);
            List<EntityModel<ExerciseDTO>> exerciseEntities = new ArrayList<>();

            for (ExerciseDTO exercise : exercises) {
                exerciseEntities.add(new ExerciseHateoas().toModel(exercise));
            }

            return exerciseEntities.isEmpty()
                    ? new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(exerciseEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "difficulty")
    public ResponseEntity<?> getExercisesByDifficulty(@RequestParam String difficulty) {
        try {
            List<ExerciseDTO> exercises = exerciseService.findBy_difficulty(difficulty);
            List<EntityModel<ExerciseDTO>> exerciseEntities = new ArrayList<>();

            for (ExerciseDTO exercise : exercises) {
                exerciseEntities.add(new ExerciseHateoas().toModel(exercise));
            }

            return exerciseEntities.isEmpty()
                    ? new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(exerciseEntities, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @GetMapping(params = "_id")
    public ResponseEntity<?> getExerciseById(@RequestParam int _id) {
        try {
            Optional<ExerciseDTO> exercise = exerciseService.findById(_id);
            if (!exercise.isEmpty()) {
                return new ResponseEntity<>(new ExerciseHateoas().toModel(exercise.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(generateNotFoundResponse(), HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        try {
            ExerciseDTO savedExercise = exerciseService.save(exerciseDTO);
            EntityModel<ExerciseDTO> exerciseEntity = new ExerciseHateoas().toModel(savedExercise);

            return new ResponseEntity<>(exerciseEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }


    @GetMapping("/calisthenics")
    public ResponseEntity<?> getExercises() {
        try {
            List<ExerciseDTO> exercisesList = exerciseService.getAllExercises();
            return new ResponseEntity<>(exercisesList, HttpStatus.OK);
        } catch (HttpClientErrorException.BadRequest badRequestException) {
            return new ResponseEntity<>("{\"error\": \"" + badRequestException.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{_id}")
    public ResponseEntity<?> updateExercise(
            @PathVariable int _id,
            @RequestBody ExerciseDTO updatedExerciseDTO
    ) {
        try {
            exerciseService.updateExercise(_id, updatedExerciseDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @DeleteMapping("/{_id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable int _id) {

        try {
            exerciseService.deleteById(_id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
