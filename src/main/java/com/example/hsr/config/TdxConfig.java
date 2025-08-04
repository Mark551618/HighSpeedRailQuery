package com.example.hsr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TdxConfig {

    @Value("${tdx.client-id}")
    private String clientId;

    @Value("${tdx.client-secret}")
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
