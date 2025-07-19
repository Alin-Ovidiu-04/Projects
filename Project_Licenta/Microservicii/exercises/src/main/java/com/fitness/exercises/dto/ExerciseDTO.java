package com.fitness.exercises.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "Exercises")
public class ExerciseDTO {

    @Id
    private int _id;
    private String name;
    private String target_muscle;
    private double calories_burned_male; // calorii arse per minut
    private double calories_burned_female; // calorii arse per minut

    public ExerciseDTO(int _id, String name, String target_muscle, double calories_burned_male, double calories_burned_female) {
        this._id = _id;
        this.name = name;
        this.target_muscle = target_muscle;
        this.calories_burned_male = calories_burned_male;
        this.calories_burned_female = calories_burned_female;
    }
}
