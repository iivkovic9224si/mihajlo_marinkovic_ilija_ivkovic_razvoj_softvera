package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.services.NoviStudentService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.StudentIndeksRequest;

import java.time.LocalDate;

@Component
public class DodajIndexController {

    private final NoviStudentService noviStudentService;

    // ---------- JMBG ----------
    @FXML private TextField jmbgTf;
    @FXML private Button proveriBtn;
    @FXML private Label statusLabel;

    // ---------- INDEKS ----------
    @FXML private TextField studProgramOznakaTf;
    @FXML private TextField godinaTf;
    @FXML private TextField nacinFinansiranjaTf;
    @FXML private CheckBox aktivanCb;
    @FXML private DatePicker vaziOdDp;
    @FXML private Button sacuvajIndeksBtn;

    private Long studentId;

    public DodajIndexController(NoviStudentService noviStudentService) {
        this.noviStudentService = noviStudentService;
    }

    @FXML
    public void initialize() {
        disableIndeksForm(true);
        aktivanCb.setSelected(true);
        vaziOdDp.setValue(LocalDate.now());
    }

    // ================= PROVERA STUDENTA =================
    @FXML
    private void proveriStudenta() {

        String jmbg = jmbgTf.getText().trim();

        if (!jmbg.matches("\\d{13}")) {
            showError("JMBG mora imati tačno 13 cifara.");
            return;
        }

        statusLabel.setText("Proveravam studenta...");
        statusLabel.setStyle("-fx-text-fill: #555;");

        noviStudentService.existsStudentByJmbg(jmbg)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(exists -> {
                    if (!exists) {
                        return null;
                    }
                    return noviStudentService.findStudentIdByJmbg(jmbg);
                })
                .subscribe(
                        id -> Platform.runLater(() -> {
                            if (id == null) {
                                statusLabel.setText("Student sa tim JMBG-om ne postoji.");
                                statusLabel.setStyle("-fx-text-fill: red;");
                                disableIndeksForm(true);
                            } else {
                                studentId = id;
                                statusLabel.setText("Student pronađen. Možete uneti indeks.");
                                statusLabel.setStyle("-fx-text-fill: green;");
                                disableIndeksForm(false);
                            }
                        }),
                        err -> Platform.runLater(() ->
                                showError("Ne postoji student sa ovim jmbg"))
                );
    }

    // ================= SACUVAJ INDEKS =================
    @FXML
    private void handleSacuvajIndeks() {

        if (studentId == null) {
            showError("Prvo proverite studenta.");
            return;
        }

        if (!validateIndeks()) return;

        StudentIndeksRequest req = new StudentIndeksRequest();
        req.setStudentId(studentId);
        req.setStudProgramOznaka(studProgramOznakaTf.getText().trim().toUpperCase());
        req.setGodina(Integer.parseInt(godinaTf.getText().trim()));
        req.setBroj(0);
        req.setNacinFinansiranja(nacinFinansiranjaTf.getText().trim());
        req.setAktivan(aktivanCb.isSelected());
        req.setVaziOd(vaziOdDp.getValue());

        noviStudentService.sacuvajIndeks(req)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        id -> Platform.runLater(() ->
                                showInfo("Indeks uspešno sačuvan.")
                        ),
                        err -> Platform.runLater(() ->
                                showError("Studijski program ne postoji."))
                );
    }

    // ================= VALIDACIJA =================
    private boolean validateIndeks() {

        if (studProgramOznakaTf.getText().isBlank()) {
            showError("Studijski program je obavezan.");
            return false;
        }

        if (!godinaTf.getText().matches("\\d{4}")) {
            showError("Godina mora imati 4 cifre.");
            return false;
        }

        String nf = nacinFinansiranjaTf.getText().trim();
        if (!(nf.equalsIgnoreCase("Budzet")
                || nf.equalsIgnoreCase("Samofinansirajuci"))) {
            showError("Način finansiranja: Budzet ili Samofinansirajuci.");
            return false;
        }

        return true;
    }

    private void disableIndeksForm(boolean disable) {
        studProgramOznakaTf.setDisable(disable);
        godinaTf.setDisable(disable);
        nacinFinansiranjaTf.setDisable(disable);
        aktivanCb.setDisable(disable);
        vaziOdDp.setDisable(disable);
        sacuvajIndeksBtn.setDisable(disable);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
