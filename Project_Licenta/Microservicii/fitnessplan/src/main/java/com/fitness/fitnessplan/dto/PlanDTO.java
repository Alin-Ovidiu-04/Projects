package com.fitness.fitnessplan.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "fitnessplan")
public class PlanDTO {

    @Id
    public String _id;
    public Long id_user;
    public Map<Integer, List<Integer>> exercises;  // Map pentru a reprezenta exercițiile pe zile
    public Map<Integer, Map<String, Integer>> dishes;  // Map pentru dish-urile pentru fiecare zi
    public int training_time;
    public Difficulty plan_difficulty;

    public enum Difficulty {
        easy,
        medium,
        hard
    }

    public PlanDTO(String _id, Long id_user, Map<Integer, List<Integer>> exercises, Map<Integer, Map<String, Integer>> dishes, int training_time, Difficulty plan_difficulty) {
        this._id = _id;
        this.id_user = id_user;
        this.exercises = exercises;
        this.dishes = dishes;
        this.training_time = training_time;
        this.plan_difficulty = plan_difficulty;
    }
}

