package com.fitness.fitnessplan.repository;

import com.fitness.fitnessplan.dto.PlanDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends MongoRepository<PlanDTO,String> {
    @Query("{ _id: '?0' }")
    Optional<PlanDTO> getById(ObjectId id);
    @Query("{ 'id_user': ?0 }")
    List<PlanDTO> getByIdUser(Long id_user);

    @Query(value = "{ _id : '?0' }",delete = true)
    void deleteById(ObjectId id);

    @Query("{ difficulty: ?0 }")
    List<PlanDTO> getByDifficulty(PlanDTO.Difficulty difficulty);

}
