package com.fitness.users.service;

import com.fitness.users.dto.UserDTO;
import com.fitness.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  IUserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(Integer id_user) throws ChangeSetPersister.NotFoundException {
        Optional<UserDTO> user = userRepository.findById(id_user);
        if (user.isPresent()) {
            userRepository.deleteById(id_user);
        } else {
            throw  new ChangeSetPersister.NotFoundException();
        }

    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserDTO> findById(Integer id_user) {
        return userRepository.findById(id_user);
    }

    @Override
    public UserDTO save(UserDTO user) throws Exception {
        try {

            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new Exception("Eroare: " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> findByName(String firstName) {
        return userRepository.findByFirst_name(firstName);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> findBy_activity(String activity_level) {
        return userRepository.findBy_activity(activity_level);
    }

    @Override
    public UserDTO updateUser(Integer id_user, UserDTO updatedUserDTO) throws ChangeSetPersister.NotFoundException {
        Optional<UserDTO> existingUser = findById(id_user);

        if (existingUser.isPresent()) {
            UserDTO updatedUser= existingUser.get();
            updatedUser.setFirst_name(updatedUserDTO.getFirst_name());
            updatedUser.setLast_name(updatedUserDTO.getLast_name());
            updatedUser.setEmail(updatedUserDTO.getEmail());
            updatedUser.setPassword(updatedUserDTO.getPassword());
            updatedUser.setHeight(updatedUserDTO.getHeight());
            updatedUser.setWeight(updatedUserDTO.getWeight());
            updatedUser.setAge(updatedUserDTO.getAge());
            updatedUser.setActivity_level(updatedUserDTO.getActivity_level());

            userRepository.save(updatedUser);
            return updatedUser;
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }
}
