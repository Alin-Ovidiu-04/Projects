package com.medical_office.appointments.dto;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AppointmentId implements Serializable {

    private int id_pacient;
    private int id_doctor;
    private Date date;


    public int getId_pacient() {
        return id_pacient;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public Date getDate() {
        return date;
    }
}
