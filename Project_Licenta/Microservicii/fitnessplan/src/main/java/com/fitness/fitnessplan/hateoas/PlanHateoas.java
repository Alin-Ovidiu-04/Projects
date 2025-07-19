package com.fitness.fitnessplan.hateoas;

import com.fitness.fitnessplan.controller.PlanController;
import com.fitness.fitnessplan.dto.PlanDTO;
import org.bson.types.ObjectId;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PlanHateoas implements RepresentationModelAssembler<PlanDTO, EntityModel<PlanDTO>> {
    @Override
    public  EntityModel<PlanDTO> toModel(PlanDTO plan)
    {
        EntityModel<PlanDTO> planModel=EntityModel.of(plan,
                linkTo(methodOn(PlanController.class).getPlanById(new ObjectId(plan.get_id()))).withSelfRel(),
                linkTo(methodOn(PlanController.class).getAll()).withRel("parent")
        );
        return planModel;
    }
}
