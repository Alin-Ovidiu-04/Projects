package com.medical_office.physicians.repository;


import com.medical_office.physicians.dto.Physician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface PhysicianRepository extends JpaRepository<Physician, Integer> {

    Collection<Physician> findByEmail(String email);
}
