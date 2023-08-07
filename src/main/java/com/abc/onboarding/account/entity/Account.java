package com.abc.onboarding.account.entity;

import jakarta.persistence.*;
import org.iban4j.CountryCode;

import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountNumber;
    private long balance;
    private CountryCode countryCode;
    private AccountType type;
    private CurrencyType currency;
    private Instant creationDate;

    public Account() {
    }

    public Account(CountryCode countryCode, AccountType type, CurrencyType currency, Instant creationDate) {
        this.countryCode = countryCode;
        this.type = type;
        this.currency = currency;
        this.creationDate = creationDate;
    }

    public static Account of(String country, AccountType type, CurrencyType currency) {
        CountryCode.valueOf(country);
        Account account = new Account(CountryCode.valueOf(country), type, currency, Instant.now());
        account.setBalance(0);
        return account;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public AccountType getType() {
        return type;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
