package com.abc.onboarding.payload.response;

public class CustomerRegistrationResponse {

    private final String username;
    private final String password;

    private CustomerRegistrationResponse(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static CustomerRegistrationResponse of(String username, String password) {
        return new CustomerRegistrationResponse(username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
