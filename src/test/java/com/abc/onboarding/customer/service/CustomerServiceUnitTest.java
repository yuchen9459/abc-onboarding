package com.abc.onboarding.customer.service;

import com.abc.onboarding.account.entity.Account;
import com.abc.onboarding.account.entity.CurrencyType;
import com.abc.onboarding.account.service.AccountService;
import com.abc.onboarding.common.exception.BadRequestException;
import com.abc.onboarding.common.exception.ConflictedUsernameException;
import com.abc.onboarding.customer.entity.Customer;
import com.abc.onboarding.customer.repository.CustomerRepository;
import com.abc.onboarding.payload.request.RegistrationRequest;
import com.abc.onboarding.payload.response.CustomerRegistrationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static com.abc.onboarding.helper.TestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CustomerServiceUnitTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(passwordEncoder,
                                              customerRepository,
                                              accountService,
                                              getOnboardingProperties()
        );
    }

    @Test
    public void testCreateCustomer_ValidRequest() {
        RegistrationRequest request = getRegistrationRequest();
        Customer expectedCustomer = Customer.of(request);
        Account expectedAccount = new Account(/* Initialize with valid data */);

        when(customerRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountService.createSavingAccount(anyString(), any(CurrencyType.class))).thenReturn(expectedAccount);
        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        CustomerRegistrationResponse response = customerService.createCustomer(request);

        verify(customerRepository, times(1)).existsByUsername(request.getUsername());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(accountService, times(1)).createSavingAccount(anyString(), any(CurrencyType.class));
        verify(customerRepository, times(1)).save(any(Customer.class));

        assertEquals(expectedCustomer.getUsername(), response.getUsername());
    }

    @Test
    public void testFindCustomerByUsername_ValidUsername() {
        String username = "testUsername";
        Customer expectedCustomer = new Customer(/* Initialize with valid data */);

        when(customerRepository.findByUsername(username)).thenReturn(java.util.Optional.of(expectedCustomer));
        Customer foundCustomer = customerService.findCustomerByUsername(username);

        verify(customerRepository, times(1)).findByUsername(username);
        assertEquals(expectedCustomer, foundCustomer);
    }

    @Test
    public void testFindCustomerByUsername_InvalidUsername() {
        String username = "nonExistentUsername";

        when(customerRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> customerService.findCustomerByUsername(username));
        verify(customerRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testCreateCustomer_DuplicateUsername() {
        RegistrationRequest request = new RegistrationRequest(/* Initialize with valid data */);

        when(customerRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(ConflictedUsernameException.class, () -> customerService.createCustomer(request));

        verify(customerRepository, times(1)).existsByUsername(request.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountService, never()).createSavingAccount(anyString(), any(CurrencyType.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomer_UnderAge() {
        RegistrationRequest request = getRegistrationRequest();
        request.setBirthDate(LocalDate.of(2022, 1, 1));

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(request));

        verify(customerRepository,times(1)).existsByUsername(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountService, never()).createSavingAccount(anyString(), any(CurrencyType.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomer_UnsupportedCountry() {
        RegistrationRequest request = getRegistrationRequest();
        request.setAddress(getUnsupportedAddress());

        assertThrows(BadRequestException.class, () -> customerService.createCustomer(request));

        verify(customerRepository, times(1)).existsByUsername(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(accountService, never()).createSavingAccount(anyString(), any(CurrencyType.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
