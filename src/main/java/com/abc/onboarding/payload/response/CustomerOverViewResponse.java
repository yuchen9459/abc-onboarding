package com.abc.onboarding.payload.response;

import com.abc.onboarding.customer.entity.Customer;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerOverViewResponse {

    private final String username;
    private final Set<AccountResponse> accounts;

    public CustomerOverViewResponse(String username, Set<AccountResponse> accounts) {
        this.username = username;
        this.accounts = accounts;
    }

    public static CustomerOverViewResponse of(Customer customer) {
        Set<AccountResponse> accountResponses = customer.getAccounts().stream()
                                                        .map(AccountResponse::of)
                                                        .collect(Collectors.toSet());
        return new CustomerOverViewResponse(customer.getUsername(), accountResponses);
    }

    public String getUsername() {
        return username;
    }

    public Set<AccountResponse> getAccounts() {
        return accounts;
    }
}
