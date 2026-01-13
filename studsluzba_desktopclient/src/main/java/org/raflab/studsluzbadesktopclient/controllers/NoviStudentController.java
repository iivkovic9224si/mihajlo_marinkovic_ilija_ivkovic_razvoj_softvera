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

import java.lang.reflect.Method;
import java.time.LocalDate;

@Component
public class NoviStudentController {

    @Autowired(required = false)
    private NoviStudentService noviStudentService;

    // --- PODACI ---
    @FXML private TextField imeTf;
    @FXML private TextField prezimeTf;
    @FXML private TextField srednjeImeTf;
    @FXML private TextField jmbgTf;

    @FXML private DatePicker datumRodjenjaDp;
    @FXML private TextField mestoRodjenjaTf;
    @FXML private TextField drzavaRodjenjaTf;

    @FXML private ToggleGroup polGroup;
    @FXML private RadioButton muskiRb;
    @FXML private RadioButton zenskiRb;

    @FXML private TextField drzavljanstvoTf;
    @FXML private TextField nacionalnostTf;

    @FXML private TextField emailTf;
    @FXML private TextField mobilniTf;
    @FXML private TextField fiksniTf;

    @FXML private TextField adresaTf;
    @FXML private TextField mestoPrebivalistaTf;

    @FXML private TextField brojLicneKarteTf;
    @FXML private TextField licnuKartuIzdaoTf;
    @FXML private TextField mestoStanovanjaTf;
    @FXML private TextField adresaStanovanjaTf;

    // --- INDEKS ---
    @FXML private TabPane tabPane;
    @FXML private Tab indeksTab;
    @FXML private Label indeksInfoLabel;

    @FXML private TextField studProgramOznakaTf;
    @FXML private TextField godinaTf;
    @FXML private TextField nacinFinansiranjaTf;
    @FXML private CheckBox aktivanCb;
    @FXML private DatePicker vaziOdDp;

    private Long savedStudentId = null;

    public NoviStudentController() {
    }

    @FXML
    private void handleZatvori() {
        ((Stage) imeTf.getScene().getWindow()).close();
    }


    public NoviStudentController(NoviStudentService noviStudentService) {
        this.noviStudentService = noviStudentService;
    }

    @FXML
    public void initialize() {
        if (noviStudentService == null) {
            noviStudentService = new NoviStudentService();
        }

        datumRodjenjaDp.setValue(null);
        drzavljanstvoTf.setText("Srbija");

        // Indeks tab default
        indeksTab.setDisable(true);
        indeksInfoLabel.setText("Prvo sačuvaj studenta u tabu Podaci.");
        vaziOdDp.setValue(LocalDate.now());
        aktivanCb.setSelected(true);
    }

