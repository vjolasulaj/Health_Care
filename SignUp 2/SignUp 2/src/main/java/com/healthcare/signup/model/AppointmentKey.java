package com.healthcare.signup.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentKey implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "doctorId", foreignKey = @ForeignKey(name = "fk_appt_doctor_id"))
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patientId", foreignKey = @ForeignKey(name = "fk_appt_patient_id"))
    private Patient patient;
}
