package com.fitness.exercises.hateoas;


import com.fitness.exercises.controller.ExerciseController;
import com.fitness.exercises.dto.ExerciseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ExerciseHateoas implements RepresentationModelAssembler<ExerciseDTO, EntityModel<ExerciseDTO>> {

    @Override
    public EntityModel<ExerciseDTO> toModel(ExerciseDTO exercise)
    {
        EntityModel<ExerciseDTO> exerciseModel =EntityModel.of(exercise,
                linkTo(methodOn(ExerciseController.class).getExerciseById(exercise.get_id())).withSelfRel(),
                linkTo(methodOn(ExerciseController.class).getAllExercises()).withRel("parent")
        );
        return exerciseModel;
    }
}
