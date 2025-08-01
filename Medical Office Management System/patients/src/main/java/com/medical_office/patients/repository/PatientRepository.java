package com.medical_office.patients.repository;


import com.medical_office.patients.dto.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Collection<Patient> findByCnp(String cnp);
}
