package com.healthcare.signup.manager;

import com.healthcare.signup.model.Patient;
import com.healthcare.signup.repository.PatientRepository;
import com.healthcare.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;

    public Patient findById(Long id){
        var u = userRepository.findById(id).orElseThrow();
        var p = patientRepository.findById(id).orElseThrow();
        return Patient.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .password(u.getPassword())
                .dateOfBirth(u.getDateOfBirth())
                .gender(u.getGender())
                .role(u.getRole()).build();
    }
}
