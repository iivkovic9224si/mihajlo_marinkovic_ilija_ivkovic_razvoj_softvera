package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzbadesktopclient.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class StudentDetailController {

    @FXML
    private GridPane osnovniPodaciGrid;

    @FXML
    private GridPane kontaktPodaciGrid;

    @FXML
    private Button sacuvajBtn;

    @FXML
    private StudentAktivnostiController studentAktivnostiController;

    private StudentDTO student;

    @Autowired
    private StudentService studentService;

    private Map<String, Node> polja = new HashMap<>();
    private ToggleGroup polGroup;

    public StudentDetailController() {}

    public void setStudent(StudentDTO student) {
        this.student = student;
        popuniLicnePodatke();

        if (studentAktivnostiController != null) {
            studentAktivnostiController.setStudent(student);
        }
    }

    private void popuniLicnePodatke() {
        if (student == null) return;

        osnovniPodaciGrid.getChildren().clear();
        kontaktPodaciGrid.getChildren().clear();
        polja.clear();

        int row = 0;

        // --- Osnovni podaci ---
        addRowEditable(osnovniPodaciGrid, "Ime:", student.getIme(), row++);
        addRowEditable(osnovniPodaciGrid, "Prezime:", student.getPrezime(), row++);
        addRowEditable(osnovniPodaciGrid, "Srednje ime:", student.getSrednjeIme(), row++);
        addRowEditable(osnovniPodaciGrid, "JMBG:", student.getJmbg(), row++);

        DatePicker datumPicker = new DatePicker();
        if (student.getDatumRodjenja() != null) datumPicker.setValue(student.getDatumRodjenja());
        addRowControl(osnovniPodaciGrid, "Datum rođenja:", datumPicker, row++);

        addRowEditable(osnovniPodaciGrid, "Mesto rođenja:", student.getMestoRodjenja(), row++);

        // Pol
        polGroup = new ToggleGroup();
        RadioButton mButton = new RadioButton("M");
        RadioButton fButton = new RadioButton("F");
        mButton.setToggleGroup(polGroup);
        fButton.setToggleGroup(polGroup);
        if (student.getPol() != null) {
            if (student.getPol() == 'M') mButton.setSelected(true);
            else if (student.getPol() == 'F') fButton.setSelected(true);
        }
        HBox polBox = new HBox(10, mButton, fButton);
        addRowControl(osnovniPodaciGrid, "Pol:", polBox, row++);

        // Broj lične karte i ko je izdao
        addRowEditable(osnovniPodaciGrid, "Broj lične karte:", student.getBrojLicneKarte(), row++);
        addRowEditable(osnovniPodaciGrid, "Licnu kartu izdao:", student.getLicnuKartuIzdao(), row++);

        // --- Kontakt i adresa ---
        row = 0;
        addRowEditable(kontaktPodaciGrid, "Mesto prebivališta:", student.getMestoPrebivalista(), row++);
        addRowEditable(kontaktPodaciGrid, "Adresa:", student.getAdresa(), row++);
        addRowEditable(kontaktPodaciGrid, "Broj telefona (mobilni):", student.getBrojTelefonaMobilni(), row++);
        addRowEditable(kontaktPodaciGrid, "Broj telefona (fiksni):", student.getBrojTelefonaFiksni(), row++);
        addRowEditable(kontaktPodaciGrid, "Email:", student.getEmail(), row++);
        addRowEditable(kontaktPodaciGrid, "Mesto stanovanja:", student.getMestoStanovanja(), row++);
        addRowEditable(kontaktPodaciGrid, "Adresa stanovanja:", student.getAdresaStanovanja(), row++);

        sacuvajBtn.setOnAction(e -> sacuvajPromene());
    }

    private void addRowEditable(GridPane grid, String label, String value, int row) {
        Label l = new Label(label);
        TextField tf = new TextField(value != null ? value : "");
        grid.add(l, 0, row);
        grid.add(tf, 1, row);
        polja.put(label, tf);
    }

    private void addRowControl(GridPane grid, String label, Node node, int row) {
        Label l = new Label(label);
        grid.add(l, 0, row);
        grid.add(node, 1, row);
        polja.put(label, node);
    }

    private void sacuvajPromene() {
        if (student == null) return;

        student.setIme(((TextField) polja.get("Ime:")).getText());
        student.setPrezime(((TextField) polja.get("Prezime:")).getText());
        student.setSrednjeIme(((TextField) polja.get("Srednje ime:")).getText());
        student.setJmbg(((TextField) polja.get("JMBG:")).getText());
        student.setDatumRodjenja(((DatePicker) polja.get("Datum rođenja:")).getValue());

        RadioButton selected = (RadioButton) polGroup.getSelectedToggle();
        if (selected != null) student.setPol(selected.getText().charAt(0));

        student.setMestoRodjenja(((TextField) polja.get("Mesto rođenja:")).getText());
        student.setBrojLicneKarte(((TextField) polja.get("Broj lične karte:")).getText());
        student.setLicnuKartuIzdao(((TextField) polja.get("Licnu kartu izdao:")).getText());

        student.setMestoPrebivalista(((TextField) polja.get("Mesto prebivališta:")).getText());
        student.setAdresa(((TextField) polja.get("Adresa:")).getText());
        student.setBrojTelefonaMobilni(((TextField) polja.get("Broj telefona (mobilni):")).getText());
        student.setBrojTelefonaFiksni(((TextField) polja.get("Broj telefona (fiksni):")).getText());
        student.setEmail(((TextField) polja.get("Email:")).getText());
        student.setMestoStanovanja(((TextField) polja.get("Mesto stanovanja:")).getText());
        student.setAdresaStanovanja(((TextField) polja.get("Adresa stanovanja:")).getText());

        // Poziv REST API-ja za update
        int result = studentService.update(student.getId(), student);
        if(result == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Student uspešno sačuvan!", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Došlo je do greške prilikom čuvanja studenta.", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
