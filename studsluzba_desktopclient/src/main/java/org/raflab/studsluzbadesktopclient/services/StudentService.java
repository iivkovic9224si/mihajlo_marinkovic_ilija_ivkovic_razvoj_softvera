package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzbadesktopclient.dtos.PageResultDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentIndexDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final String baseUrl;

    private final String STUDENT_URL_PATH = "/student";

    public int update(Long id, StudentDTO student) {
        try {
            // PUT REST poziv na backend
            restTemplate.put(
                    createURL("updateujStudenta/" + id),
                    student // ovo će biti serijalizovano u JSON
            );
            return 1; // uspešno
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // neuspešno
        }
    }


    private String createURL(String pathEnd) {
        return baseUrl + STUDENT_URL_PATH + "/" + pathEnd;
    }

    // ----------------- Dobavljanje svih studenata -----------------
    public List<StudentDTO> sviStudenti() {
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(
                createURL("all"),
                StudentDTO[].class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return List.of(response.getBody());
        else return List.of();
    }

    // ----------------- Pretraga studenta po imenu -----------------
    public List<StudentDTO> searchStudent(String ime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("imeandorprezime"))
                .queryParam("ime", ime);
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(),
                StudentDTO[].class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return List.of(response.getBody());
        else return List.of();
    }

    // ----------------- Asinkrona pretraga studenta sa paginacijom -----------------
    public Mono<PageResultDTO<StudentDTO>> searchStudentsPaged(String ime, String prezime, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(STUDENT_URL_PATH + "/imeandorprezime")
                            .queryParam("page", page)
                            .queryParam("size", size);

                    if (ime != null && !ime.isEmpty()) {
                        uriBuilder.queryParam("ime", ime + "%"); // dodaj % za like pretragu
                    }
                    if (prezime != null && !prezime.isEmpty()) {
                        uriBuilder.queryParam("prezime", prezime + "%");
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResultDTO<StudentDTO>>() {});
    }
    // ----------------- Dobavljanje svih indeksa studenta -----------------
    public List<String> getIndeksiForStudent(Long studentId) {
        // Poziv REST API-ja: /api/student/indeksi/all/{studentId}
        ResponseEntity<StudentIndexDTO[]> response = restTemplate.getForEntity(
                baseUrl + "/student/indeksi/" + studentId,
                StudentIndexDTO[].class
        );

        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Mapiramo na format broj/godinaStudProgramOznaka
            return List.of(response.getBody()).stream()
                    .map(i -> i.getBroj() + "/" + i.getGodina() + i.getStudProgramOznaka().toUpperCase())
                    .toList();
        } else return List.of();
    }


    // ----------------- Snimanje studenta -----------------
    public Long saveStudent(StudentDTO studentDTO) {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                createURL("add"),
                new HttpEntity<>(studentDTO),
                Long.class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return response.getBody();
        else return null;
    }
}
