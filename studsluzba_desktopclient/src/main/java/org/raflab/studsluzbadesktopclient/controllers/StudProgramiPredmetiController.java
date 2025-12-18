package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.raflab.studsluzbadesktopclient.services.PredmetClientService;
import org.raflab.studsluzbadesktopclient.services.StudProgramClientService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.PredmetRequest;
import request.StudijskiProgramRequest;
import response.PredmetResponse;
import response.StudijskiProgramResponse;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudProgramiPredmetiController {

    private final StudProgramClientService studProgramService;
    private final PredmetClientService predmetService;

    private StudijskiProgramResponse selectedProgram;
    private PredmetResponse selectedPredmet;

    @FXML private TableView<StudijskiProgramResponse> studProgramTable;
    @FXML private TableColumn<StudijskiProgramResponse, String> oznakaCol;
    @FXML private TableColumn<StudijskiProgramResponse, String> nazivCol;
    @FXML private TableColumn<StudijskiProgramResponse, String> godinaCol;

    @FXML private TableView<PredmetResponse> predmetTable;
    @FXML private TableColumn<PredmetResponse, String> sifraCol;
    @FXML private TableColumn<PredmetResponse, String> nazivPredmetCol;
    @FXML private TableColumn<PredmetResponse, String> espbCol;
    @FXML private TableColumn<PredmetResponse, String> obavezanCol;

    @FXML private TableView<Map<String, Object>> prosekTable;
    @FXML private TableColumn<Map<String, Object>, String> prosekNazivCol;
    @FXML private TableColumn<Map<String, Object>, String> prosekCol;

    @FXML private TextField sifraTf, nazivTf, espbTf, opisTf;
    @FXML private CheckBox obavezanCb;
    @FXML private TextField godinaOdTf, godinaDoTf;

    public StudProgramiPredmetiController(
            StudProgramClientService studProgramService,
            PredmetClientService predmetService) {
        this.studProgramService = studProgramService;
        this.predmetService = predmetService;
    }

    @FXML
    public void initialize() {
        System.out.println("Initialize controller...");

        // Postavi kolone za TableView studijskih programa
        oznakaCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOznaka()));
        nazivCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNaziv()));
        godinaCol.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getGodinaAkreditacije()))
        );

        // Postavi kolone za TableView predmeta
        sifraCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSifra()));
        nazivPredmetCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNaziv()));
        espbCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getEspb())));
        obavezanCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().isObavezan() ? "Da" : "Ne")
        );

        // Postavi kolone za tabelu proseka
        prosekNazivCol.setCellValueFactory(cell -> new SimpleStringProperty((String) cell.getValue().get("naziv")));
        prosekCol.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("prosek")))
        );

        studProgramService.getAll()
                .doOnSubscribe(s -> System.out.println("Fetching all studijski programi..."))
                .doOnError(err -> System.out.println("Error fetching studijski programi: " + err))
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(list -> Platform.runLater(() -> {
                    System.out.println("Received studijski programi: " + list.size());
                    studProgramTable.setItems(FXCollections.observableArrayList(list));
                }));

        studProgramTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> {
                    selectedProgram = n;
                    System.out.println("Selected program: " + (n != null ? n.getNaziv() : "null"));
                    loadPredmeti();
                });

        predmetTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> {
                    selectedPredmet = n;
                    System.out.println("Selected predmet: " + (n != null ? n.getNaziv() : "null"));
                });
    }

    private void loadPredmeti() {
        if (selectedProgram == null) {
            System.out.println("No program selected, skipping loadPredmeti.");
            return;
        }

        StudijskiProgramRequest req = new StudijskiProgramRequest();
        req.setOznaka(selectedProgram.getOznaka());
        req.setVrstaStudija(selectedProgram.getVrstaStudija());

        System.out.println("Fetching predmeti for program: " + selectedProgram.getNaziv());

        studProgramService.getPredmeti(req)
                .doOnSubscribe(s -> System.out.println("Started fetching predmeti..."))
                .doOnError(err -> System.out.println("Error fetching predmeti: " + err))
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(list -> Platform.runLater(() -> {
                    System.out.println("Received predmeti: " + list.size());
                    predmetTable.setItems(FXCollections.observableArrayList(list));
                }));
    }

    @FXML
    public void handleSavePredmet() {
        if (selectedProgram == null) {
            System.out.println("No program selected, cannot save predmet.");
            return;
        }

        PredmetRequest req = new PredmetRequest();
        req.setSifra(sifraTf.getText());
        req.setNaziv(nazivTf.getText());
        req.setEspb(Integer.parseInt(espbTf.getText()));
        req.setOpis(opisTf.getText());
        req.setObavezan(obavezanCb.isSelected());
        req.setOznakaStudProgram(selectedProgram.getOznaka());

        System.out.println("Saving predmet: " + req.getNaziv());

        predmetService.save(req)
                .doOnError(err -> System.out.println("Error saving predmet: " + err))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(v -> {
                    System.out.println("Predmet saved, reloading predmeti...");
                    loadPredmeti();
                });
    }

    @FXML
    public void handleProsek() {
        if (selectedPredmet == null) {
            System.out.println("No predmet selected, cannot calculate prosek.");
            return;
        }

        int od = Integer.parseInt(godinaOdTf.getText());
        int doG = Integer.parseInt(godinaDoTf.getText());

        System.out.println("Fetching prosek for predmet: " + selectedPredmet.getNaziv());

        predmetService.getProsecnaOcena(selectedPredmet.getSifra(), od, doG)
                .doOnError(err -> System.out.println("Error fetching prosek: " + err))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(prosek -> {
                    System.out.println("Prosek received: " + prosek);
                    Map<String, Object> row = new HashMap<>();
                    row.put("naziv", selectedPredmet.getNaziv());
                    row.put("prosek", prosek);

                    Platform.runLater(() -> prosekTable.setItems(FXCollections.observableArrayList(row)));
                });
    }

    @FXML
    public void handleIzvestaj() {
        System.out.println("Izvestaj clicked.");
    }
}
