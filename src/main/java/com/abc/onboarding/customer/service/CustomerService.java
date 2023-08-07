package com.abc.onboarding.customer.service;

import com.abc.onboarding.account.entity.Account;
import com.abc.onboarding.account.entity.CurrencyType;
import com.abc.onboarding.account.service.AccountService;
import com.abc.onboarding.common.exception.BadRequestException;
import com.abc.onboarding.common.exception.ConflictedUsernameException;
import com.abc.onboarding.common.properties.OnboardingProperties;
import com.abc.onboarding.customer.entity.Customer;
import com.abc.onboarding.customer.repository.CustomerRepository;
import com.abc.onboarding.payload.request.RegistrationRequest;
import com.abc.onboarding.payload.response.CustomerRegistrationResponse;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.abc.onboarding.common.helper.CustomerHelper.generateTempPassword;
import static com.abc.onboarding.handlers.ErrorCodes.*;

@Service
public class CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final OnboardingProperties properties;

    public CustomerService(PasswordEncoder passwordEncoder,
                           CustomerRepository customerRepository,
                           AccountService accountService,
                           OnboardingProperties properties) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.accountService = accountService;
        this.properties = properties;
    }

    @Transactional
    public CustomerRegistrationResponse createCustomer(RegistrationRequest request) {
        validateCustomerInfo(request);

        Customer customer = Customer.of(request);
        String tempPassword = generateTempPassword();
        customer.setPassword(passwordEncoder.encode(tempPassword));

        // Create account for customer
        Account account = createAccount(request.getAddress().getCountry());
        customer.addAccount(account);

        try {
            customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictedUsernameException("The provided username has been used.", CONFLICTED_USERNAME);
        }
        return CustomerRegistrationResponse.of(customer.getUsername(), tempPassword);
    }

    public Customer findCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                                 .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username:" +
                                                                                          " " + username));
    }

    private Account createAccount(String country) {
        String countryCode = properties.getCountries().get(country);
        return accountService.createSavingAccount(countryCode, CurrencyType.EURO);
    }

    private void validateCustomerInfo(RegistrationRequest request) {
        // Validate the uniqueness of the provided username
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new ConflictedUsernameException("The provided username has been used.", CONFLICTED_USERNAME);
        }

        // Validate the user age
        long age = request.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS);
        if (age < 18) {
            throw new BadRequestException("The user is below 18.", UNDER_AGE);
        }

        // Validate the Country is in list
        String country = request.getAddress().getCountry();
        if (!properties.getCountries().containsKey(country)) {
            throw new BadRequestException("The provided country is not supported", UNSUPPORTED_COUNTRY);
        }
    }
}
