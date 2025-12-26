package com.boss_battle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



import java.util.Map;

@Service
public class CaptchaService {

   @Value("${turnstile.secret}")
   private String secretKey;


   public boolean isValid(String token) {
	    String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";
	    RestTemplate restTemplate = new RestTemplate();

	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("secret", secretKey);
	    params.add("response", token);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

	    try {
	        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
	        System.out.println("[CaptchaService] HTTP status: " + response.getStatusCode());
	        System.out.println("[CaptchaService] Body: " + response.getBody());

	        if (response.getBody() == null) return false;

	        Object success = response.getBody().get("success");
	        if (Boolean.TRUE.equals(success)) {
	            return true;
	        } else {
	            Object errors = response.getBody().get("error-codes");
	            System.err.println("[CaptchaService] Verificação falhou. error-codes: " + errors);
	            return false;
	        }
	    } catch (Exception e) {
	        System.err.println("[CaptchaService] Exceção ao chamar Turnstile: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}


}
