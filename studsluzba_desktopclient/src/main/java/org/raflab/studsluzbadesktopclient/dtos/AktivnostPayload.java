package org.raflab.studsluzbadesktopclient.dtos;

// npr. u istom paketu
import java.util.List;

public class AktivnostPayload {
    private StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest;
    private List<DrziPredmetRequest> drziPredmetRequests;

    public AktivnostPayload() {}
    public AktivnostPayload(StudijskiTokAktivnostiRequest r, List<DrziPredmetRequest> l) {
        this.studijskiTokAktivnostiRequest = r;
        this.drziPredmetRequests = l;
    }

    // getteri/setteri
}

