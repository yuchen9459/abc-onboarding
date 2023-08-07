package com.abc.onboarding.payload.response;

import com.abc.onboarding.account.entity.Account;

import java.time.Instant;

import static com.abc.onboarding.common.helper.IbanHelper.generateIban;

public class AccountResponse {

    private Long accountNumber;
    private String iban;
    private String accountType;
    private double balance;
    private String currency;
    private Instant creationTimestamp;

    public static AccountResponse of(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getType().toString());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency().toString());
        response.setCreationTimestamp(account.getCreationDate());
        response.setIban(generateIban(account.getAccountNumber()));
        return response;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
