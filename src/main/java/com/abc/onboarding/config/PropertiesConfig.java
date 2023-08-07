package com.abc.onboarding.config;

import com.abc.onboarding.common.properties.OnboardingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OnboardingProperties.class)
public class PropertiesConfig {

}
