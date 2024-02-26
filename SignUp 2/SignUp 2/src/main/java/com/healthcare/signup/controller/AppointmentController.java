package com.healthcare.signup.controller;

import com.healthcare.signup.dto.AppointmentDTO;
import com.healthcare.signup.manager.DoctorManager;
import com.healthcare.signup.manager.PatientManager;
import com.healthcare.signup.model.Appointment;
import com.healthcare.signup.model.AppointmentKey;
import com.healthcare.signup.model.Status;
import com.healthcare.signup.model.User;
import com.healthcare.signup.service.AppointmentService;
import com.healthcare.signup.utils.Response;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    DoctorManager doctorManager;

    @Autowired
    PatientManager patientManager;

    @PostMapping()
    public Response<Appointment> bookAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try {
            var p = patientManager.findById(appointmentDTO.getPatientId());
            var d = doctorManager.findById(appointmentDTO.getDoctorId());
            var key = AppointmentKey.builder()
                    .doctor(d).patient(p).build();
            var appoint = Appointment.builder()
                    .key(key)
                    .apptDateTime(appointmentDTO.getApptDateTime())
                    .apptDescription(appointmentDTO.getDescription())
                    .apptStatus(Status.WAITING_APPROVAL).build();
            var a = appointmentService.bookAppointment(appoint);
            return new Response<>(a, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Response<>(null, List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    Response<String> updateStatus(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        try {
            Status appointmentStatus = appointmentDTO.getStatus();
            appointmentService.updateStatus(id, appointmentStatus);

            return new Response<String>("Status updated successfully.", null, HttpStatus.OK);
        } catch (IllegalAccessError e) {
            return new Response<>(null, List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, List.of(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new Response<>(null, List.of("Something went wrong."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    Response<List<Appointment>> getAppointments(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            var appts = appointmentService.getAppointments(currentUser, fromDate, toDate, page, size);
            return new Response<List<Appointment>>(appts, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Response<>(null, List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
