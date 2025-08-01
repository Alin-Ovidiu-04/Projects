package com.fitness.fitnessplan.dto;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Exercises {
    public String name;
    public int repetitions;
}
