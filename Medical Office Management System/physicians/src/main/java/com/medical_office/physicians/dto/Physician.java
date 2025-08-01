package com.medical_office.physicians.dto;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "physician")
public class Physician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_doctor;
    private int id_user;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    public Physician() {
    }

    public Physician(int id_user, String firstName, String lastName, String email, String phone, Specialization specialization) {
        this.id_user = id_user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public int getId_user() {
        return id_user;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

}
