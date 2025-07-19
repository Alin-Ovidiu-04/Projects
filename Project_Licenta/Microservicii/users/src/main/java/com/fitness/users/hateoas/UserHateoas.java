package com.fitness.users.hateoas;

import com.fitness.users.controller.UserController;
import com.fitness.users.dto.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserHateoas implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO user)
    {
        EntityModel<UserDTO> userModel=EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserByEmail(user.getEmail())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("parent")
        );
        return userModel;
    }
}
