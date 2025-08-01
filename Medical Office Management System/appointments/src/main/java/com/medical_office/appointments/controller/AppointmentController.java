package com.medical_office.appointments.controller;

import com.medical_office.appointments.dto.Appointment;
import com.medical_office.appointments.hateoas.AppointmentHateoas;
import com.medical_office.appointments.repository.AppointmentRepository;
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
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @RequestMapping("/appointments")
    List<Appointment> appointments(){
        return this.appointmentRepository.findAll();
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        List<Appointment> appointments = this.appointmentRepository.findAll();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Integer id){
        Optional<Appointment> appointment = this.appointmentRepository.findById(id);

        if(!appointment.isEmpty())
        {
            return new ResponseEntity<>(new AppointmentHateoas().toModel(appointment.get()),HttpStatus.OK);
        }
        else {
            Map<String, ArrayList<Link>> links = new HashMap<>();
            ArrayList<Link> arrayLinks = new ArrayList<>();

            Link parentLink = linkTo(methodOn(AppointmentController.class).getAllAppointments()).withRel("parent");

            arrayLinks.add(parentLink);
            links.put("_links", new ArrayList<Link>(arrayLinks));

            return new ResponseEntity<>(links,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment){
        System.out.println(appointment.toString());
        Appointment savedAppointment = this.appointmentRepository.save(appointment);
        return new ResponseEntity<>(savedAppointment,HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id)
    {
        this.appointmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
