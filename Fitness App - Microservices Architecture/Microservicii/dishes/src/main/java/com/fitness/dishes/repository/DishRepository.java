package com.fitness.dishes.repository;

import com.fitness.dishes.dto.DishDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends MongoRepository<DishDTO,String> {
    @Query("{ _id: '?0' }")
    Optional<DishDTO> getById(int id);
    @Query("{ 'name': ?0 }")
    List<DishDTO> getByName(String name);

    @Query(value = "{ _id : '?0' }",delete = true)
    void deleteById(int id);

    @Query("{ difficulty: ?0 }")
    List<DishDTO> getByDifficulty(DishDTO.Difficulty difficulty);

    @Query("{ type : ?0 }")
    List<DishDTO> getByType(DishDTO.Type type);


}

