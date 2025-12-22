package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;
import request.DrziPredmetRequest;
import request.IspitRequest;
import request.IspitniRokRequest;
import response.IspitResponse;
import response.IspitniRokResponse;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.raflab.studsluzbadesktopclient.services.IspitniRokService;

import java.sql.SQLOutput;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class IspitController {

    private final IspitService ispitService;
    private final IspitniRokService ispitniRokService;

    public IspitController(IspitService ispitService, IspitniRokService ispitniRokService) {
        this.ispitService = ispitService;
        this.ispitniRokService = ispitniRokService;
    }

    // TABOVI
    @FXML private TabPane tabPane;
    @FXML private Tab rokoviTab;
    @FXML private Tab rezultatiTab;

    // ISPITNI ROKOVI
    @FXML private DatePicker rokPocetak;
    @FXML private DatePicker rokKraj;
    @FXML private TableView<IspitniRokResponse> rokoviTable;
    @FXML private TableColumn<IspitniRokResponse, String> colGodina;
    @FXML private TableColumn<IspitniRokResponse, String> colPocetak;
    @FXML private TableColumn<IspitniRokResponse, String> colKraj;

    // ISPITI
    @FXML private DatePicker ispitDatum;
    @FXML private TextField ispitVreme;
    @FXML private TextField trajanjeField;
    @FXML private TextField nastavnikEmail;
    @FXML private TextField sifraPredmeta;
    @FXML private TableView<IspitResponse> ispitiTable;
    @FXML private TableColumn<IspitResponse, String> colSifraPredmeta;
    @FXML private TableColumn<IspitResponse, String> colEmailNastavnika;
    @FXML private TableColumn<IspitResponse, String> colDatum;
    @FXML private TableColumn<IspitResponse, String> colVremePocetka;
    @FXML private TableColumn<IspitResponse, String> colTrajanje;

    // REZULTATI
    @FXML private TableView<?> rezultatiTable;

    // Selekcija trenutnog isp. roka
    private IspitniRokResponse selectedRok;

    // INIT
    @FXML
    private void initialize() {
        // TableView kolone za ispitne rokove
        colGodina.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGodina()));
        colPocetak.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getPocetak() != null ? data.getValue().getPocetak().toString() : ""
        ));
        colKraj.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getKraj() != null ? data.getValue().getKraj().toString() : ""
        ));

        loadIspitniRokovi();

        // Listener za selekciju isp. roka u tabeli
        rokoviTable.getSelectionModel().selectedItemProperty().addListener((obs, oldRok, newRok) -> {
            selectedRok = newRok;
            if (selectedRok != null) {
                loadIspitiByRok(selectedRok.getId());
            } else {
                ispitiTable.getItems().clear();
            }
        });

        // Kolone za ispite
        colSifraPredmeta.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDrziPredmetResponse() != null ?
                        data.getValue().getDrziPredmetResponse().getPredmetSifra() : ""
        ));
        colEmailNastavnika.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDrziPredmetResponse() != null ?
                        data.getValue().getDrziPredmetResponse().getNastavnikEmail() : ""
        ));
        colDatum.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDatumIspita() != null ? data.getValue().getDatumIspita().toString() : ""
        ));
        colVremePocetka.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getVremePocetkaIspita() != null ? data.getValue().getVremePocetkaIspita().toString() : ""
        ));
        colTrajanje.setCellValueFactory(data -> new SimpleStringProperty(
                ""
        ));
    }

    // TAB ACTIONS
    @FXML private void openIspitniRokovi() {
        tabPane.getSelectionModel().select(rokoviTab);
    }

    @FXML private void openRezultati() {
        tabPane.getSelectionModel().select(rezultatiTab);
    }

    // ISPITNI ROKOVI
    @FXML
    private void sacuvajRok() {
        IspitniRokRequest request = new IspitniRokRequest();
        request.setPocetak(rokPocetak.getValue());
        request.setKraj(rokKraj.getValue());
        new Thread(() -> {
            ispitniRokService.save(request);
            Platform.runLater(this::loadIspitniRokovi);
        }).start();
    }

    private void loadIspitniRokovi() {
        new Thread(() -> {
            List<IspitniRokResponse> rokovi = ispitniRokService.findAll();
            ObservableList<IspitniRokResponse> data = FXCollections.observableArrayList(rokovi);
            Platform.runLater(() -> rokoviTable.setItems(data));
        }).start();
    }

    @FXML
    private void sacuvajIspit() {
        if (selectedRok == null) {
            System.out.println("Nije izabran ispitni rok!");
            return;
        }

        IspitRequest request = new IspitRequest();
        request.setDatumIspita(ispitDatum.getValue());
        request.setVremePocetkaIspita(LocalTime.parse(ispitVreme.getText()));
        request.setTrajanjeUMinutama(Integer.parseInt(trajanjeField.getText()));

        DrziPredmetRequest dp = new DrziPredmetRequest();
        dp.setPredmetSifra(sifraPredmeta.getText());
        dp.setNastavnikEmail(nastavnikEmail.getText());
        dp.setSkolskaGodinaNaziv(selectedRok.getGodina());
        request.setDrziPredmetRequest(dp);

        IspitniRokRequest ir = new IspitniRokRequest();
        ir.setId(selectedRok.getId());
        ir.setGodina(selectedRok.getGodina());
        ir.setKraj(selectedRok.getKraj());
        ir.setPocetak(selectedRok.getPocetak());
        request.setIspitniRokRequest(ir);
        new Thread(() -> {
            Optional<IspitResponse> saved = ispitService.save(request);
            Platform.runLater(() -> loadIspitiByRok(selectedRok.getId()));
        }).start();
    }


    private void loadIspitiByRok(Long rokId) {
        new Thread(() -> {
            List<IspitResponse> ispiti = ispitniRokService.findIspitiByRokId(rokId);
            ObservableList<IspitResponse> data = FXCollections.observableArrayList(ispiti);
            Platform.runLater(() -> ispitiTable.setItems(data));
        }).start();
    }
}
