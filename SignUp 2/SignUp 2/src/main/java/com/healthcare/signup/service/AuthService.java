package com.healthcare.signup.service;

import com.healthcare.signup.dto.LogInDto;
import com.healthcare.signup.dto.LoginResponse;
import com.healthcare.signup.dto.SignUpDTO;
import com.healthcare.signup.model.Doctor;
import com.healthcare.signup.model.Patient;
import com.healthcare.signup.model.Role;
import com.healthcare.signup.model.User;
import com.healthcare.signup.repository.DoctorRepository;
import com.healthcare.signup.repository.PatientRepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    public LoginResponse login(LogInDto logInDto) {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(logInDto.getEmail(),
                logInDto.getPassword());
        authenticationManager.authenticate(login);
        User user = userService.getUserByEmail(logInDto.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder().token(jwtToken).build();

    }

    public User signup(SignUpDTO signUpDTO) throws Exception {
        var exists = userService.exists(signUpDTO.getEmail());
        if (exists) {
            throw new Exception("User already exists");
        }

        if (!isValidSignupRole(signUpDTO.getRole())) {
            throw new RestClientException("Invalid role");
        }

        if (signUpDTO.getRole() == Role.DOCTOR) {
            if (signUpDTO.getSpecialization() == null) {
                throw new RestClientException("Specialization is required");
            }

            try {
                var doctor = Doctor.builder()
                        .firstName(signUpDTO.getFirstName())
                        .lastName(signUpDTO.getLastName())
                        .email(signUpDTO.getEmail())
                        .dateOfBirth(signUpDTO.getDateOfBirth())
                        .password(encoder.encode(signUpDTO.getPassword()))
                        .gender(signUpDTO.getGender())
                        .role(Role.DOCTOR)
                        .specialization(signUpDTO.getSpecialization())
                        .build();
                return doctorRepository.save(doctor);
            } catch (Exception e) {
                var message = e.getMessage();
                if (message.toLowerCase().contains("duplicate entry")) {
                    throw new Exception("Doctor with this email or ID already exists");
                } else {
                    throw new Exception(message);
                }
            }
        }

        try {
            var patient = Patient.builder()
                    .firstName(signUpDTO.getFirstName())
                    .lastName(signUpDTO.getLastName())
                    .email(signUpDTO.getEmail())
                    .dateOfBirth(signUpDTO.getDateOfBirth())
                    .password(encoder.encode(signUpDTO.getPassword()))
                    .gender(signUpDTO.getGender())
                    .role(Role.PATIENT)
                    .build();
            return patientRepository.save(patient);
        } catch (Exception e) {
            var message = e.getMessage();
            if (message.toLowerCase().contains("duplicate entry")) {
                throw new Exception("Patient with this email or ID already exists");
            } else {
                throw new Exception(message);
            }
        }

    }

    private boolean isValidSignupRole(Role role) {
        return role != Role.ADMIN && Set.of(Role.DOCTOR, Role.PATIENT).contains(role);
    }

//Abstract factor//
}
