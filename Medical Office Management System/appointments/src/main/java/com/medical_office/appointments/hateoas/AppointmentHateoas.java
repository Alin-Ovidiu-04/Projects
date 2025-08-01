package com.medical_office.appointments.hateoas;

import com.medical_office.appointments.controller.AppointmentController;
import com.medical_office.appointments.dto.Appointment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppointmentHateoas implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {

    @Override
    public EntityModel<Appointment> toModel(Appointment appointment)
    {
        EntityModel<Appointment> physicianModel = EntityModel.of(appointment,
                WebMvcLinkBuilder.linkTo(methodOn(AppointmentController.class).getAppointmentById(appointment.getId_doctor())).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getAllAppointments()).withRel("parent")
        );

        return physicianModel;
    }
}
