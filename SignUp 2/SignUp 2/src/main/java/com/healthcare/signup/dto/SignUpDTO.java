package com.healthcare.signup.dto;

import com.healthcare.signup.model.Gender;
import com.healthcare.signup.model.Role;
import com.healthcare.signup.model.Specialization;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SignUpDTO {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Specialization specialization;
}
