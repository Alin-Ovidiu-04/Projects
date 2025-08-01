package com.fitness.dishes.dto;
import java.util.List;

import lombok.Data;

@Data
public class Recipe {
    private List<String> ingredients;
    private List<String> additional_preparations;
    private List<String> method;
}
