package com.ads.assignment1.dbaccess.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class RestService {
	
	public String getUserUniqueID(String session_id ){
		RestTemplate restTemplate = new RestTemplate();
		
		//String url = "http://auth_server:80/login_auth/verify_token";
		// Local Debug
		String url = "http://localhost:9000/login_auth/verify_token";
		
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters	
        Map<String, Object> map = new HashMap<>();
        //map.put("session_id", objUserSession.getSession_id());
        map.put("session_id", session_id);
        
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        
        return response.getBody();
	}

	public String authenticateUser(String session_id ){
		
		JSONObject obj = new JSONObject(getUserUniqueID(session_id));
		
		if( obj.getString("STATUS") == "TOKEN VERIFIED")
			return obj.getJSONObject("DECODED_DATA").getString("UNIQUE_USER_ID");
		else
			return "";
	}
		
	/*
	private RestTemplate restTemplate = new RestTemplate();
	
	
	public RestService(RestTemplateBuilder restTemplateBuilder) {
	    // set connection and read timeouts
	    this.restTemplate = restTemplateBuilder
	            .setConnectTimeout(Duration.ofSeconds(500))
	            .setReadTimeout(Duration.ofSeconds(500))
	            .build();
	}
	
	public String createPost(String session_id) {
        String url = "http://localhost:3000/login_auth/verify_token";
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        
        // create a map for post parameters	
        Map<String, Object> map = new HashMap<>();
        //map.put("session_id", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJFTUFJTF9JRCI6ImFiY0BpdS5lZHUiLCJVTklRVUVfVVNFUl9JRCI6IjYyMDBhMjYxZTg2YWUwMWM4ZGFhYjhhNyIsIkxPR0lOX0RBVEVUSU1FIjoiMjAyMi0wMi0wN1QwNDozODo1Ny41MjRaIiwiaWF0IjoxNjQ0MjA4NzM3LCJleHAiOjE2NDQyOTUxMzd9.copVip259z8HzxU6cADcbqLl-8FcFfo_lU-4scQbReg");
        map.put("session_id", session_id);
        
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);

		
        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
        
        
        return response.getBody();
		
    }	
    */
    
}
