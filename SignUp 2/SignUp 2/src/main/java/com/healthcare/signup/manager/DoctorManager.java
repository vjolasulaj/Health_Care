package com.healthcare.signup.manager;

import com.healthcare.signup.model.Doctor;
import com.healthcare.signup.model.Patient;
import com.healthcare.signup.repository.DoctorRepository;
import com.healthcare.signup.repository.PatientRepository;
import com.healthcare.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoctorManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor findById(Long id) {
        var user = userRepository.findById(id).orElseThrow();
        var doctor = doctorRepository.findById(id).orElseThrow();
        return Doctor.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .password(u.getPassword())
                .dateOfBirth(u.getDateOfBirth())
                .gender(u.getGender())
                .role(u.getRole())
                .specialization(d.getSpecialization()).build();
    }

}
