package com.medical_office.appointments.dto;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.util.Date;


@Entity
public class Appointment {
    @EmbeddedId
    private AppointmentId id;

    private Status status;
    public Appointment() {
    }

    public Appointment(AppointmentId id, Status status) {
        this.id = id;
        this.status = status;
    }

    public int getId_doctor() {
        return id.getId_doctor();
    }

    public Status getStatus() {
        return status;
    }

    private enum Status{
        Honored(),
        Unpresented(),
        Canceled()
    };
}
