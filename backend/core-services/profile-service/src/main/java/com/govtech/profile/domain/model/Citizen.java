package com.govtech.profile.domain.model;

import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.BankAccount;
import com.govtech.profile.domain.valueobject.Household;
import com.govtech.profile.domain.valueobject.Identity;
import com.govtech.profile.domain.valueobject.Mergeable;
import com.govtech.profile.domain.valueobject.TaxInformation;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class Citizen {

  private UUID id;
  private String subject;
  private String email;
  private Identity identity;
  private Address address;
  private Household household;
  private TaxInformation taxInformation;
  private BankAccount bankAccount;

  public Citizen(
      UUID id,
      String subject,
      String email,
      Identity identity,
      Address address,
      Household household) {
    this.id = id;
    this.subject = subject;
    this.email = email;
    this.identity = identity;
    this.address = address;
    this.household = household;
  }

  public Citizen(
      UUID id,
      String subject,
      String email,
      Identity identity,
      Address address,
      Household household,
      TaxInformation taxInformation,
      BankAccount bankAccount) {

    this.id = id;
    this.subject = subject;
    this.email = email;
    this.identity = identity;
    this.address = address;
    this.household = household;
    this.taxInformation = taxInformation;
    this.bankAccount = bankAccount;

  }

  public void update(UpdateProfileCommand command) {

    identity = merge(identity, command.identity());
    address = merge(address, command.address());
    household = merge(household, command.household());
    taxInformation = merge(taxInformation, command.taxInformation());
    bankAccount = merge(bankAccount, command.bankAccount());
  }

  private <T extends Mergeable<T>> T merge(T current, T incoming) {

    if (incoming == null) {
      return current;
    }

    return current == null ? incoming : current.merge(incoming);
  }

  public String getFullName() {
    return identity.firstName() + " " + identity.lastName();
  }
}
