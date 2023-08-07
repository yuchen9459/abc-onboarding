package com.abc.onboarding.customer.entity;

import com.abc.onboarding.account.entity.Account;
import com.abc.onboarding.payload.request.RegistrationRequest;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    @OneToMany
    private Set<Account> accounts;
    @OneToOne(cascade = CascadeType.ALL)
    private CustomerDetails customerDetails;

    public static Customer of(RegistrationRequest request) {
        Customer customer = new Customer();
        customer.setCustomerDetails(CustomerDetails.of(request));
        customer.setUsername(request.getUsername());
        return customer;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        if (this.accounts == null) {
            accounts = new HashSet<>();
        }
        accounts.add(account);
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }
}
