package org.raflab.studsluzbadesktopclient.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import pomocniDTO.StudentUpisaneGodineRequest;
import request.DrziPredmetRequest;
import request.StudijskiTokAktivnostiRequest;
import response.DrziPredmetResponse;

import java.util.List;

@Service
public class StudentTokAktivnostiService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/studijski-tok/";

    public StudentTokAktivnostiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ================= POSTOJEĆE METODE =================

    public int upisiNaGodinu(StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        return send("upisstudenta", req, predmeti);
    }

    public int obnovaGodine(StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        return send("obnova", req, predmeti);
    }

    private int send(String path, StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<List<DrziPredmetResponse>> drziResp = restTemplate.exchange(
                    "http://localhost:8090/api/drziPredmet/findAll",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<DrziPredmetResponse>>() {}
            );

            List<DrziPredmetRequest> validPredmeti = drziResp.getBody().stream()
                    .filter(dpr -> predmeti.stream().anyMatch(p ->
                            p.getPredmetSifra().equals(dpr.getPredmetSifra()) &&
                                    p.getNastavnikEmail().equals(dpr.getNastavnikEmail())
                    ))
                    .map(this::mapToRequest)
                    .toList();

            String url = baseUrl + path;
            AktivnostPayload payload = new AktivnostPayload(req, validPredmeti);

            restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), Void.class);
            return 1;

        } catch (RestClientResponseException e) {
            String body = e.getResponseBodyAsString();
            throw new RuntimeException(extractMessage(body), e);
        }
    }

    private String extractMessage(String body) {
        try {
            JsonNode node = new ObjectMapper().readTree(body);
            if (node.hasNonNull("message")) return node.get("message").asText();
            return body;
        } catch (Exception ex) {
            return body;
        }
    }

    private DrziPredmetRequest mapToRequest(DrziPredmetResponse dpr) {
        DrziPredmetRequest req = new DrziPredmetRequest();
        req.setPredmetSifra(dpr.getPredmetSifra());
        req.setNastavnikEmail(dpr.getNastavnikEmail());
        return req;
    }

    // ================= POSTOJEĆI DTO =================

    public static class AktivnostPayload {
        private StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest;
        private List<DrziPredmetRequest> drziPredmetRequests;

        public AktivnostPayload() {}
        public AktivnostPayload(StudijskiTokAktivnostiRequest r, List<DrziPredmetRequest> l) {
            this.studijskiTokAktivnostiRequest = r;
            this.drziPredmetRequests = l;
        }

        public StudijskiTokAktivnostiRequest getStudijskiTokAktivnostiRequest() {
            return studijskiTokAktivnostiRequest;
        }

        public void setStudijskiTokAktivnostiRequest(StudijskiTokAktivnostiRequest s) {
            this.studijskiTokAktivnostiRequest = s;
        }

        public List<DrziPredmetRequest> getDrziPredmetRequests() {
            return drziPredmetRequests;
        }

        public void setDrziPredmetRequests(List<DrziPredmetRequest> d) {
            this.drziPredmetRequests = d;
        }
    }

    // ================= POSTOJEĆA METODA =================

    public Integer getPoslednjaGodina(int godinaIndeksa, int brojIndeksa, String studProgramOznaka) {
        try {
            List<Integer> godine = getUpisaneGodine(godinaIndeksa, brojIndeksa, studProgramOznaka);
            return godine.isEmpty() ? null : godine.get(godine.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= POSTOJEĆA METODA =================

    public List<Integer> getUpisaneGodine(int godinaIndeksa, int brojIndeksa, String studProgramOznaka) {
        StudentUpisaneGodineRequest req = new StudentUpisaneGodineRequest();
        req.setGodina(godinaIndeksa);
        req.setBroj(brojIndeksa);
        req.setStudProgramOznaka(studProgramOznaka);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudentUpisaneGodineRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<List<Integer>> resp = restTemplate.exchange(
                "http://localhost:8090/api/student/upisanegodine",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<Integer>>() {}
        );

        return resp.getBody();
    }

    // ================= ✅ NOVA METODA ZA OBNOVLJENE GODINE =================

    public List<Integer> getObnovljeneGodine(int godinaIndeksa, int brojIndeksa, String studProgramOznaka) {
        StudentUpisaneGodineRequest req = new StudentUpisaneGodineRequest();
        req.setGodina(godinaIndeksa);
        req.setBroj(brojIndeksa);
        req.setStudProgramOznaka(studProgramOznaka);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudentUpisaneGodineRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<List<Integer>> resp = restTemplate.exchange(
                "http://localhost:8090/api/student/obnovljenegodine",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<Integer>>() {}
        );

        return resp.getBody();
    }
}