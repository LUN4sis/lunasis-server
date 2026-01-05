package com.github.lunasis.global.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "apple")
public class AppleProperty {

    private String clientId;
    private String clientPrivateKey;
    private String teamId;
    private String redirectUri;
    private String keyId;
}
