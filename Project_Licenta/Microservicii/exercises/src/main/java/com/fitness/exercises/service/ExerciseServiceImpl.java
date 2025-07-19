package com.fitness.exercises.service;

import com.fitness.exercises.dto.ExerciseDTO;
import com.fitness.exercises.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements IExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }


    @Override
    public List<ExerciseDTO> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public void deleteById(int _id) throws ChangeSetPersister.NotFoundException {
        Optional<ExerciseDTO> exercise = exerciseRepository.findById(_id);
        if (exercise.isPresent()) {
            exerciseRepository.deleteById(_id);
        } else {
            throw  new ChangeSetPersister.NotFoundException();
        }

    }

    @Override
    public Optional<ExerciseDTO> findById(int _id) { return exerciseRepository.findById(_id); }

    @Override
    public ExerciseDTO save(ExerciseDTO exercise) throws Exception {
        try {

            exerciseRepository.save(exercise);
            return exercise;
        } catch (Exception e) {
            throw new Exception("Eroare: " + e.getMessage());
        }
    }

    @Override
    public List<ExerciseDTO> findByName(String name) {
        return exerciseRepository.findByName(name);
    }

    @Override
    public List<ExerciseDTO> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<ExerciseDTO> findBy_difficulty(String difficulty) {
        return exerciseRepository.findByTargetMuscle(difficulty);
    }

    @Override
    public ExerciseDTO updateExercise(int _id, ExerciseDTO exerciseDTO) throws ChangeSetPersister.NotFoundException {
        Optional<ExerciseDTO> existingExercise = findById(_id);

        if (existingExercise.isPresent()) {
            ExerciseDTO updatedExercise= existingExercise.get();
            updatedExercise.setName(exerciseDTO.getName());
            updatedExercise.setTarget_muscle(exerciseDTO.getTarget_muscle());
            updatedExercise.setCalories_burned_male(exerciseDTO.getCalories_burned_male());
            updatedExercise.setCalories_burned_female(exerciseDTO.getCalories_burned_female());

            exerciseRepository.save(updatedExercise);
            return updatedExercise;
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }
}
