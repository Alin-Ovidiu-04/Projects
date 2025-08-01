package com.medical_office.consultations.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Investigations {
    @Id
    @GeneratedValue
    private int _id;
    private String name;
    private int time;
    private String result;

    public Investigations(){

    }
    public Investigations(String name, int time, String result) {
        this.name = name;
        this.time = time;
        this.result = result;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public String getResult() {
        return result;
    }
}
