package com.medical_office.patients.hateoas;

import com.medical_office.patients.controller.PatientController;
import com.medical_office.patients.dto.Patient;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PatientHateoas implements RepresentationModelAssembler<Patient, EntityModel<Patient>> {

    @Override
    public EntityModel<Patient> toModel(Patient patient)
    {
        EntityModel<Patient> patientModel = EntityModel.of(patient,
                linkTo(methodOn(PatientController.class).getPatientById(parseInt(patient.getId()))).withSelfRel(),
                linkTo(methodOn(PatientController.class).getAllPatients()).withRel("parent")
        );

        return patientModel;
    }
}
