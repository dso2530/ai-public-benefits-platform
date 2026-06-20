package com.govtech.profile.infrastructure.persistence;

import com.govtech.profile.domain.valueobject.HousingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "citizens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitizenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String subject;

    @Column(nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    // Address

    private String street;

    private String postalCode;

    private String city;

    private String country;

    // Household

    @Enumerated(EnumType.STRING)
    private HousingStatus housingStatus;

    private Integer childrenCount;

    private Boolean singleParent;
}