package org.raflab.studsluzbadesktopclient.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import request.DrziPredmetRequest;
import request.StudijskiTokAktivnostiRequest;

import java.util.List;

@Service
public class StudentTokAktivnostiService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/studijski-tok/";

    public StudentTokAktivnostiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int upisiNaGodinu(StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        return send("upisstudenta", req, predmeti);
    }

    public int obnovaGodine(StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        return send("obnova", req, predmeti);
    }

    private int send(String path, StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        try {
            String url = baseUrl + path;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            AktivnostPayload payload = new AktivnostPayload(req, predmeti);
            HttpEntity<AktivnostPayload> entity = new HttpEntity<>(payload, headers);

            restTemplate.postForEntity(url, entity, Void.class);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // omot klasa za JSON telo
    public static class AktivnostPayload {
        private StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest;
        private List<DrziPredmetRequest> drziPredmetRequests;

        public AktivnostPayload() {}
        public AktivnostPayload(StudijskiTokAktivnostiRequest r, List<DrziPredmetRequest> l) {
            this.studijskiTokAktivnostiRequest = r;
            this.drziPredmetRequests = l;
        }

        // getteri/setteri
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
}
