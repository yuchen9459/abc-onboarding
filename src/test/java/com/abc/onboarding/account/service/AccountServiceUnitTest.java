package com.abc.onboarding.account.service;
import com.abc.onboarding.account.entity.Account;
import com.abc.onboarding.account.entity.AccountType;
import com.abc.onboarding.account.entity.CurrencyType;
import com.abc.onboarding.account.respository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService(accountRepository);
    }

    @Test
    public void testCreateSavingAccount() {
        String countryCode = "US";
        CurrencyType currencyType = CurrencyType.EURO;

        Account expectedAccount = Account.of(countryCode, AccountType.SAVING, currencyType);
        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);

        Account createdAccount = accountService.createSavingAccount(countryCode, currencyType);

        verify(accountRepository, times(1)).save(any(Account.class));
        assertEquals(expectedAccount, createdAccount);
    }

    @Test
    public void testCreateCurrentAccount() {
        String countryCode = "GB";
        CurrencyType currencyType = CurrencyType.EURO;

        Account expectedAccount = Account.of(countryCode, AccountType.CURRENT, currencyType);
        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);

        Account createdAccount = accountService.createCurrentAccount(countryCode, currencyType);

        verify(accountRepository, times(1)).save(any(Account.class));
        assertEquals(expectedAccount, createdAccount);
    }
}
