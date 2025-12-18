package org.raflab.studsluzbadesktopclient.services;

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
}

