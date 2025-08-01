package com.medical_office.patients.controller;

import com.medical_office.patients.dto.Patient;
import com.medical_office.patients.hateoas.PatientHateoas;
import com.medical_office.patients.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/medical_office")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @RequestMapping("/patients")
    List<Patient> patients(){
        return this.patientRepository.findAll();
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = this.patientRepository.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Integer id){
        Optional<Patient> patient = this.patientRepository.findById(id);

        if(!patient.isEmpty())
        {
            return new ResponseEntity<>(new PatientHateoas().toModel(patient.get()),HttpStatus.OK);
        }
        else {
            Map<String, ArrayList<Link>> links = new HashMap<>();
            ArrayList<Link> arrayLinks = new ArrayList<>();

            Link parentLink = linkTo(methodOn(PatientController.class).getAllPatients()).withRel("parent");

            arrayLinks.add(parentLink);
            links.put("_links", new ArrayList<Link>(arrayLinks));

            return new ResponseEntity<>(links,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient){
        System.out.println(patient.toString());
        Patient savedPatient = this.patientRepository.save(patient);
        return new ResponseEntity<>(savedPatient,HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> DeletePatient(@PathVariable Integer id)
    {
        this.patientRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
