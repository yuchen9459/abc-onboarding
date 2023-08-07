package com.abc.onboarding.account.service;

import com.abc.onboarding.account.entity.Account;
import com.abc.onboarding.account.entity.AccountType;
import com.abc.onboarding.account.entity.CurrencyType;
import com.abc.onboarding.account.respository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createSavingAccount(String countryCode, CurrencyType currencyType) {
        return createAccount(countryCode, AccountType.SAVING, currencyType);
    }

    public Account createCurrentAccount(String countryCode, CurrencyType currencyType) {
        return createAccount(countryCode, AccountType.CURRENT, currencyType);
    }

    private Account createAccount(String countryCode, AccountType accountType, CurrencyType currencyType) {
        Account account = Account.of(countryCode, accountType, currencyType);
        return accountRepository.save(account);
    }
}
