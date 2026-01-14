package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzbadesktopclient.services.StudentUspehClientService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.StudentIndeksRequest;
import response.PolozeniUspehRowResponse;
import response.PredmetResponse;
import response.StudentPodaciResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class StudentUspehController {

    private final StudentUspehClientService uspehService;
    private StudentIndeksRequest currentStudentIndeks;
    private StudentDetailController parentController;


    @FXML private TableView<PolozeniUspehRowResponse> polozeniPredmetiTable;
    @FXML private TableColumn<PolozeniUspehRowResponse, String> polozeniSifraCol, polozeniNazivCol, polozeniOcenaCol, polozeniPoeniCol, polozeniEspbCol;
    @FXML private TableView<PredmetResponse> nepolozeniPredmetiTable;
    @FXML private TableColumn<PredmetResponse, String> nepolozeniSifraCol, nepolozeniNazivCol, nepolozeniEspbCol;
    @FXML private Label brojPolozenihLabel, brojNepolozenihLabel;
    @FXML private Button refreshBtn;
    @FXML private Label ukupnoEspbLabel;
    @FXML private Label prosecnaOcenaLabel;
    @FXML private Button uverenjePolozeniBtn;

    public StudentUspehController(StudentUspehClientService uspehService) {
        this.uspehService = uspehService;
    }

    @FXML
    public void initialize() {
        setupColumns();
        if (refreshBtn != null) refreshBtn.setOnAction(e -> refreshUspeh());
        if (uverenjePolozeniBtn != null) uverenjePolozeniBtn.setOnAction(e -> generisiUverenjeOPolozenim());
    }

    private void setupColumns() {
        // Položeni (row DTO)
        polozeniSifraCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() != null ? cell.getValue().getSifra() : ""));
        polozeniNazivCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() != null ? cell.getValue().getNaziv() : ""));
        polozeniOcenaCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() != null && cell.getValue().getOcena() != null
                        ? cell.getValue().getOcena().toString()
                        : ""));
        polozeniPoeniCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() != null
                        ? String.format("%.1f", cell.getValue().getPoeni())
                        : ""));
        polozeniEspbCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() != null
                        ? String.valueOf(cell.getValue().getEspb())
                        : ""));

        // Nepoloženi (isto kao pre)
        nepolozeniSifraCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSifra()));
        nepolozeniNazivCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNaziv()));
        nepolozeniEspbCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getEspb())));
    }


    public void loadUspehForStudent(StudentIndeksRequest studentIndeks, StudentDetailController parent) {
        this.currentStudentIndeks = studentIndeks;
        this.parentController = parent;
        refreshUspeh();
    }

    private int page = 0;
    private int size = 10;

    @FXML
    private void refreshUspeh() {
        if (currentStudentIndeks == null) return;

        uspehService.getPolozeniPage(currentStudentIndeks, page, size)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(pr -> Platform.runLater(() -> {
                    polozeniPredmetiTable.setItems(FXCollections.observableArrayList(pr.getContent()));
                    brojPolozenihLabel.setText("(" + pr.getTotalElements() + ")");
                }), err -> System.out.println("❌ Položeni error: " + err.getMessage()));

        uspehService.getNepolozeniPage(currentStudentIndeks, page, size)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(pr -> Platform.runLater(() -> {
                    nepolozeniPredmetiTable.setItems(FXCollections.observableArrayList(pr.getContent()));
                    brojNepolozenihLabel.setText("(" + pr.getTotalElements() + ")");
                }), err -> System.out.println("❌ Nepoloženi error: " + err.getMessage()));

        uspehService.getUkupnoEspbPolozeni(currentStudentIndeks)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(val -> Platform.runLater(() ->
                        ukupnoEspbLabel.setText(String.valueOf(val))
                ), err -> System.out.println("❌ ESPB error: " + err.getMessage()));

        uspehService.getProsecnaOcenaPolozeni(currentStudentIndeks)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(val -> Platform.runLater(() ->
                        prosecnaOcenaLabel.setText(String.format("%.2f", val))
                ), err -> System.out.println("❌ Prosek error: " + err.getMessage()));
    }

    @FXML
    private void generisiUverenjeOPolozenim() {
        if (parentController == null || parentController.getStudent() == null) {
            showAlert(Alert.AlertType.WARNING, "Nema student podataka!");
            return;
        }

        try {
            // 1. Dohvati SVE položene (page 0, size 1000)
            var polozeniPage = uspehService.getPolozeniPage(currentStudentIndeks, 0, 1000).block();
            List<PolozeniUspehRowResponse> polozeniLista = polozeniPage.getContent();

            // 2. Header parametri
            Integer ukupnoEspb = uspehService.getUkupnoEspbPolozeni(currentStudentIndeks).block();
            Double prosek = uspehService.getProsecnaOcenaPolozeni(currentStudentIndeks).block();

            StudentPodaciResponse studentPodaci = parentController.getStudent();

            Map<String, Object> params = new HashMap<>();
            params.put("pIme", studentPodaci.getIme());
            params.put("pPrezime", studentPodaci.getPrezime());
            params.put("pBrojIndeksa", currentStudentIndeks.getBroj() + "/" + currentStudentIndeks.getGodina() + currentStudentIndeks.getStudProgramOznaka());
            params.put("pStudijskiProgram", currentStudentIndeks.getStudProgramOznaka());
            params.put("pDatumIzdavanja", LocalDate.now());
            params.put("pUkupnoEspb", ukupnoEspb);
            params.put("pProsek", prosek);

            // 3. Kompajliraj + puni report
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/uverenje_o_polozenim_predmetima.jrxml"));

            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(polozeniLista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);

            // 4. Sačuvaj PDF
            String fileName = String.format("uverenje_polozeni_%d_%d_%s.pdf",
                    currentStudentIndeks.getBroj(), currentStudentIndeks.getGodina(), currentStudentIndeks.getStudProgramOznaka());
            JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);

            showAlert(Alert.AlertType.INFORMATION, "✅ Uverenje sačuvano: " + fileName);

        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "❌ Greška: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String content) {
        new Alert(type, content).showAndWait();
    }

}
