package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.services.NoviStudentService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.StudentPodaciRequest;

import java.time.LocalDate;
import java.time.Period;

@Component
public class DodajStudentaController {

    private final NoviStudentService noviStudentService;

    // ---------- PODACI ----------
    @FXML private TextField imeTf;
    @FXML private TextField prezimeTf;
    @FXML private TextField srednjeImeTf;
    @FXML private TextField jmbgTf;

    @FXML private DatePicker datumRodjenjaDp;

    @FXML private ToggleGroup polGroup;
    @FXML private RadioButton muskiRb;
    @FXML private RadioButton zenskiRb;

    @FXML private TextField drzavljanstvoTf;
    @FXML private TextField emailTf;
    @FXML private TextField adresaTf;
    @FXML private TextField mestoPrebivalistaTf;

    public DodajStudentaController(NoviStudentService noviStudentService) {
        this.noviStudentService = noviStudentService;
    }

    @FXML
    public void initialize() {
        drzavljanstvoTf.setText("Srbija");
    }

    // ================== SACUVAJ STUDENTA ==================
    @FXML
    private void handleSacuvaj() {

        if (!validatePodaci()) return;

        String jmbg = jmbgTf.getText().trim();

        // ✅ PROVERA DA LI JMBG VEĆ POSTOJI
        noviStudentService.existsStudentByJmbg(jmbg)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        exists -> Platform.runLater(() -> {
                            if (exists) {
                                showError("Student sa unetim JMBG-om već postoji.");
                            } else {
                                sacuvajStudenta();
                            }
                        }),
                        err -> Platform.runLater(() ->
                                showError(err.getMessage()))
                );
    }

    // ================== REALNO ČUVANJE ==================
    private void sacuvajStudenta() {

        StudentPodaciRequest req = new StudentPodaciRequest();
        req.setIme(imeTf.getText().trim());
        req.setPrezime(prezimeTf.getText().trim());
        req.setSrednjeIme(srednjeImeTf.getText().trim());
        req.setJmbg(jmbgTf.getText().trim());
        req.setDatumRodjenja(datumRodjenjaDp.getValue());
        req.setMestoPrebivalista(mestoPrebivalistaTf.getText().trim());
        req.setDrzavljanstvo(drzavljanstvoTf.getText().trim());
        req.setEmail(emailTf.getText().trim());
        req.setAdresa(adresaTf.getText().trim());
        req.setPol(muskiRb.isSelected() ? 'M' : 'Z');

        noviStudentService.dodajStudenta(req)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        id -> Platform.runLater(() ->
                                showInfo("Student je uspešno sačuvan.")
                        ),
                        err -> Platform.runLater(() ->
                                showError(err.getMessage()))
                );
    }

    // ================== VALIDACIJA ==================
    private boolean validatePodaci() {

        if (imeTf.getText().isBlank()) {
            showError("Ime je obavezno.");
            return false;
        }

        if (prezimeTf.getText().isBlank()) {
            showError("Prezime je obavezno.");
            return false;
        }

        if (!jmbgTf.getText().matches("\\d{13}")) {
            showError("JMBG mora imati tačno 13 cifara.");
            return false;
        }

        LocalDate datumRodjenja = datumRodjenjaDp.getValue();
        if (datumRodjenja == null || datumRodjenja.isAfter(LocalDate.now())) {
            showError("Neispravan datum rođenja.");
            return false;
        }

        if (Period.between(datumRodjenja, LocalDate.now()).getYears() < 14) {
            showError("Student mora imati najmanje 14 godina.");
            return false;
        }

        if (polGroup.getSelectedToggle() == null) {
            showError("Morate izabrati pol.");
            return false;
        }

        return true;
    }

    // ================== ALERTI ==================
    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
