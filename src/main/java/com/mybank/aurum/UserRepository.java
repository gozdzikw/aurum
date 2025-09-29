package com.mybank.aurum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //TODO have: save(User entity), findById(Long id), findAll(), deleteById(Long id), etc.

    Optional<User> findByFirstName(String firstName);

    List<User> findByLastNameContainingIgnoreCase(String lastNamePart);
}