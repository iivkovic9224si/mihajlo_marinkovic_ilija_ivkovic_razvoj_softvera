package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.services.StudentUspehClientService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.StudentIndeksRequest;
import response.PolozeniUspehRowResponse;
import response.PredmetResponse;


@Component
public class StudentUspehController {

    private final StudentUspehClientService uspehService;
    private StudentIndeksRequest currentStudentIndeks;

    @FXML private TableView<PolozeniUspehRowResponse> polozeniPredmetiTable;
    @FXML private TableColumn<PolozeniUspehRowResponse, String> polozeniSifraCol, polozeniNazivCol, polozeniOcenaCol, polozeniPoeniCol, polozeniEspbCol;
    @FXML private TableView<PredmetResponse> nepolozeniPredmetiTable;
    @FXML private TableColumn<PredmetResponse, String> nepolozeniSifraCol, nepolozeniNazivCol, nepolozeniEspbCol;
    @FXML private Label brojPolozenihLabel, brojNepolozenihLabel;
    @FXML private Button refreshBtn;
    @FXML private Label ukupnoEspbLabel;
    @FXML private Label prosecnaOcenaLabel;


    public StudentUspehController(StudentUspehClientService uspehService) {
        this.uspehService = uspehService;
    }

    @FXML
    public void initialize() {
        setupColumns();
        if (refreshBtn != null) refreshBtn.setOnAction(e -> refreshUspeh());
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


    public void loadUspehForStudent(StudentIndeksRequest studentIndeks) {
        this.currentStudentIndeks = studentIndeks;
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
}
