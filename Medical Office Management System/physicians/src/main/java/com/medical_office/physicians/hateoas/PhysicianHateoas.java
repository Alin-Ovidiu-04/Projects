package com.medical_office.physicians.hateoas;

import com.medical_office.physicians.controller.PhysicianController;
import com.medical_office.physicians.dto.Physician;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PhysicianHateoas implements RepresentationModelAssembler<Physician, EntityModel<Physician>> {

    @Override
    public EntityModel<Physician> toModel(Physician physician)
    {
        EntityModel<Physician> physicianModel = EntityModel.of(physician,
                WebMvcLinkBuilder.linkTo(methodOn(PhysicianController.class).getPhysicianById(physician.getId_doctor())).withSelfRel(),
                linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withRel("parent")
        );

        return physicianModel;
    }
}
