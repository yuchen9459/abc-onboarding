package com.abc.onboarding.common.helper;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

public class IbanHelper {

    private final static String BANK_CODE = "ABCD";

    private IbanHelper() {
    }

    public static String generateIban(Long Bban) {
        return new Iban.Builder()
                .leftPadding(true)
                .countryCode(CountryCode.NL)
                .bankCode(BANK_CODE)
                .accountNumber(String.valueOf(Bban))
                .build()
                .toString();
    }
}
