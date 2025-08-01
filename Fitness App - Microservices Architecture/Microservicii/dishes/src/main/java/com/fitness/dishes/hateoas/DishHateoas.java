package com.fitness.dishes.hateoas;

import com.fitness.dishes.controller.DishController;
import com.fitness.dishes.dto.DishDTO;
import org.bson.types.ObjectId;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class DishHateoas implements RepresentationModelAssembler<DishDTO, EntityModel<DishDTO>> {
    @Override
    public  EntityModel<DishDTO> toModel(DishDTO dish)
    {
        EntityModel<DishDTO> dishModel=EntityModel.of(dish,
                linkTo(methodOn(DishController.class).getDishById(dish.get_id())).withSelfRel(),
                linkTo(methodOn(DishController.class).getAll()).withRel("parent")
        );
        return dishModel;
    }
}
