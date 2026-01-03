package com.github.lunasis.global.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleProperty {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
