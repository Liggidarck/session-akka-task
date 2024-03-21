package com.example.testtask.repository;

import com.example.testtask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT e FROM User e WHERE e.email = :email")
    Optional<User> findByPrincipal(@Param(value = "email") String email);

}