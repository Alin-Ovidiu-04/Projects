package com.fitness.exercises.service;

import com.fitness.exercises.dto.ExerciseDTO;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

public interface IExerciseService {

    List<ExerciseDTO> findAll();
    void deleteById(int _id) throws ChangeSetPersister.NotFoundException;

    List<ExerciseDTO> findByName(String name);

    Optional<ExerciseDTO> findById(int _id);

    ExerciseDTO save(ExerciseDTO exercise) throws Exception;

    List<ExerciseDTO> getAllExercises();

    List<ExerciseDTO> findBy_difficulty(String difficulty);

    ExerciseDTO updateExercise(int _id, ExerciseDTO exerciseDTO) throws ChangeSetPersister.NotFoundException;
}
