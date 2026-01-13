package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.PrijavaIspitaRequest;
import response.PrijavaIspitaResponse;
import response.StudentIndeksResponse;

@Service
public class PrijavaIspitaClientService {

    private final WebClient webClient;

    public PrijavaIspitaClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api/prijavaIspita").build();
    }

    public Mono<PrijavaIspitaResponse> sacuvajPrijavu(PrijavaIspitaRequest request) {
        return webClient.post()
                .uri("/save")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PrijavaIspitaResponse.class)
                .doOnError(err -> System.err.println("sacuvajPrijavu error: " + err.getMessage()));
    }

    public Flux<PrijavaIspitaResponse> getSvePrijave() {
        return webClient.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(PrijavaIspitaResponse.class)
                .doOnError(err -> System.err.println("getSvePrijave error: " + err.getMessage()));
    }

    public Mono<Void> opozoviPrijavu(Long id) {
        return webClient.get()
                .uri("/delete/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(err -> System.err.println("opozoviPrijavu error: " + err.getMessage()));
    }

    // Za specifične endpointove studenta kasnije
    public Flux<StudentIndeksResponse> getStudentiZaIspit(Long ispitId) {
        return webClient.get()
                .uri("/studentiZaIspit/{ispitId}", ispitId)
                .retrieve()
                .bodyToFlux(StudentIndeksResponse.class)
                .doOnError(err -> System.err.println("getStudentiZaIspit error: " + err.getMessage()));
    }

    public Flux<PrijavaIspitaResponse> getPrijaveZaStudenta(Integer godina, Integer broj, String oznaka) {
        return webClient.get()
                .uri("/prijaveZaStudenta/{godina}/{broj}/{oznaka}", godina, broj, oznaka)
                .retrieve()
                .bodyToFlux(PrijavaIspitaResponse.class)
                .doOnError(err -> System.err.println("getPrijaveZaStudenta error: " + err.getMessage()));
    }

    public Flux<PrijavaIspitaResponse> getDostupniIspitiZaStudenta(Integer godina, Integer broj, String oznaka) {
        return webClient.get()
                .uri("/dostupniIspiti/{godina}/{broj}/{oznaka}", godina, broj, oznaka)
                .retrieve()
                .bodyToFlux(PrijavaIspitaResponse.class)
                .doOnError(err -> System.err.println("getDostupniIspiti error: " + err.getMessage()));
    }

    public Flux<PrijavaIspitaResponse> getAktivniDostupniIspiti(Integer godina, Integer broj, String oznaka) {
        return webClient.get()
                .uri("/aktivniDostupniIspiti/{godina}/{broj}/{oznaka}", godina, broj, oznaka)
                .retrieve()
                .bodyToFlux(PrijavaIspitaResponse.class)
                .doOnError(err -> System.err.println("getAktivniDostupni error: " + err.getMessage()));
    }


}
