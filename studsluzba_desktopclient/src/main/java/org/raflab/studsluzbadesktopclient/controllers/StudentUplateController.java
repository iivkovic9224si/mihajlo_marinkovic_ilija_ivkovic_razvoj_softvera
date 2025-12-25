package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.raflab.studsluzbadesktopclient.services.UplataService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.UplataRequest;
import response.StudentPodaciResponse;
import response.UplataResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class StudentUplateController {

    private final UplataService uplataService;
    private StudentPodaciResponse selectedStudent;
    private double trenutniDug = 0.0;

    @FXML private TextField skolskaGodinaTf;
    @FXML private Label dugujeLabel;
    @FXML private DatePicker datumUplateDp;
    @FXML private TextField iznosUplateTf;
    @FXML private TableView<UplataResponse> uplateTable;

    @FXML
    private TableColumn<UplataResponse, LocalDate> datumCol;

    @FXML
    private TableColumn<UplataResponse, Double> iznosRsdCol;

    @FXML
    private TableColumn<UplataResponse, Double> iznosEurCol;

    public StudentUplateController(UplataService uplataService) {
        this.uplataService = uplataService;
    }

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        datumCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getDatumUplate()));
        iznosRsdCol.setCellValueFactory(cell ->
                new SimpleDoubleProperty(cell.getValue().getIznosUDinarima()).asObject());
        iznosEurCol.setCellValueFactory(cell ->
                new SimpleDoubleProperty(cell.getValue().getIznosUEurima()).asObject());

        datumCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });

        iznosRsdCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%.2f RSD", item));
            }
        });

        iznosEurCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%.2f EUR", item));
            }
        });

        // TableView inicijalno prazna
        uplateTable.setItems(FXCollections.observableArrayList());

        // Dug i default vrednosti
        dugujeLabel.setText("--- Čekam studenta...");
        skolskaGodinaTf.setText("2025/2026");
        datumUplateDp.setValue(LocalDate.now());
    }

    public void setSelectedStudent(StudentPodaciResponse student) {
        this.selectedStudent = student;

        if (student != null && student.getJmbg() != null && !student.getJmbg().trim().isEmpty()) {
            handleOsveziDug();
            ucitajIstorijuUplata();
        } else {
            dugujeLabel.setText("---");
        }
    }

    @FXML
    private void handleOsveziDug() {

        if (selectedStudent == null) {
            dugujeLabel.setText("---");
            return;
        }

        String jmbg = selectedStudent.getJmbg();
        if (jmbg == null || jmbg.trim().isEmpty()) {
            dugujeLabel.setText("---");
            return;
        }

        dugujeLabel.setText("Učitavam...");

        uplataService.dugujeZaStudenta(jmbg)
                .doOnSubscribe(s -> System.out.println("🚀 WebClient START"))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        resp -> {
                            System.out.println("✅ RESPONSE: " + resp.getIznosUDinarima());
                            Platform.runLater(() -> {
                                trenutniDug = resp.getIznosUDinarima() != null ? resp.getIznosUDinarima() : 0.0;
                                dugujeLabel.setText(String.format("Dug: %.2f RSD", trenutniDug));
                            });
                        },
                        err -> {
                            System.err.println("💥 ERROR dugujeZaStudenta: " + err.getMessage());
                            Platform.runLater(() -> dugujeLabel.setText("GREŠKA: " + err.getMessage()));
                        }
                );
    }

    private void ucitajIstorijuUplata() {
        if (selectedStudent == null || selectedStudent.getJmbg() == null || selectedStudent.getJmbg().trim().isEmpty()) {
            return;
        }

        String jmbg = selectedStudent.getJmbg();

        uplataService.getUplateZaStudenta(jmbg)
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(list -> Platform.runLater(() -> {
                    uplateTable.setItems(FXCollections.observableArrayList(list));
                }));
    }

    @FXML
    private void handleSacuvajUplatu() {

        if (selectedStudent == null || selectedStudent.getJmbg() == null || selectedStudent.getJmbg().trim().isEmpty()) {
            return;
        }

        LocalDate datum = datumUplateDp.getValue();
        if (datum == null) {
            showAlert(Alert.AlertType.WARNING, "Unesite datum!");
            return;
        }

        String iznosText = iznosUplateTf.getText().trim();
        if (iznosText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Unesite iznos!");
            return;
        }

        try {
            double iznos = Double.parseDouble(iznosText);
            if (iznos <= 0) {
                showAlert(Alert.AlertType.WARNING, "Iznos > 0!");
                return;
            }

            String skolskaGodina = skolskaGodinaTf.getText().trim();
            UplataRequest req = new UplataRequest();
            req.setDatumUplate(datum);
            req.setIznosUDinarima(iznos);
            req.setStudentJmbg(selectedStudent.getJmbg());
            req.setSkolskaGodina(skolskaGodina);


            // OPTIMISTIČKI
            double stariDug = trenutniDug;
            double noviDug = Math.max(0, trenutniDug - iznos);
            trenutniDug = noviDug;
            dugujeLabel.setText(String.format("Dug: %.2f RSD", noviDug));

            // BACKEND
            uplataService.dodajUplatu(req)
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(
                            id -> {
                                System.out.println("✅ UPLATA USPJEŠNA! ID: " + id);
                                Platform.runLater(() -> {
                                    handleOsveziDug();
                                    ucitajIstorijuUplata();
                                });
                            },
                            err -> {
                                System.err.println("💥 UPLATA ERROR: " + err.getMessage());
                                Platform.runLater(() -> {
                                    trenutniDug = stariDug; // Vrati nazad
                                    dugujeLabel.setText(String.format("Dug: %.2f RSD", stariDug));
                                });
                            }
                    );

            iznosUplateTf.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Nevalidan iznos!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Platform.runLater(() -> new Alert(type, message).showAndWait());
    }
}
