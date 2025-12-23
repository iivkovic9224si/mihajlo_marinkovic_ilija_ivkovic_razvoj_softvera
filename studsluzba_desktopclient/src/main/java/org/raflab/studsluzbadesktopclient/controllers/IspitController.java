package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.ContextFXMLLoader;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.raflab.studsluzbadesktopclient.services.IspitniRokService;
import org.springframework.stereotype.Component;
import request.DrziPredmetRequest;
import request.IspitRequest;
import request.IspitniRokRequest;
import response.IspitResponse;
import response.IspitniRokResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class IspitController {

    private final IspitService ispitService;
    private final IspitniRokService ispitniRokService;
    private final ContextFXMLLoader contextFXMLLoader;

    public IspitController(IspitService ispitService, IspitniRokService ispitniRokService, ContextFXMLLoader contextFXMLLoader) {
        this.ispitService = ispitService;
        this.ispitniRokService = ispitniRokService;
        this.contextFXMLLoader = contextFXMLLoader;
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

    private IspitniRokResponse selectedRok;

    @FXML
    private void initialize() {
        colGodina.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGodina()));
        colPocetak.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPocetak().toString()));
        colKraj.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKraj().toString()));

        loadIspitniRokovi();

        rokoviTable.getSelectionModel().selectedItemProperty().addListener((o, old, rok) -> {
            selectedRok = rok;
            if (rok != null) loadIspitiByRok(rok);
        });

        colSifraPredmeta.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDrziPredmetResponse().getPredmetSifra()));
        colEmailNastavnika.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDrziPredmetResponse().getNastavnikEmail()));
        colDatum.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDatumIspita().toString()));
        colVremePocetka.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getVremePocetkaIspita().toString()));
        colTrajanje.setCellValueFactory(d -> new SimpleStringProperty(""));

        // DVOKLIK: otvaranje prijavljenih studenata
        ispitiTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                IspitResponse ispit = ispitiTable.getSelectionModel().getSelectedItem();
                if (ispit != null) {
                    openPrijavljeniStudentiView(ispit);
                }
            }
        });
    }

    @FXML
    private void sacuvajRok() {
        if (rokPocetak.getValue() == null || rokKraj.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Greška", "Morate uneti i početak i kraj isp. roka.");
            return;
        }

        if (rokPocetak.getValue().isAfter(rokKraj.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Greška", "Početak isp. roka ne može biti posle kraja.");
            return;
        }

        String godina = extractSkolskaGodina(rokPocetak.getValue(), rokKraj.getValue());

        IspitniRokRequest request = new IspitniRokRequest();
        request.setPocetak(rokPocetak.getValue());
        request.setKraj(rokKraj.getValue());
        request.setGodina(godina);

        new Thread(() -> {
            try {
                ispitniRokService.save(request);
                Platform.runLater(() -> {
                    loadIspitniRokovi();
                    showAlert(Alert.AlertType.INFORMATION, "Uspeh", "Ispitni rok je uspešno dodat za školsku godinu: " + godina);
                });
            } catch (Exception e) {
                String message = "Ne moguće dodati ispitni rok u nepostojeću školsku godinu: " + godina;
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Greška", message));
            }
        }).start();
    }

    private String extractSkolskaGodina(LocalDate pocetak, LocalDate kraj) {
        int startYear = pocetak.getYear();
        int endYear = (pocetak.getMonthValue() >= 9) ? startYear + 1 : startYear;
        return startYear + "/" + endYear;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadIspitniRokovi() {
        new Thread(() -> {
            List<IspitniRokResponse> rokovi = ispitniRokService.findAll();
            Platform.runLater(() -> rokoviTable.setItems(FXCollections.observableArrayList(rokovi)));
        }).start();
    }

    @FXML
    private void sacuvajIspit() {
        if (selectedRok == null) {
            showError("Morate izabrati ispitni rok.");
            return;
        }

        LocalDate datum = ispitDatum.getValue();
        if (datum == null || datum.isBefore(selectedRok.getPocetak()) || datum.isAfter(selectedRok.getKraj())) {
            showError("Datum ispita mora biti u okviru izabranog ispitnog roka.");
            return;
        }

        LocalTime vreme;
        try {
            vreme = LocalTime.parse(ispitVreme.getText());
        } catch (Exception e) {
            showError("Vreme mora biti u formatu HH:mm.");
            return;
        }

        int trajanje;
        try {
            trajanje = Integer.parseInt(trajanjeField.getText());
        } catch (Exception e) {
            showError("Trajanje mora biti broj.");
            return;
        }

        IspitRequest request = new IspitRequest();
        request.setDatumIspita(datum);
        request.setVremePocetkaIspita(vreme);
        request.setTrajanjeUMinutama(trajanje);

        DrziPredmetRequest dp = new DrziPredmetRequest();
        dp.setPredmetSifra(sifraPredmeta.getText());
        dp.setNastavnikEmail(nastavnikEmail.getText());
        dp.setSkolskaGodinaNaziv(selectedRok.getGodina());
        request.setDrziPredmetRequest(dp);

        IspitniRokRequest ir = new IspitniRokRequest();
        ir.setId(selectedRok.getId());
        request.setIspitniRokRequest(ir);

        new Thread(() -> {
            Optional<IspitResponse> saved = ispitService.save(request);
            Platform.runLater(() -> {
                if (saved.isEmpty()) {
                    showError("Greška prilikom čuvanja ispita.");
                } else {
                    loadIspitiByRok(selectedRok);
                }
            });
        }).start();
    }

    private void loadIspitiByRok(IspitniRokResponse rok) {
        new Thread(() -> {
            List<IspitResponse> ispiti = ispitniRokService.findIspitiByRokId(rok.getId());
            Platform.runLater(() -> ispitiTable.setItems(FXCollections.observableArrayList(ispiti)));
        }).start();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void openPrijavljeniStudentiView(IspitResponse ispit) {
        try {
            FXMLLoader loader = contextFXMLLoader.getLoader("/fxml/prijavljeniStudentiZaIspitView.fxml");
            Parent root = loader.load();

            PrijavljeniStudentiZaIspitController controller = loader.getController();
            controller.postaviIspit(ispit);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Prijavljeni studenti za ispit");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ne mogu otvoriti prikaz prijavljenih studenata.");
        }
    }
}
