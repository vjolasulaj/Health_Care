package com.healthcare.signup.specifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.healthcare.signup.model.Appointment;
import com.healthcare.signup.model.Doctor;
import com.healthcare.signup.model.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
public class AppointmentSpecification {

    public static Specification<Appointment> ofUser(User user) {
        return (root, query, builder) -> {
            switch (user.getRole()) {
                case ADMIN:
                    // krijon nje truthy predicate
                    return builder.conjunction();
                case DOCTOR: {
                    Path<Long> doctorId = root.get("key").get("doctor").get("id");
                    return builder.equal(doctorId, user.getId());
                }
                case PATIENT: {
                    Path<Long> patientId = root.get("key").get("patient").get("id");
                    return builder.equal(patientId, user.getId());
                }
                default:
                    return null;
            }
        };
    }

    public static Specification<Appointment> fromDate(LocalDate date) {
        return (root, query, builder) -> {
            Path<LocalDate> apptDate = root.get("apptDateTime");
            return builder.greaterThanOrEqualTo(apptDate, date);
        };
    }

    public static Specification<Appointment> toDate(LocalDate date) {
        return (root, query, builder) -> {
            Path<LocalDateTime> apptDate = root.get("apptDateTime");

            LocalDateTime endOfDayToDate = LocalDateTime.from(date.atTime(LocalTime.MAX));

            return builder.lessThanOrEqualTo(apptDate, endOfDayToDate);
        };
    }

}
