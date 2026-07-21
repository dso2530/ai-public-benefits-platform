package com.govtech.application.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import com.govtech.application.domain.model.PackageStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_packages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationPackageJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID applicationId;

    @Column(nullable = false)
    private String objectKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageStatus status;

    @Column(nullable = false)
    private Instant generatedAt;
}