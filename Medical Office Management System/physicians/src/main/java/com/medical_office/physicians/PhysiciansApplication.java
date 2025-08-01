package com.medical_office.physicians;


import com.medical_office.physicians.dto.Physician;
import com.medical_office.physicians.repository.PhysicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PhysiciansApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhysiciansApplication.class, args);
	}

}

