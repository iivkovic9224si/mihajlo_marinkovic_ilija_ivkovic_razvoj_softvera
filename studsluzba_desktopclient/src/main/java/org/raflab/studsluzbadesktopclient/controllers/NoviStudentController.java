package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.services.NoviStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.StudentIndeksRequest;
import request.StudentPodaciRequest;

import java.time.LocalDate;
import java.time.Period;

@Component
public class NoviStudentController {

    @Autowired(required = false)
    private NoviStudentService noviStudentService;

    // ---------------- PODACI ----------------
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

    // ---------------- INDEKS ----------------
    @FXML private TabPane tabPane;
    @FXML private Tab indeksTab;
    @FXML private Label indeksInfoLabel;

    @FXML private TextField studProgramOznakaTf;
    @FXML private TextField godinaTf;
    @FXML private TextField nacinFinansiranjaTf;
    @FXML private CheckBox aktivanCb;
    @FXML private DatePicker vaziOdDp;

    private Long savedStudentId = null;

    public NoviStudentController(NoviStudentService noviStudentService) {
        this.noviStudentService = noviStudentService;
    }

    @FXML
    public void initialize() {
        indeksTab.setDisable(true);
        indeksInfoLabel.setText("Prvo sačuvaj studenta.");
        vaziOdDp.setValue(LocalDate.now());
        aktivanCb.setSelected(true);
        drzavljanstvoTf.setText("Srbija");
    }

    // ================== SACUVAJ STUDENTA ==================
    @FXML
    private void handleSacuvaj() {
        if (!validatePodaci()) return;

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
                        id -> Platform.runLater(() -> {
                            savedStudentId = id;
                            indeksTab.setDisable(false);
                            indeksInfoLabel.setText("Student sačuvan. Sada unesi indeks.");
                            tabPane.getSelectionModel().select(indeksTab);
                        }),
                        err -> Platform.runLater(() ->
                                showError(err.getMessage()))
                );
    }

    // ================== SACUVAJ INDEKS ==================
    @FXML
    private void handleSacuvajIndeks() {
        if (savedStudentId == null) {
            showError("Prvo sačuvaj studenta.");
            return;
        }

        if (!validateIndeks()) return;

        StudentIndeksRequest req = new StudentIndeksRequest();
        req.setStudentId(savedStudentId);
        req.setStudProgramOznaka(studProgramOznakaTf.getText().trim());
        req.setGodina(Integer.parseInt(godinaTf.getText().trim()));
        req.setBroj(0);
        req.setNacinFinansiranja(nacinFinansiranjaTf.getText().trim());
        req.setAktivan(aktivanCb.isSelected());
        req.setVaziOd(vaziOdDp.getValue());

        noviStudentService.sacuvajIndeks(req)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        id -> Platform.runLater(() ->
                                showInfo("Indeks uspešno sačuvan.")),
                        err -> Platform.runLater(() ->
                                showError(err.getMessage()))
                );
    }

    // ================== VALIDACIJE ==================

    private boolean validatePodaci() {

        if (!jmbgTf.getText().matches("\\d{13}")) {
            showError("JMBG mora imati tačno 13 cifara.");
            return false;
        }

        LocalDate datumRodjenja = datumRodjenjaDp.getValue();
        if (datumRodjenja == null) {
            showError("Datum rođenja je obavezan.");
            return false;
        }

        if (datumRodjenja.isAfter(LocalDate.now())) {
            showError("Datum rođenja ne može biti u budućnosti.");
            return false;
        }

        int godine = Period.between(datumRodjenja, LocalDate.now()).getYears();
        if (godine < 14) {
            showError("Student mora imati najmanje 14 godina.");
            return false;
        }

        if (polGroup.getSelectedToggle() == null) {
            showError("Morate izabrati pol.");
            return false;
        }

        return true;
    }

    private boolean validateIndeks() {

        if (studProgramOznakaTf.getText().isBlank()) {
            showError("Studijski program je obavezan.");
            return false;
        }

        if (!godinaTf.getText().matches("\\d{4}")) {
            showError("Godina mora imati tačno 4 cifre (npr. 2024).");
            return false;
        }

        String finansiranje = nacinFinansiranjaTf.getText().trim();
        if (!(finansiranje.equalsIgnoreCase("Budzet")
                || finansiranje.equalsIgnoreCase("Samofinansirajuci"))) {
            showError("Način finansiranja mora biti: Budzet ili Samofinansirajuci.");
            return false;
        }

        return true;
    }

    // ================== POMOCNE ==================

    @FXML
    private void handlePonisti() {
        ((Stage) imeTf.getScene().getWindow()).close();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
