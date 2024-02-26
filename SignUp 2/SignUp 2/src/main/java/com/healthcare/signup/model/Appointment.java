package com.healthcare.signup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @EmbeddedId
    private AppointmentKey key;
    private LocalDateTime apptDateTime;
    private String apptDescription;
    @Enumerated(EnumType.STRING)
    private Status apptStatus;
}
