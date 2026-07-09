package com.govtech.profile.infrastructure.persistence.entity;

import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.BankAccount;
import com.govtech.profile.domain.valueobject.Household;
import com.govtech.profile.domain.valueobject.Identity;
import com.govtech.profile.domain.valueobject.TaxInformation;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

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

  @Embedded
  private Identity identity;

  @Embedded
  private Address address;

  @Embedded
  private HouseholdEmbeddable household;

  @Embedded
  private TaxInformation taxInformation;

  @Embedded
  private BankAccount bankAccount;
}
