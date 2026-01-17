package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import lombok.Data;
import org.raflab.studsluzbadesktopclient.services.StudentPredmetService;
import org.raflab.studsluzbadesktopclient.services.StudentTokAktivnostiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import request.DrziPredmetRequest;
import request.StudijskiTokAktivnostiRequest;
import response.PredmetResponse;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;
import response.StudijskiTokAktivnostiResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class StudentAktivnostiController {

    @FXML
    private ComboBox<String> tipAktivnostiCb;

    @FXML
    private Spinner<Integer> godinaSpinner;

    @FXML
    private DatePicker datumPicker;

    @FXML
    private TextField programTf;

    @FXML
    private TextField indeksGodinaTf;

    @FXML
    private TextField indeksBrojTf;

    @FXML
    private CheckBox indeksAktivanCb;

    @FXML
    private TextArea napomenaTa;

    private StudentPodaciResponse student;
    private StudentIndeksResponse izabraniIndeks;

    @FXML
    private TableView<PredmetSelection> predmetiTable;

    @FXML
    private TableColumn<PredmetSelection, Boolean> selectCol;

    @FXML
    private TableColumn<PredmetSelection, String> sifraCol;

    @FXML
    private TableColumn<PredmetSelection, String> nastavnikCol;

    @FXML private Label lblPoslednjaGodina;
    @FXML private Label lblUpisaneGodine;
    @FXML private Label lblObnovljeneGodine;

    @Autowired
    private StudentTokAktivnostiService aktivnostiService;

    @Autowired
    private StudentPredmetService predmetService;


    public StudentAktivnostiController() {}

    @FXML
    private void initialize() {
        tipAktivnostiCb.getItems().addAll("UPIS", "OBNOVA");
        godinaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        datumPicker.setValue(LocalDate.now());

        predmetiTable.setEditable(true);
        selectCol.setEditable(true);

        programTf.setEditable(false);
        indeksGodinaTf.setEditable(false);
        indeksBrojTf.setEditable(false);

        programTf.setDisable(true);
        indeksGodinaTf.setDisable(true);
        indeksBrojTf.setDisable(true);
        indeksAktivanCb.setDisable(true);

        sifraCol.setCellValueFactory(data -> data.getValue().predmetSifraProperty());
        nastavnikCol.setCellValueFactory(data -> data.getValue().nastavnikEmailProperty());
        selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectCol.setCellFactory(tc -> new CheckBoxTableCell<>());

        lblPoslednjaGodina.setText("Nema upisanih godina");
        lblUpisaneGodine.setText("/");
        lblObnovljeneGodine.setText("/");
    }

    public void setStudent(StudentPodaciResponse student) {
        this.student = student;
        postaviIzabraniIndeksUI();
    }

    public void setIzabraniIndeks(StudentIndeksResponse izabraniIndeks) {
        this.izabraniIndeks = izabraniIndeks;
        postaviIzabraniIndeksUI();
    }

    @FXML
    private void handleUpis() {
        sacuvajAktivnost("UPIS");
    }

    @FXML
    private void handleObnova() {
        sacuvajAktivnost("OBNOVA");
    }

    private void sacuvajAktivnost(String tip) {
        if (student == null || izabraniIndeks == null) {
            new Alert(Alert.AlertType.ERROR, "Student ili indeks nisu postavljeni.", ButtonType.OK).showAndWait();
            return;
        }

        try {
            StudijskiTokAktivnostiRequest req = new StudijskiTokAktivnostiRequest(
                    tip, godinaSpinner.getValue(), datumPicker.getValue(),
                    napomenaTa.getText(), indeksAktivanCb.isSelected(),
                    izabraniIndeks.getGodina(), izabraniIndeks.getBroj(),
                    izabraniIndeks.getStudProgramOznaka()
            );

            List<DrziPredmetRequest> izabraniPredmeti = predmetiTable.getItems().stream()
                    .filter(PredmetSelection::isSelected)
                    .map(p -> {
                        DrziPredmetRequest d = new DrziPredmetRequest();
                        d.setPredmetSifra(p.getPredmetSifra());
                        d.setNastavnikEmail(p.getNastavnikEmail());
                        return d;
                    })
                    .toList();

            if ("UPIS".equals(tip)) {
                aktivnostiService.upisiNaGodinu(req, izabraniPredmeti);
            } else {
                aktivnostiService.obnovaGodine(req, izabraniPredmeti);
            }

            new Alert(Alert.AlertType.INFORMATION, "Aktivnost uspešno sačuvana.", ButtonType.OK).showAndWait();
            osveziGodineAsync();

        } catch (RuntimeException ex) {
            // ✅ ovde stiže server poruka (ne preskači godinu / obnova pogrešna godina / ESPB > 60...)
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Greška: " + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @Data
    public static class PredmetSelection {
        private final StringProperty predmetSifra = new SimpleStringProperty();
        private final StringProperty nastavnikEmail = new SimpleStringProperty();
        private final BooleanProperty selected = new SimpleBooleanProperty(false);

        public PredmetSelection(String predmetSifra, String nastavnikEmail) {
            this.predmetSifra.set(predmetSifra);
            this.nastavnikEmail.set(nastavnikEmail);
        }

        public String getPredmetSifra() { return predmetSifra.get(); }
        public void setPredmetSifra(String value) { predmetSifra.set(value); }
        public StringProperty predmetSifraProperty() { return predmetSifra; }

        public String getNastavnikEmail() { return nastavnikEmail.get(); }
        public void setNastavnikEmail(String value) { nastavnikEmail.set(value); }
        public StringProperty nastavnikEmailProperty() { return nastavnikEmail; }

        public boolean isSelected() { return selected.get(); }
        public void setSelected(boolean value) { selected.set(value); }
        public BooleanProperty selectedProperty() { return selected; }
    }

    @FXML
    private Button ucitajPredmeteBtn;

    @FXML
    private void handleUcitajPredmete() {
        if (programTf.getText().isEmpty() || indeksGodinaTf.getText().isEmpty() || indeksBrojTf.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Popunite program i indeks!", ButtonType.OK).showAndWait();
            return;
        }

        int godinaUpisa = godinaSpinner.getValue();
        String program = programTf.getText();
        int indeksGodina = Integer.parseInt(indeksGodinaTf.getText());
        int indeksBroj = Integer.parseInt(indeksBrojTf.getText());
        String indeksGodinaBroj = indeksGodinaTf.getText() + "/" + indeksBrojTf.getText();

        try {
            List<PredmetResponse> predmeti = predmetService.getDostupniPredmeti(
                    godinaUpisa, program, indeksGodinaBroj, indeksGodina, indeksBroj);

            predmetiTable.getItems().clear();
            predmeti.forEach(p -> predmetiTable.getItems().add(
                    new PredmetSelection(p.getSifra(), p.getNaziv())  // ✅ FIX: naziv umesto null
            ));

            new Alert(Alert.AlertType.INFORMATION,
                    "✅ Učitano " + predmeti.size() + " NEPOLOŽENIH predmeta za godine ≤" + godinaUpisa,
                    ButtonType.OK).showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Greška: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void postaviIzabraniIndeksUI() {
        if (izabraniIndeks != null) {
            programTf.setText(izabraniIndeks.getStudProgramOznaka());
            indeksGodinaTf.setText(String.valueOf(izabraniIndeks.getGodina()));
            indeksBrojTf.setText(String.valueOf(izabraniIndeks.getBroj()));
            indeksAktivanCb.setSelected(izabraniIndeks.isAktivan());

            osveziGodineAsync(); // ✅ PROMENJENO: zove novu metodu
        } else {
            lblPoslednjaGodina.setText("Nema indeksa");
            lblUpisaneGodine.setText("/");
            lblObnovljeneGodine.setText("/");
        }
    }

    // ✅ NOVA METODA koja osvežava sve 3 labele
    private void osveziGodineAsync() {
        if (izabraniIndeks == null) {
            lblPoslednjaGodina.setText("Nema indeksa");
            lblUpisaneGodine.setText("/");
            lblObnovljeneGodine.setText("/");
            return;
        }

        lblPoslednjaGodina.setText("Učitavam...");
        lblUpisaneGodine.setText("Učitavam...");
        lblObnovljeneGodine.setText("Učitavam...");

        Task<GodineResult> task = new Task<>() {
            @Override
            protected GodineResult call() {
                Integer poslednjaGodina = aktivnostiService.getPoslednjaGodina(
                        izabraniIndeks.getGodina(),
                        izabraniIndeks.getBroj(),
                        izabraniIndeks.getStudProgramOznaka()
                );

                List<Integer> upisaneGodine = aktivnostiService.getUpisaneGodine(
                        izabraniIndeks.getGodina(),
                        izabraniIndeks.getBroj(),
                        izabraniIndeks.getStudProgramOznaka()
                );

                List<Integer> obnovljeneGodine = aktivnostiService.getObnovljeneGodine(
                        izabraniIndeks.getGodina(),
                        izabraniIndeks.getBroj(),
                        izabraniIndeks.getStudProgramOznaka()
                );

                return new GodineResult(poslednjaGodina, upisaneGodine, obnovljeneGodine);
            }
        };

        task.setOnSucceeded(e -> {
            GodineResult result = task.getValue();

            // Postavi poslednju godinu
            lblPoslednjaGodina.setText(result.poslednjaGodina != null ?
                    result.poslednjaGodina.toString() : "Nema aktivnosti");

            // Postavi upisane godine
            if (result.upisaneGodine == null || result.upisaneGodine.isEmpty()) {
                lblUpisaneGodine.setText("/");
            } else {
                String upisane = result.upisaneGodine.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                lblUpisaneGodine.setText(upisane);
            }

            // Postavi obnovljene godine
            if (result.obnovljeneGodine == null || result.obnovljeneGodine.isEmpty()) {
                lblObnovljeneGodine.setText("/");
            } else {
                String obnovljene = result.obnovljeneGodine.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                lblObnovljeneGodine.setText(obnovljene);
            }
        });

        task.setOnFailed(e -> {
            lblPoslednjaGodina.setText("Greška");
            lblUpisaneGodine.setText("/");
            lblObnovljeneGodine.setText("/");
            task.getException().printStackTrace();
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    // ✅ POMOCNI DTO za rezultate
    private static class GodineResult {
        Integer poslednjaGodina;
        List<Integer> upisaneGodine;
        List<Integer> obnovljeneGodine;

        GodineResult(Integer poslednjaGodina, List<Integer> upisaneGodine, List<Integer> obnovljeneGodine) {
            this.poslednjaGodina = poslednjaGodina;
            this.upisaneGodine = upisaneGodine;
            this.obnovljeneGodine = obnovljeneGodine;
        }
    }
}