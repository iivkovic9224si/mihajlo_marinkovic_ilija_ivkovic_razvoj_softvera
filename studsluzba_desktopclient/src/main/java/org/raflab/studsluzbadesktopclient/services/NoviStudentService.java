package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import request.StudentIndeksRequest;
import request.StudentPodaciRequest;

@Service
public class NoviStudentService {

    private final WebClient webClient;

    public NoviStudentService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api/student").build();
    }

    public Mono<Long> dodajStudenta(StudentPodaciRequest req) {
        return webClient.post()
                .uri("/add")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<Long> sacuvajIndeks(StudentIndeksRequest req) {
        return webClient.post()
                .uri("/saveindeks")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<Boolean> existsStudentByJmbg(String jmbg) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exists-by-jmbg")
                        .queryParam("jmbg", jmbg)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<Long> findStudentIdByJmbg(String jmbg) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find-id-by-jmbg")
                        .queryParam("jmbg", jmbg)
                        .build())
                .retrieve()
                .bodyToMono(Long.class);
    }

}
