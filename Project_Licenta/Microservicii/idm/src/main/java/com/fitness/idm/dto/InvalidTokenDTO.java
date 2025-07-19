package com.fitness.idm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidTokenDTO {
    private int id;
    private String token;
    private String information;

    public InvalidTokenDTO(String token, String information) {
        this.token = token;
        this.information = information;
    }
}
