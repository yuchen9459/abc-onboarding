package com.abc.onboarding.payload.request;

import com.abc.onboarding.customer.entity.Address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AddressDTO {

    @NotBlank(message = "The country is required.")
    private String country;

    @NotBlank(message = "The city is required.")
    private String city;

    @NotBlank(message = "The Zip code is required.")
    @Pattern(regexp = "^\\d{1,5}$",
            flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE},
            message = "The Zip code is invalid.")
    private String zipCode;

    @NotBlank(message = "The street name is required.")
    private String street;
    private String state;

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getState() {
        return state;
    }

    public Address toAddress() {
        return Address.of(country, city, zipCode, street, state);
    }
}
