package com.example.testtask.service;

import com.example.testtask.model.User;
import com.example.testtask.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByPrincipal(String principal) {
        return userRepository.findByPrincipal(principal)
                .orElseThrow(() -> new IllegalStateException("User with email " + principal + " not exist!"));
    }

}
