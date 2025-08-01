package com.medical_office.consultations.controller;

import com.medical_office.consultations.dto.Consultation;
import com.medical_office.consultations.hateoas.ConsultationHateoas;
import com.medical_office.consultations.repository.ConsultationRepository;
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
public class ConsultationController {

    @Autowired
    ConsultationRepository consultationRepository;

    @RequestMapping("/consultations")
    List<Consultation> consultations(){
        return this.consultationRepository.findAll();
    }

    @GetMapping("/consultations")
    public ResponseEntity<List<Consultation>> getAllConsultations(){
        List<Consultation> consultations = this.consultationRepository.findAll();
        return new ResponseEntity<>(consultations, HttpStatus.OK);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/consultations/{id}")
    public ResponseEntity<?> getConsultationById(@PathVariable Integer id){
        Optional<Consultation> consultation = this.consultationRepository.findById(id);

        if(!consultation.isEmpty())
        {
            return new ResponseEntity<>(new ConsultationHateoas().toModel(consultation.get()),HttpStatus.OK);
        }
        else {
            Map<String, ArrayList<Link>> links = new HashMap<>();
            ArrayList<Link> arrayLinks = new ArrayList<>();

            Link parentLink = linkTo(methodOn(ConsultationController.class).getAllConsultations()).withRel("parent");

            arrayLinks.add(parentLink);
            links.put("_links", new ArrayList<Link>(arrayLinks));

            return new ResponseEntity<>(links,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/consultations")
    public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation){
        System.out.println(consultation.toString());
        Consultation savedConsultation = this.consultationRepository.save(consultation);
        return new ResponseEntity<>(savedConsultation,HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteConsultation(@PathVariable Integer id)
    {
        this.consultationRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
