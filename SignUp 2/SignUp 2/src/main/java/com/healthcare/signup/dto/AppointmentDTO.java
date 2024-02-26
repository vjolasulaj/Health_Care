package com.healthcare.signup.dto;

import com.healthcare.signup.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentDTO {
    private Long doctorId;
    private Long patientId;
    private LocalDateTime apptDateTime;
    private String description;
    private Status status;
}
