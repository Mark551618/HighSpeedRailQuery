package com.example.hsr.service;

import com.example.hsr.config.TdxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Autowired
    private TdxConfig tdxConfig;
    @Autowired
    private RestTemplate restTemplate;

    private String accessToken;
    private long tokenExpireTime = 0;

    public String getAccessToken() {
        String url = "https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", tdxConfig.getClientId());
        formData.add("client_secret", tdxConfig.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            return (String) body.get("access_token");
        } else {
            throw new RuntimeException("無法取得 access_token，狀態碼: " + response.getStatusCode());
        }
    }
}