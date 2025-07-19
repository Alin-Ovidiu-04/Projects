package com.fitness.dishes.service;

import com.fitness.dishes.dto.DishDTO;
import com.fitness.dishes.repository.DishRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishServiceImpl implements IDishService {


    private final DishRepository dishRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public List<DishDTO> getAll() {
        return dishRepository.findAll();
    }

    @Override
    public Optional<DishDTO> getById(int _id) {
        return dishRepository.getById(_id);
    }

    @Override
    public List<DishDTO> getByName(String name) {
        return dishRepository.getByName(name);
    }

    @Override
    public DishDTO createDish(DishDTO dish) {
        return dishRepository.save(dish);
    }

    @Override
    public DishDTO updateDish(int _id, DishDTO updatedDishDTO) throws ChangeSetPersister.NotFoundException {
        Optional<DishDTO> existingDish = dishRepository.getById(_id);

        if (existingDish.isPresent()) {
            DishDTO updatedDish = existingDish.get();
            updatedDish.setName(updatedDishDTO.getName());
            updatedDish.setPreparation_time(updatedDishDTO.getPreparation_time());
            updatedDish.setCooking_difficulty(updatedDishDTO.getCooking_difficulty());
            updatedDish.setMeal_type(updatedDishDTO.getMeal_type());
            updatedDish.setServes(updatedDishDTO.getServes());
            updatedDish.setNutrition_per_serving(updatedDishDTO.getNutrition_per_serving());
            updatedDish.setRecipe(updatedDishDTO.getRecipe());

            dishRepository.save(updatedDish);
            return updatedDish;
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Override
    public void deleteById(int _id) throws ChangeSetPersister.NotFoundException {
        Optional<DishDTO> dish = dishRepository.getById(_id);
        if (dish.isPresent()) {
            dishRepository.deleteById(_id);
        } else {
            throw  new ChangeSetPersister.NotFoundException();
        }
    }


    @Override
    public List<DishDTO> getByDifficulty(DishDTO.Difficulty difficulty) {
        return dishRepository.getByDifficulty(difficulty);
    }

    @Override
    public List<DishDTO> getByType(DishDTO.Type type) {
        return dishRepository.getByType(type);
    }

}
