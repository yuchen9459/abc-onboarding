package com.abc.onboarding.customer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long addressId;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String state;

    public Address() {
    }

    private Address(String country, String city, String zipCode, String street, String state) {
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.state = state;
    }

    public static Address of(String country, String city, String zipCode, String street, String state) {
        return new Address(country, city, zipCode, street, state);
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
