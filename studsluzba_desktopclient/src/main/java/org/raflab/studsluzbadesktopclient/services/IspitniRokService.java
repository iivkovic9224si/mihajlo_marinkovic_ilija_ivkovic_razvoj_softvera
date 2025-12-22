package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import request.IspitniRokRequest;
import response.IspitniRokResponse;
import response.IspitResponse;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IspitniRokService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/ispitniRok";

    public IspitniRokResponse save(IspitniRokRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<IspitniRokRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(baseUrl + "/save", entity, IspitniRokResponse.class);
    }

    public List<IspitniRokResponse> findAll() {
        ResponseEntity<List<IspitniRokResponse>> response = restTemplate.exchange(
                baseUrl + "/findAll",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IspitniRokResponse>>() {}
        );
        return response.getBody();
    }

    public List<IspitniRokResponse> findAllAktivna() {
        ResponseEntity<List<IspitniRokResponse>> response = restTemplate.exchange(
                baseUrl + "/findAllAktivna",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IspitniRokResponse>>() {}
        );
        return response.getBody();
    }

    public Optional<IspitniRokResponse> findById(Long id) {
        return Optional.ofNullable(
                restTemplate.getForObject(baseUrl + "/findById/" + id, IspitniRokResponse.class)
        );
    }

    public IspitniRokResponse update(IspitniRokRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<IspitniRokRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject(baseUrl + "/update", entity, IspitniRokResponse.class);
    }

    public void delete(Long id) {
        restTemplate.delete(baseUrl + "/delete/" + id);
    }

    // Vrati sve ispite za dati IspitniRok
    public List<IspitResponse> findIspitiByRokId(Long rokId) {
        ResponseEntity<List<IspitResponse>> response = restTemplate.exchange(
                "http://localhost:8090/api/ispit/byRok/" + rokId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IspitResponse>>() {}
        );
        return response.getBody();
    }
}
