package com.healthcare.signup.repository;

import com.healthcare.signup.model.Appointment;
import com.healthcare.signup.model.Doctor;
import com.healthcare.signup.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    Appointment findByApptDateTimeAndKey_DoctorAndKey_Patient(
            LocalDateTime dateTime,
            Doctor doc,
            Patient pat);
}
