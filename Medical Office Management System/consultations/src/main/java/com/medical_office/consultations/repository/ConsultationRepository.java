package com.medical_office.consultations.repository;


import com.medical_office.consultations.dto.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    Collection<Consultation> findById(int id);

}
