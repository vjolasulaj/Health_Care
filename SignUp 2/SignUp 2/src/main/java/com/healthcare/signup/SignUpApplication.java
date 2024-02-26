package com.healthcare.signup;

import com.healthcare.signup.model.*;
import com.healthcare.signup.repository.AppointmentRepository;
import com.healthcare.signup.repository.DoctorRepository;
import com.healthcare.signup.repository.PatientRepository;
import com.healthcare.signup.repository.UserRepository;
import com.healthcare.signup.service.AppointmentService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SignUpApplication {

        public static void main(String[] args) {
                SpringApplication.run(SignUpApplication.class, args);
        }

        @Bean
        CommandLineRunner run(UserRepository userRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository,
                        AppointmentRepository appointmentRepository,
                        PasswordEncoder encoder) {
                return args -> {

                        var now = new Date();

                        var user = new User(1L, "admin", "admin",
                                        now, "admin@admin.com", encoder.encode("12345"), Gender.FEMALE, Role.ADMIN);
                        userRepository.save(user);

                        var enumValues = Arrays.asList(Specialization.values());

                        Integer[] integerArray = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

                        var docs = Stream.of(integerArray).map(number -> {
                                var specialization = enumValues.get(number % 5);

                                var doc = Doctor.builder()
                                                .email("d" + number + "@d.com").role(Role.DOCTOR)
                                                .firstName("dFirstName" + number.toString())
                                                .lastName("dLastName" + number.toString())
                                                .dateOfBirth(now).gender(Gender.FEMALE)
                                                .password(encoder.encode("pass"))
                                                .specialization(specialization).build();

                                return doc;
                        }).toList();

                        var addedDocs = doctorRepository.saveAll(docs);

                        var pats = Stream.of(integerArray).map(number -> {
                                var pat = Patient.builder()
                                                .email("p" + number + "p@d.com").role(Role.PATIENT)
                                                .firstName("dFirstName" + number.toString())
                                                .lastName("dLastName" + number.toString())
                                                .dateOfBirth(now).gender(Gender.FEMALE)
                                                .password(encoder.encode("pass" + number.toString()))
                                                .build();

                                return pat;
                        }).toList();

                        var addedPats = patientRepository.saveAll(pats);

                        var appts = Stream.of(integerArray).map(number -> {
                                var doc = addedDocs.get(number % 4);
                                var pat = addedPats.get(number % 3);
                                LocalDateTime date = LocalDateTime.of(2024, Month.FEBRUARY, number, 9, 0, 0);
                                String desc = new String("test " + number);
                                var key = AppointmentKey.builder().doctor(doc).patient(pat).build();
                                var appt = Appointment.builder()
                                                .key(key)
                                                .apptStatus(Status.WAITING_APPROVAL)
                                                .apptDateTime(date).apptDescription(desc)
                                                .build();
                                return appt;
                        }).toList();

                        appointmentRepository.saveAll(appts);

                };
        }
}
