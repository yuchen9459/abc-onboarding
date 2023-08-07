package com.abc.onboarding.customer.entity;

import com.abc.onboarding.payload.request.RegistrationRequest;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "customer_details")
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;

    private String name;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;
    private String documentId;
    private LocalDate birthDate;

    public static CustomerDetails of(RegistrationRequest request) {
        CustomerDetails customerDetails = new CustomerDetails();

        customerDetails.setName(request.getName());
        customerDetails.setAddress(request.getAddress().toAddress());
        customerDetails.setBirthDate(request.getBirthDate());
        customerDetails.setDocumentId(request.getDocumentId());
        return customerDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
