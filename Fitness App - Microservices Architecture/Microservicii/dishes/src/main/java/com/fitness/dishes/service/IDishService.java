package com.fitness.dishes.service;

import com.fitness.dishes.dto.DishDTO;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

public interface IDishService {

    List<DishDTO> getAll();
    Optional<DishDTO> getById(int _id);

    List<DishDTO> getByName(String name);

    DishDTO createDish(DishDTO dish);

    DishDTO updateDish(int _id, DishDTO updatedDishDTO) throws ChangeSetPersister.NotFoundException;

    void deleteById(int _id) throws ChangeSetPersister.NotFoundException;

    List<DishDTO> getByDifficulty(DishDTO.Difficulty difficulty);
    List<DishDTO> getByType(DishDTO.Type type);

}
