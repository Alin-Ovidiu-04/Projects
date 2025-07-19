package com.fitness.gateway.workflow;

public class UserDTO {
    private Long id_user;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private int height;
    private int weight;
    private int age;
    private String activity_level;

    public Long getId_user() {
        return id_user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }
}
