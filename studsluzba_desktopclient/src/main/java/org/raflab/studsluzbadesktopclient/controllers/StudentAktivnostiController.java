package org.raflab.studsluzbadesktopclient.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import lombok.Data;
import org.raflab.studsluzbadesktopclient.dtos.DrziPredmetRequest;
import org.raflab.studsluzbadesktopclient.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentIndexDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzbadesktopclient.services.StudentTokAktivnostiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private StudentDTO student;
    private StudentIndexDTO izabraniIndeks;

    @FXML
    private TableView<PredmetSelection> predmetiTable;

    @FXML
    private TableColumn<PredmetSelection, Boolean> selectCol;

    @FXML
    private TableColumn<PredmetSelection, String> sifraCol;

    @FXML
    private TableColumn<PredmetSelection, String> nastavnikCol;

    @Autowired
    private StudentTokAktivnostiService aktivnostiService;

    public StudentAktivnostiController() {}

    @FXML
    private void initialize() {
        tipAktivnostiCb.getItems().addAll("UPIS", "OBNOVA");
        godinaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        datumPicker.setValue(LocalDate.now());

        predmetiTable.setEditable(true);
        selectCol.setEditable(true);

        sifraCol.setCellValueFactory(data -> data.getValue().predmetSifraProperty());
        nastavnikCol.setCellValueFactory(data -> data.getValue().nastavnikEmailProperty());
        selectCol.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectCol.setCellFactory(tc -> new CheckBoxTableCell<>());
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
        postaviIzabraniIndeksUI();
    }

    public void setIzabraniIndeks(StudentIndexDTO izabraniIndeks) {
        this.izabraniIndeks = izabraniIndeks;
        postaviIzabraniIndeksUI();
    }

    private void postaviIzabraniIndeksUI() {
        if (izabraniIndeks != null) {
            programTf.setText(izabraniIndeks.getStudProgramOznaka());
            indeksGodinaTf.setText(String.valueOf(izabraniIndeks.getGodina()));
            indeksBrojTf.setText(String.valueOf(izabraniIndeks.getBroj()));
            indeksAktivanCb.setSelected(izabraniIndeks.isAktivan());
        }
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
            StudijskiTokAktivnostiRequest req = new StudijskiTokAktivnostiRequest();
            req.setTip(tip);
            req.setGodina(godinaSpinner.getValue());
            req.setDatum(datumPicker.getValue());
            req.setNapomena(napomenaTa.getText());
            req.setIndeksAktivan(indeksAktivanCb.isSelected());

            // koristimo postojeći indeks
            req.setId(izabraniIndeks.getId());

            List<DrziPredmetRequest> izabraniPredmeti = predmetiTable.getItems().stream()
                    .filter(PredmetSelection::isSelected)
                    .map(p -> {
                        DrziPredmetRequest d = new DrziPredmetRequest();
                        d.setPredmetSifra(p.getPredmetSifra());
                        d.setNastavnikEmail(p.getNastavnikEmail());
                        return d;
                    })
                    .toList();

            int result;
            if ("UPIS".equals(tip)) {
                result = aktivnostiService.upisiNaGodinu(req, izabraniPredmeti);
            } else {
                result = aktivnostiService.obnovaGodine(req, izabraniPredmeti);
            }

            if (result == 1) {
                new Alert(Alert.AlertType.INFORMATION, "Aktivnost uspešno sačuvana.", ButtonType.OK).showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Greška prilikom čuvanja aktivnosti.", ButtonType.OK).showAndWait();
            }

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
}
