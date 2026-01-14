package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzbadesktopclient.services.StudentUspehClientService;
import pomocniDTO.UverenjeData;
import request.StudentIndeksRequest;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Data;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
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

    private StudentPodaciResponse student;
    private StudentIndeksResponse studentIndex;

    @FXML
    private VBox uplateInclude;   // root node (opciono)

    @FXML
    private StudentUplateController uplateIncludeController;

    @FXML
    private StudentUspehController uspehIncludeController;

    @FXML private StudentIspitiController ispitiIncludeController;

    @FXML private Button generisiUverenjeBtn;

    @Autowired
    private StudentService studentService;

    private Map<String, Node> polja = new HashMap<>();
    private ToggleGroup polGroup;

    public StudentDetailController() {}

    @FXML
    public void initialize() {
        if (generisiUverenjeBtn != null) {
            generisiUverenjeBtn.setOnAction(e -> generisiUverenje());
        }
    }

    public void setStudentAndIndex(StudentPodaciResponse student, StudentIndeksResponse index) {
        this.student = student;
        this.studentIndex = index;

        popuniLicnePodatke();

        if (studentAktivnostiController != null) {
            studentAktivnostiController.setStudent(student);
            studentAktivnostiController.setIzabraniIndeks(index);
        }

        if (uplateIncludeController != null) {
            uplateIncludeController.setSelectedStudent(student);
        }

        if (uspehIncludeController != null && index != null) {
            StudentIndeksRequest req = new StudentIndeksRequest();
            req.setGodina(index.getGodina());
            req.setBroj(index.getBroj());
            req.setStudProgramOznaka(index.getStudProgramOznaka());
            uspehIncludeController.loadUspehForStudent(req, this);
        }

        if (ispitiIncludeController != null) {
            ispitiIncludeController.setStudentAndIndex(student, index);
        }
    }


    // ostaje ako negde koristiš samo studenta
    public void setStudent(StudentPodaciResponse student) {
        this.student = student;
        popuniLicnePodatke();

        if (studentAktivnostiController != null) {
            studentAktivnostiController.setStudent(student);
        }
    }

    // =========================================================

    private void popuniLicnePodatke() {
        if (student == null) return;

        osnovniPodaciGrid.getChildren().clear();
        kontaktPodaciGrid.getChildren().clear();
        polja.clear();

        int row = 0;

        addRowEditable(osnovniPodaciGrid, "Ime:", student.getIme(), row++);
        addRowEditable(osnovniPodaciGrid, "Prezime:", student.getPrezime(), row++);
        addRowEditable(osnovniPodaciGrid, "Srednje ime:", student.getSrednjeIme(), row++);
        addRowEditable(osnovniPodaciGrid, "JMBG:", student.getJmbg(), row++);

        DatePicker datumPicker = new DatePicker(student.getDatumRodjenja());
        addRowControl(osnovniPodaciGrid, "Datum rođenja:", datumPicker, row++);

        addRowEditable(osnovniPodaciGrid, "Mesto rođenja:", student.getMestoRodjenja(), row++);

        polGroup = new ToggleGroup();
        RadioButton m = new RadioButton("M");
        RadioButton f = new RadioButton("F");
        m.setToggleGroup(polGroup);
        f.setToggleGroup(polGroup);

        if (student.getPol() != null) {
            if (student.getPol() == 'M') m.setSelected(true);
            else f.setSelected(true);
        }

        addRowControl(osnovniPodaciGrid, "Pol:", new HBox(10, m, f), row++);

        addRowEditable(osnovniPodaciGrid, "Broj lične karte:", student.getBrojLicneKarte(), row++);
        addRowEditable(osnovniPodaciGrid, "Licnu kartu izdao:", student.getLicnuKartuIzdao(), row++);

        row = 0;
        addRowEditable(kontaktPodaciGrid, "Mesto prebivališta:", student.getMestoPrebivalista(), row++);
        addRowEditable(kontaktPodaciGrid, "Adresa:", student.getAdresa(), row++);
        addRowEditable(kontaktPodaciGrid, "Mobilni:", student.getBrojTelefonaMobilni(), row++);
        addRowEditable(kontaktPodaciGrid, "Fiksni:", student.getBrojTelefonaFiksni(), row++);
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

        studentService.update(student.getId(), student);

        new Alert(Alert.AlertType.INFORMATION, "Student sačuvan").showAndWait();
    }

    private void generisiUverenje() {
        if (student == null || studentIndex == null) {
            new Alert(Alert.AlertType.WARNING, "Odaberi studenta i indeks!").showAndWait();
            return;
        }

        try {
            UverenjeData data = studentService.getUverenjeData(student, studentIndex);

            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/uverenje_o_studiranju.jrxml"));

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(List.of(data));

            JasperPrint jp = JasperFillManager.fillReport(report, new HashMap<>(), ds);

            String fileName = "uverenje_" + student.getJmbg() + "_" + studentIndex.getBroj() + "_" + studentIndex.getGodina() + ".pdf";
            JasperExportManager.exportReportToPdfFile(jp, fileName);

            new Alert(Alert.AlertType.INFORMATION, "✅ Uverenje sačuvano: " + fileName).showAndWait();

        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "❌ Greška: " + ex.getMessage()).showAndWait();
            ex.printStackTrace();
        }
    }
}
