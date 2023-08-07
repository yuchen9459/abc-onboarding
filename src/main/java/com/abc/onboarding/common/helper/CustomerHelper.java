package com.abc.onboarding.common.helper;

import org.apache.commons.lang3.RandomStringUtils;

public class CustomerHelper {

    private CustomerHelper() {
    }

    public static String generateTempPassword() {
        return RandomStringUtils.randomAlphabetic(5).toUpperCase() + RandomStringUtils.random(1,
                                                                                              '@',
                                                                                              '!',
                                                                                              '$',
                                                                                              '#'
        ) + RandomStringUtils.randomAlphabetic(5)
                             .toLowerCase() + RandomStringUtils.randomNumeric(5,
                                                                              10
        );
    }
}
