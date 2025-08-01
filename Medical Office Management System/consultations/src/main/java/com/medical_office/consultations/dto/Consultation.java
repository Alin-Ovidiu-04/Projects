package com.medical_office.consultations.dto;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Consultation {
    @Id
    @GeneratedValue
    private int _id;
    private int id_pacient;
    private int id_doctor;
    private Date date;
    private Diagnostic diagnostic;
    @OneToMany(cascade = CascadeType.ALL)
    List<Investigations> investigations;
    public Consultation() {
    }

    public Consultation(int _id, int id_pacient, int id_doctor, Date date, Diagnostic diagnostic, List<Investigations> investigations) {
        this._id = _id;
        this.id_pacient = id_pacient;
        this.id_doctor = id_doctor;
        this.date = date;
        this.diagnostic = diagnostic;
        this.investigations = investigations;
    }

    public int get_id() {
        return _id;
    }

    public int getId_pacient() {
        return id_pacient;
    }

    public int getId_doctor() {
        return id_doctor;
    }

    public Date getDate() {
        return date;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public List<Investigations> getInvestigations() {
        return investigations;
    }

    private enum Diagnostic{
        Sanatos(),
        Bolnav()
    };
}
