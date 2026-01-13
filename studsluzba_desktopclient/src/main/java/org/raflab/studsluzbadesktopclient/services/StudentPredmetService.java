package org.raflab.studsluzbadesktopclient.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import response.PredmetResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class StudentPredmetService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api";

    public StudentPredmetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PredmetResponse> getDostupniPredmeti(int godinaUpisa, String studProgramOznaka, String indeksGodinaBroj) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/predmet/dostupni")
                .queryParam("indeksGodinaBroj", indeksGodinaBroj)  // "2/123"
                .queryParam("maxGodina", godinaUpisa)
                .queryParam("studProgramOznaka", studProgramOznaka)
                .toUriString();

        ResponseEntity<PredmetResponse[]> response = restTemplate.getForEntity(url, PredmetResponse[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }
}

