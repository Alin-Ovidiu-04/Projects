package com.medical_office.patients.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Patient {

    @Id
    private String cnp;
    private int id_user;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private LocalDate birth_date;
    private Boolean is_active;

    public Patient(){
        this.is_active = true;
    }
    public Patient( String cnp, int idUser, String firstName, String lastName, String email, String phone, LocalDate birthDate) {
        this.cnp = cnp;
        this.id_user = idUser;
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.phone = phone;
        this.birth_date = birthDate;
    }

    public String getId() {
        return cnp;
    }

    public int getId_user() { return id_user; }

    public String getFirst_name() { return first_name; }

    public String getLast_name() { return last_name; }

    public String getCnp() {
        return cnp;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public Boolean getIs_active() {
        return is_active;
    }
}
