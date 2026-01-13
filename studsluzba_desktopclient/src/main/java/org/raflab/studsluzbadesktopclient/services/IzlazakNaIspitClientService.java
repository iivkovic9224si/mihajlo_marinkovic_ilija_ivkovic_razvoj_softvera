package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.IzlazakNaIspitRequest;
import response.IzlazakNaIspitResponse;
import response.PrijavaIspitaResponse;

@Service
public class IzlazakNaIspitClientService {
    private final WebClient webClient;

    public IzlazakNaIspitClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api/izlazakNaIspit").build();
    }

    public Flux<PrijavaIspitaResponse> getPrijaveZaIzlazak(Integer godina, Integer broj, String oznaka) {
        return webClient.get()
                .uri("/prijaveZaIzlazak/{godina}/{broj}/{oznaka}", godina, broj, oznaka)
                .retrieve()
                .bodyToFlux(PrijavaIspitaResponse.class);
    }

    public Flux<IzlazakNaIspitResponse> getIzlasciZaStudenta(Integer godina, Integer broj, String oznaka) {
        return webClient.get()
                .uri("/izlasciZaStudenta/{godina}/{broj}/{oznaka}", godina, broj, oznaka)
                .retrieve()
                .bodyToFlux(IzlazakNaIspitResponse.class);
    }

    public Mono<IzlazakNaIspitResponse> sacuvajIzlazak(IzlazakNaIspitRequest request) {
        return webClient.post()
                .uri("/saveWithPolozen")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(IzlazakNaIspitResponse.class);
    }
}
