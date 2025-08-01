package com.medical_office.appointments.repository;


import com.medical_office.appointments.dto.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;


public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Collection<Appointment> findByStatus(Status status);

    public enum Status{
        Onorata(),
        Neprezentata(),
        Anulata()
    };
}
