package com.govtech.eligibility.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "eligibilities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID calculationId;

    @Column(nullable = false)
    private Instant calculatedAt;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String aidCode;

    @Column(nullable = false)
    private String aidName;

    @Column(nullable = false)
    private String status;

    private String reason;

    private String estimatedAmount;
}