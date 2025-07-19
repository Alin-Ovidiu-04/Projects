package com.fitness.dishes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "dishes")
public class DishDTO {

    @Id
    private int _id;
    private String name;
    private Type meal_type;
    private Difficulty  cooking_difficulty;
    private String preparation_time;
    private int serves;
    private Nutrition nutrition_per_serving;
    private Recipe recipe;

    public DishDTO(int _id, String name, Type meal_type, Difficulty cooking_difficulty, String preparation_time, int serves, Nutrition nutrition_per_serving, Recipe recipe) {
        this._id = _id;
        this.name = name;
        this.meal_type = meal_type;
        this.cooking_difficulty = cooking_difficulty;
        this.preparation_time = preparation_time;
        this.serves = serves;
        this.nutrition_per_serving = nutrition_per_serving;
        this.recipe = recipe;
    }

    public enum Difficulty {
        easy,
        medium,
        hard
    }

    public enum Type {
        breakfast,
        lunch,
        dinner
    }

}
