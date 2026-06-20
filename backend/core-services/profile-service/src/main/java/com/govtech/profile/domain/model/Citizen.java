package com.govtech.profile.domain.model;


import com.govtech.profile.domain.valueobject.Address;
import com.govtech.profile.domain.valueobject.Household;

import java.util.UUID;

public class Citizen {

    private UUID id;

    private String subject;

    private String email;

    private String firstName;

    private String lastName;

    private Address address;

    private Household household;

    public Citizen(
            UUID id,
            String subject,
            String email,
            String firstName,
            String lastName,
            Address address,
            Household household) {

        this.id = id;
        this.subject = subject;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.household = household;
    }

    public UUID getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }

    public Household getHousehold() {
        return household;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}