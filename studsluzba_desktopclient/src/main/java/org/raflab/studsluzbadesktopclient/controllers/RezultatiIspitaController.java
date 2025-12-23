package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.springframework.stereotype.Component;
import response.RezultatiIspitaResponse;

import java.util.List;

@Component
public class RezultatiIspitaController {

    private final IspitService ispitService;

    public RezultatiIspitaController(IspitService ispitService) {
        this.ispitService = ispitService;
    }

    @FXML private TableView<RezultatiIspitaResponse> rezultatiTable;
    @FXML private TableColumn<RezultatiIspitaResponse, String> colIme;
    @FXML private TableColumn<RezultatiIspitaResponse, String> colPrezime;
    @FXML private TableColumn<RezultatiIspitaResponse, String> colStudProgram;
    @FXML private TableColumn<RezultatiIspitaResponse, String> colGodinaUpisa;
    @FXML private TableColumn<RezultatiIspitaResponse, String> colBrojIndeksa;
    @FXML private TableColumn<RezultatiIspitaResponse, String> colUkupnoPoena;

    private Long ispitId;

    // Ova metoda se poziva iz IspitController-a prilikom otvaranja prozora
    public void setIspitId(Long ispitId) {
        this.ispitId = ispitId;
        loadRezultati();
    }

    private void loadRezultati() {
        new Thread(() -> {
            List<RezultatiIspitaResponse> rezultati = ispitService.getRezultati(ispitId);
            Platform.runLater(() -> {
                rezultatiTable.setItems(FXCollections.observableArrayList(rezultati));
            });
        }).start();
    }

    @FXML
    private void initialize() {
        colIme.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getImeStudenta()));
        colPrezime.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrezimeStudenta()));
        colStudProgram.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStudProgram()));
        colGodinaUpisa.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getGodinaUpisa())));
        colBrojIndeksa.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getBrojIndeksa())));
        colUkupnoPoena.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getUkupnoPoena())));
    }
}