    @FXML
    private void handleSacuvaj() {
        if (!validateRequiredPodaci()) return;

        StudentPodaciRequest req = new StudentPodaciRequest();
        req.setIme(imeTf.getText().trim());
        req.setPrezime(prezimeTf.getText().trim());
        req.setSrednjeIme(srednjeImeTf.getText().trim());
        req.setJmbg(emptyToNull(jmbgTf.getText()));

        req.setDatumRodjenja(datumRodjenjaDp.getValue());
        req.setMestoRodjenja(emptyToNull(mestoRodjenjaTf.getText()));
        req.setDrzavaRodjenja(emptyToNull(drzavaRodjenjaTf.getText()));

        req.setMestoPrebivalista(mestoPrebivalistaTf.getText().trim());
        req.setDrzavljanstvo(drzavljanstvoTf.getText().trim());
        req.setNacionalnost(emptyToNull(nacionalnostTf.getText()));

        Character pol = muskiRb.isSelected() ? 'M' : (zenskiRb.isSelected() ? 'Z' : null);
        req.setPol(pol);

        req.setAdresa(adresaTf.getText().trim());
        req.setEmail(emailTf.getText().trim());

        req.setBrojTelefonaMobilni(emptyToNull(mobilniTf.getText()));
        req.setBrojTelefonaFiksni(emptyToNull(fiksniTf.getText()));

        req.setBrojLicneKarte(emptyToNull(brojLicneKarteTf.getText()));
        req.setLicnuKartuIzdao(emptyToNull(licnuKartuIzdaoTf.getText()));
        req.setMestoStanovanja(emptyToNull(mestoStanovanjaTf.getText()));
        req.setAdresaStanovanja(emptyToNull(adresaStanovanjaTf.getText()));

        noviStudentService.dodajStudenta(req)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        id -> Platform.runLater(() -> {
                            savedStudentId = id;
                            showAlert(Alert.AlertType.INFORMATION, "Student sačuvan! ID: " + id);

                            indeksTab.setDisable(false);
                            indeksInfoLabel.setText("Student ID: " + id + " (sada možeš da sačuvaš indeks).");

                            // prebaci na indeks tab
                            tabPane.getSelectionModel().select(indeksTab);
                        }),
                        err -> Platform.runLater(() ->
                                showAlert(Alert.AlertType.ERROR, "Greška: " + err.getMessage()))
                );
    }

    @FXML
    private void handleSacuvajIndeks() {
        if (savedStudentId == null) {
            showAlert(Alert.AlertType.WARNING, "Prvo sačuvaj studenta (Podaci).");
            return;
        }
        if (!validateRequiredIndeks()) return;

        StudentIndeksRequest req = new StudentIndeksRequest();

        int godina;
        try {
            godina = Integer.parseInt(godinaTf.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Godina mora biti broj (npr. 2024)!");
            godinaTf.requestFocus();
            return;
        }

        req.setGodina(godina);
        req.setStudProgramOznaka(studProgramOznakaTf.getText().trim());

        // Backend ti sam računa sledeći broj, ali DTO ima int broj — pošalji 0.
        req.setBroj(0);

        req.setNacinFinansiranja(emptyToNull(nacinFinansiranjaTf.getText()));
        req.setAktivan(aktivanCb.isSelected());
        req.setVaziOd(vaziOdDp.getValue());
        req.setStudentId(savedStudentId);

        noviStudentService.sacuvajIndeks(req)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        indeksId -> Platform.runLater(() ->
                                showAlert(Alert.AlertType.INFORMATION, "Indeks sačuvan! ID: " + indeksId)),
                        err -> Platform.runLater(() ->
                                showAlert(Alert.AlertType.ERROR, "Greška (indeks): " + err.getMessage()))
                );
    }

    @FXML
    private void handlePonisti() {
        clearForm();
    }

    private boolean validateRequiredPodaci() {
        if (imeTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Ime je obavezno!"); imeTf.requestFocus(); return false; }
        if (prezimeTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Prezime je obavezno!"); prezimeTf.requestFocus(); return false; }
        if (srednjeImeTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Srednje ime je obavezno!"); srednjeImeTf.requestFocus(); return false; }
        if (datumRodjenjaDp.getValue() == null) { showAlert(Alert.AlertType.WARNING, "Datum rođenja je obavezan!"); datumRodjenjaDp.requestFocus(); return false; }
        if (mestoPrebivalistaTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Mesto prebivališta je obavezno!"); mestoPrebivalistaTf.requestFocus(); return false; }
        if (drzavljanstvoTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Državljanstvo je obavezno!"); drzavljanstvoTf.requestFocus(); return false; }
        if (adresaTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Adresa je obavezna!"); adresaTf.requestFocus(); return false; }
        if (emailTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Email je obavezan!"); emailTf.requestFocus(); return false; }
        if (polGroup == null || polGroup.getSelectedToggle() == null) { showAlert(Alert.AlertType.WARNING, "Izaberite pol!"); muskiRb.requestFocus(); return false; }
        return true;
    }

    private boolean validateRequiredIndeks() {
        if (studProgramOznakaTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Studijski program je obavezan!"); studProgramOznakaTf.requestFocus(); return false; }
        if (godinaTf.getText().trim().isEmpty()) { showAlert(Alert.AlertType.WARNING, "Godina je obavezna!"); godinaTf.requestFocus(); return false; }
        if (vaziOdDp.getValue() == null) { showAlert(Alert.AlertType.WARNING, "Važi od je obavezno!"); vaziOdDp.requestFocus(); return false; }
        return true;
    }

    private void clearForm() {
        // Podaci
        imeTf.clear(); prezimeTf.clear(); srednjeImeTf.clear(); jmbgTf.clear();
        datumRodjenjaDp.setValue(null);
        mestoRodjenjaTf.clear(); drzavaRodjenjaTf.clear();
        if (polGroup != null) polGroup.selectToggle(null);
        drzavljanstvoTf.setText("Srbija");
        nacionalnostTf.clear();
        emailTf.clear(); mobilniTf.clear(); fiksniTf.clear();
        adresaTf.clear(); mestoPrebivalistaTf.clear();
        brojLicneKarteTf.clear(); licnuKartuIzdaoTf.clear();
        mestoStanovanjaTf.clear(); adresaStanovanjaTf.clear();

        // Indeks
        studProgramOznakaTf.clear();
        godinaTf.clear();
        nacinFinansiranjaTf.clear();
        aktivanCb.setSelected(true);
        vaziOdDp.setValue(LocalDate.now());

        savedStudentId = null;
        indeksTab.setDisable(true);
        indeksInfoLabel.setText("Prvo sačuvaj studenta u tabu Podaci.");
        tabPane.getSelectionModel().select(0);

        imeTf.requestFocus();
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Platform.runLater(() -> new Alert(type, message).showAndWait());
    }

    private void setIfExists(Object target, String setterName, String value) {
        if (value == null) return;
        try {
            Method m = target.getClass().getMethod(setterName, String.class);
            m.invoke(target, value);
        } catch (NoSuchMethodException e) {
            // Ako tvoj DTO nema taj setter, samo ignoriši (ili promeni setterName da se poklopi).
            System.err.println("DTO nema " + setterName + "(String) – proveri ime polja u StudentPodaciRequest.");
        } catch (Exception e) {
            System.err.println("Ne mogu da setujem srednju skolu: " + e.getMessage());
        }
    }
}
