package com.fitness.exercises.repository;

import com.fitness.exercises.dto.ExerciseDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends MongoRepository<ExerciseDTO, String> {

    @Query("{ 'name': ?0 }")
    List<ExerciseDTO> findByName(String name);
    @Query("{ _id: '?0' }")
    Optional<ExerciseDTO> findById(int id);
    @Query("{ target_muscle: ?0 }")
    List<ExerciseDTO> findByTargetMuscle(String difficulty);

    @Query(value = "{ _id : '?0' }",delete = true)
    void deleteById(int id);

}