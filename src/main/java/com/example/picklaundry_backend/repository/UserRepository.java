package com.example.picklaundry_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.picklaundry_backend.entity.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

            List<User> findAll();

    Optional<User> findByEmail(String email);
     Optional<User> findByFullName(String fullName);

    Optional<User> findByMobile(String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}
