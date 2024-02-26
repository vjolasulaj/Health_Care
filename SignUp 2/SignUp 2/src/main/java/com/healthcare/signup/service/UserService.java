package com.healthcare.signup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.healthcare.signup.model.User;
import com.healthcare.signup.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public User save(@NonNull User user) {
        return userRepository.save(user);
    }

    public boolean exists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
