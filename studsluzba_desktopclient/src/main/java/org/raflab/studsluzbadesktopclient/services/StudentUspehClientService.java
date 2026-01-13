package org.raflab.studsluzbadesktopclient.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pomocniDTO.PageResultDTO;
import reactor.core.publisher.Mono;
import request.StudentIndeksRequest;
import response.PolozeniUspehRowResponse;
import response.PredmetResponse;

import java.util.List;

@Service
public class StudentUspehClientService {

    private final WebClient webClient;
    private final ObjectMapper om;

    public StudentUspehClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api").build();

        this.om = new ObjectMapper();
        this.om.registerModule(new JavaTimeModule());
        this.om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Mono<PageResultDTO<PolozeniUspehRowResponse>> getPolozeniPage(StudentIndeksRequest req, int page, int size) {
        return webClient.post()
                .uri("/student/uspeh/polozeni-tabela?page={page}&size={size}", page, size)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parsePagePolozeni);
    }

    public Mono<PageResultDTO<PredmetResponse>> getNepolozeniPage(StudentIndeksRequest req, int page, int size) {
        return webClient.post()
                .uri("/student/uspeh/nepolozeni?page={page}&size={size}", page, size)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parsePageNepolozeni);
    }

    private PageResultDTO<PolozeniUspehRowResponse> parsePagePolozeni(String json) {
        try {
            JsonNode root = om.readTree(json);

            List<PolozeniUspehRowResponse> content = om.readValue(
                    root.path("content").toString(),
                    new TypeReference<List<PolozeniUspehRowResponse>>() {}
            );

            return new PageResultDTO<>(
                    content,
                    root.path("totalPages").asInt(),
                    root.path("totalElements").asLong(),
                    root.path("number").asInt(),
                    root.path("size").asInt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Polozeni JSON parsing failed", e);
        }
    }

    private PageResultDTO<PredmetResponse> parsePageNepolozeni(String json) {
        try {
            JsonNode root = om.readTree(json);

            List<PredmetResponse> content = om.readValue(
                    root.path("content").toString(),
                    new TypeReference<List<PredmetResponse>>() {}
            );

            return new PageResultDTO<>(
                    content,
                    root.path("totalPages").asInt(),
                    root.path("totalElements").asLong(),
                    root.path("number").asInt(),
                    root.path("size").asInt()
            );
        } catch (Exception e) {
            throw new RuntimeException("Nepolozeni JSON parsing failed", e);
        }
    }

    public Mono<Integer> getUkupnoEspbPolozeni(StudentIndeksRequest req) {
        return webClient.post()
                .uri("/student/uspeh/polozeni/ukupno-espb")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Integer.class);
    }

    public Mono<Double> getProsecnaOcenaPolozeni(StudentIndeksRequest req) {
        return webClient.post()
                .uri("/student/uspeh/polozeni/prosecna-ocena")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Double.class);
    }


}
