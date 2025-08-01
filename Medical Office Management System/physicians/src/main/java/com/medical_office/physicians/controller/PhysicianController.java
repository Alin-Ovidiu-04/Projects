package com.medical_office.physicians.controller;

import com.medical_office.physicians.dto.Physician;
import com.medical_office.physicians.hateoas.PhysicianHateoas;
import com.medical_office.physicians.repository.PhysicianRepository;
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
public class PhysicianController {

    @Autowired
    PhysicianRepository physicianRepository;

    @RequestMapping("/physicians")
    List<Physician> physicians(){
        return this.physicianRepository.findAll();
    }

    @GetMapping("/physicians")
    public ResponseEntity<List<Physician>> getAllPhysicians(){
        List<Physician> physicians = this.physicianRepository.findAll();
        return new ResponseEntity<>(physicians, HttpStatus.OK);
    }

    @GetMapping("/physicians/{id}")
    public ResponseEntity<?> getPhysicianById(@PathVariable Integer id){
        Optional<Physician> physician = this.physicianRepository.findById(id);

        if(!physician.isEmpty())
        {
            return new ResponseEntity<>(new PhysicianHateoas().toModel(physician.get()),HttpStatus.OK);
        }
        else {
            Map<String, ArrayList<Link>> links = new HashMap<>();
            ArrayList<Link> arrayLinks = new ArrayList<>();

            Link parentLink = linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withRel("parent");

            arrayLinks.add(parentLink);
            links.put("_links", new ArrayList<Link>(arrayLinks));

            return new ResponseEntity<>(links,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/physicians")
    public ResponseEntity<Physician> createPhysician(@RequestBody Physician physician){
        System.out.println(physician.toString());
        Physician savedPhysician = this.physicianRepository.save(physician);
        return new ResponseEntity<>(savedPhysician,HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePhysician(@PathVariable Integer id)
    {
        this.physicianRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
