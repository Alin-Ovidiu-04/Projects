package com.fitness.users.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="users")
@Getter
@Setter
public class UserDTO {

    @Id
    @Basic
    @Column(name="id_user")
    private Long id_user;//id-ul

    @Basic
    @Column(name="first_name")
    private String first_name;
    @Basic
    @Column(name="last_name")
    private String last_name;

    @Basic
    @Column(name="email")
    private String email;
    @Basic
    @Column(name="password")
    private String password;

    @Basic
    @Column(name="height")
    private int height;
    @Basic
    @Column(name="weight")
    private int weight;
    @Basic
    @Column(name="age")
    private int age;

    @Basic
    @Column(name="activity_level")
    private String activity_level;

    public UserDTO()
    {

    }
    public UserDTO(Long id_user, String first_name, String last_name, String email, String password, int height, int weight, int age, String activity_level) {
        this.id_user = id_user;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.activity_level = activity_level;
    }
}
