package com.abc.onboarding.helper;

import com.abc.onboarding.common.properties.OnboardingProperties;
import com.abc.onboarding.payload.request.AddressDTO;
import com.abc.onboarding.payload.request.RegistrationRequest;

import java.time.LocalDate;
import java.util.Map;

public class TestHelper {

    public static OnboardingProperties getOnboardingProperties() {
        OnboardingProperties properties = new OnboardingProperties();
        Map<String, String> countries = Map.of("Netherlands", "NL", "Germany", "DE", "Belgium", "BE");
        properties.setCountries(countries);
        return properties;
    }

    public static RegistrationRequest getRegistrationRequest() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("test");
        request.setUsername("test");
        request.setBirthDate(LocalDate.of(1997, 6, 7));
        request.setDocumentId("test");
        request.setAddress(getAddress());
        return request;
    }

    public static AddressDTO getAddress() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Amsterdam");
        addressDTO.setCountry("Netherlands");
        addressDTO.setState("South Holland");
        addressDTO.setStreet("Van Halewijnlaan");
        addressDTO.setZipCode("2595TW");
        return addressDTO;
    }

    public static AddressDTO getUnsupportedAddress() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Bali");
        addressDTO.setCountry("Indonesia");
        addressDTO.setState("South Holland");
        addressDTO.setStreet("Van Halewijnlaan");
        addressDTO.setZipCode("2595TW");
        return addressDTO;
    }
}
