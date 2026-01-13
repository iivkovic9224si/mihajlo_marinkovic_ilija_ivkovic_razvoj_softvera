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
            osveziPoslednjuGodinuAsync();

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
        String indeksGodinaBroj = indeksGodinaTf.getText() + "/" + indeksBrojTf.getText();

        try {
            List<PredmetResponse> predmeti = predmetService.getDostupniPredmeti(godinaUpisa, program, indeksGodinaBroj);

            predmetiTable.getItems().clear();
            predmeti.forEach(p -> predmetiTable.getItems().add(
                    new PredmetSelection(p.getSifra(), p.getNaziv())
            ));

            new Alert(Alert.AlertType.INFORMATION,
                    "Učitano " + predmeti.size() + " predmeta za godine ≤" + godinaUpisa,
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

            osveziPoslednjuGodinuAsync(); // ✅ DODAJ OVDE
        } else {
            lblPoslednjaGodina.setText("Nema indeksa");
        }
    }


    private void osveziPoslednjuGodinuAsync() {
        if (lblPoslednjaGodina == null) return; // ako FXML nije povezan
        if (izabraniIndeks == null) {
            lblPoslednjaGodina.setText("Nema indeksa");
            return;
        }

        lblPoslednjaGodina.setText("Učitavam...");

        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() {
                return aktivnostiService.getPoslednjaGodina(
                        izabraniIndeks.getGodina(),
                        izabraniIndeks.getBroj(),
                        izabraniIndeks.getStudProgramOznaka()
                );
            }
        };

        task.setOnSucceeded(e -> {
            Integer g = task.getValue();
            lblPoslednjaGodina.setText(g != null ? ("" + g) : "Nema aktivnosti");
        });

        task.setOnFailed(e -> {
            lblPoslednjaGodina.setText("Greška pri učitavanju");
            task.getException().printStackTrace();
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
}
