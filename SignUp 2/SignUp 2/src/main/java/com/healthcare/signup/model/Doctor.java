package com.healthcare.signup.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "userId")
@SuperBuilder
public class Doctor extends User {
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
}
