package com.healthcare.signup.service;

import com.healthcare.signup.model.Appointment;
import com.healthcare.signup.model.Status;
import com.healthcare.signup.model.User;
import com.healthcare.signup.repository.AppointmentRepository;
import com.healthcare.signup.specifications.AppointmentSpecification;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepo;

    public Appointment bookAppointment(Appointment appointment) {
        Appointment myAppointment = appointmentRepo
                .findByApptDateTimeAndKey_DoctorAndKey_Patient(
                        appointment.getApptDateTime(), appointment.getKey().getDoctor(),
                        appointment.getKey().getPatient());
        if (myAppointment == null) {
            return appointmentRepo.save(appointment);
        } else {
            throw new IllegalStateException("Appointment with already present");
        }
    }

    public void updateStatus(Long id, Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status should not be null");
        }
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No appointment could be found with this id."));
        Status currentStatus = appointment.getApptStatus();
        if (!isValidStatusUpdate(currentStatus, status)) {
            throw new IllegalArgumentException("Status update not allowed.");
        }

        appointment.setApptStatus(status);
        appointmentRepo.save(appointment);
    }

    public void cancelAppointment(Long id) {
        appointmentRepo.deleteById(id);
    }

    private boolean isValidStatusUpdate(Status currentStatus, Status updatedStatus) {
        return getAllowedStatusSet(currentStatus).contains(updatedStatus);
    }

    private Set<Status> getAllowedStatusSet(Status currentStatus) {
        return switch (currentStatus) {
            case ACCEPTED -> Set.of(Status.CANCELED);
            case WAITING_APPROVAL -> Set.of(Status.ACCEPTED, Status.DECLINED);
            default -> new HashSet<>();
        };
    }

    public Appointment getAppointment(Long id) throws NotFoundException {
        return appointmentRepo.findById(id).orElseThrow();
    }

    public List<Appointment> getAppointments(
            User currentUser,
            LocalDate fromDate,
            LocalDate toDate,
            Integer _page,
            Integer _pageSize) {

        Specification<Appointment> apptFilter = Specification.where(
                AppointmentSpecification.ofUser(currentUser))
                .and(toDate == null ? null : AppointmentSpecification.toDate(toDate))
                .and(fromDate == null ? null : AppointmentSpecification.fromDate(fromDate));
        int page = _page == null ? 0 : _page;
        int pageSize = _pageSize == null ? 10 : _pageSize;

        Pageable pageable = PageRequest.of(page, pageSize);

        return appointmentRepo.findAll(apptFilter, pageable).toList();
    }

}
