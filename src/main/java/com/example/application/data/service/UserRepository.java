package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUsername(String username);

    User getByActivationCode(String activationCode);

    ArrayList<User> getAllByRole(Role role);

    User getByRegistrationNumber(String registrationNumber);
}
