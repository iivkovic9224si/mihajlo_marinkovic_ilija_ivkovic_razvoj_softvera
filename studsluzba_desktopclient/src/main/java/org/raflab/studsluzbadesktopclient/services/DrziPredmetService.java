package org.raflab.studsluzbadesktopclient.services;

import org.raflab.studsluzbadesktopclient.dtos.DrziPredmetResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DrziPredmetService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/drziPredmet/";

    public DrziPredmetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<DrziPredmetResponse> findAll() {
        ResponseEntity<List<DrziPredmetResponse>> response =
                restTemplate.exchange(
                        baseUrl + "findAll",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<DrziPredmetResponse>>() {}
                );
        return response.getBody();
    }
}

