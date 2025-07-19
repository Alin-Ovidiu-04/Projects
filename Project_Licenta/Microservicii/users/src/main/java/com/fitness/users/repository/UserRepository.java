package com.fitness.users.repository;

import com.fitness.users.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDTO, String> {
    @Query("DELETE FROM UserDTO u WHERE u.id_user = :id_user")
    void deleteById(Integer id_user);
    Optional<UserDTO> findByEmail(String email);

    @Query("SELECT u FROM UserDTO u WHERE u.first_name = :first_name")
    List<UserDTO> findByFirst_name(String first_name);
    @Query("SELECT u FROM UserDTO u WHERE u.id_user = :id_user")
    Optional<UserDTO> findById(@Param("id_user")Integer id_user);
    @Query("SELECT u FROM UserDTO u WHERE u.activity_level = :activity_level")
    List<UserDTO> findBy_activity(@Param("activity_level")String activity_level);

}
