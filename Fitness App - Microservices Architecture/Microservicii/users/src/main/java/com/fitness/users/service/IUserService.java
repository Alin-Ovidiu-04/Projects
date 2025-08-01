package com.fitness.users.service;

import com.fitness.users.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserDTO> findAll();

    @Transactional
    void deleteById(Integer id_user) throws ChangeSetPersister.NotFoundException;

    Optional<UserDTO> findByEmail(String email);

    List<UserDTO> findByName(String firstName);

    Optional<UserDTO> findById(Integer id_user);

    UserDTO save(UserDTO user) throws Exception;

    List<UserDTO> getAllUsers();

    List<UserDTO> findBy_activity(String activity_level);

    UserDTO updateUser(Integer id_user, UserDTO updatedUserDTO) throws ChangeSetPersister.NotFoundException;
}
