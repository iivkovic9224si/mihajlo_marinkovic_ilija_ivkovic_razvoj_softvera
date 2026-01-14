package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import request.IspitRequest;
import response.IspitResponse;
import response.RezultatiIspitaResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IspitService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/ispit";

    /**
     * Čuva ispit na backend-u koji vraća Optional<IspitResponse>.
     * Ovo će ispravno tretirati i Optional.empty() sa backend-a.
     */
    public Optional<IspitResponse> save(IspitRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<IspitRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Optional<IspitResponse>> response = restTemplate.exchange(
                    baseUrl + "/save",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Optional<IspitResponse>>() {}
            );

            Optional<IspitResponse> saved = response.getBody();
            return saved != null ? saved : Optional.empty();

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<IspitResponse> findAll() {
        IspitResponse[] response = restTemplate.getForObject(baseUrl + "/findAll", IspitResponse[].class);
        return response != null ? Arrays.asList(response) : List.of();
    }

    public Optional<IspitResponse> findById(Long id) {
        return Optional.ofNullable(restTemplate.getForObject(baseUrl + "/findById/" + id, IspitResponse.class));
    }

    public Optional<IspitResponse> update(IspitRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<IspitRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Optional<IspitResponse>> response = restTemplate.exchange(
                    baseUrl + "/update",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Optional<IspitResponse>>() {}
            );

            Optional<IspitResponse> updated = response.getBody();
            return updated != null ? updated : Optional.empty();

        } catch (Exception e) {
            System.out.println("Poziv update() servisa je bacio izuzetak: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void delete(Long id) {
        restTemplate.getForObject(baseUrl + "/delete/" + id, Void.class);
    }

    public List<RezultatiIspitaResponse> getRezultati(Long ispitId) {
        RezultatiIspitaResponse[] response = restTemplate.getForObject(
                baseUrl + "/rezultati/" + ispitId, RezultatiIspitaResponse[].class
        );
        return response != null ? Arrays.asList(response) : List.of();
    }

    public Double getProsecnaOcena(Long ispitId) {
        return restTemplate.getForObject(baseUrl + "/prosecnaOcena/" + ispitId, Double.class);
    }
}
