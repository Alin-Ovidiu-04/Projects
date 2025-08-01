package com.medical_office.consultations.hateoas;

import com.medical_office.consultations.controller.ConsultationController;
import com.medical_office.consultations.dto.Consultation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConsultationHateoas implements RepresentationModelAssembler<Consultation, EntityModel<Consultation>> {

    @Override
    public EntityModel<Consultation> toModel(Consultation consultation)
    {
        EntityModel<Consultation> consultationModel = EntityModel.of(consultation,
                WebMvcLinkBuilder.linkTo(methodOn(ConsultationController.class).getConsultationById(consultation.get_id())).withSelfRel(),
                linkTo(methodOn(ConsultationController.class).getAllConsultations()).withRel("parent")
        );

        return consultationModel;
    }
}
