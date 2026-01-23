package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.raflab.studsluzbadesktopclient.services.IspitService;
import org.springframework.stereotype.Component;
import response.RezultatiIspitaResponse;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @FXML private javafx.scene.control.Button stampajBtn;

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

        if (stampajBtn != null) stampajBtn.setOnAction(e -> stampajRezultate());
    }


    private void stampajRezultate() {
        if (ispitId == null) {
            new Alert(Alert.AlertType.WARNING, "Nije postavljen ispitId!").showAndWait();
            return;
        }

        new Thread(() -> {
            try {
                // 1) Podaci (tabela)
                var rezultati = ispitService.getRezultati(ispitId);

                // 2) Podaci o ispitu
                var ispitOpt = ispitService.findById(ispitId);
                if (ispitOpt.isEmpty()) {
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Ispit nije pronađen!").showAndWait());
                    return;
                }
                var ispit = ispitOpt.get();

                // 3) Prosek
                Double prosek = ispitService.getProsecnaOcena(ispitId);

                // 4) Parametri (mora da se poklope sa JRXML)
                Map<String, Object> params = new HashMap<>();

                // TODO: prilagodi ove gettere tačno tvojoj DrziPredmetResponse strukturi:
                String nazivPredmeta = "N/A";
                Integer godinaSkolskeGodine = null;

                if (ispit.getDrziPredmetResponse() != null) {
                    nazivPredmeta = ispit.getDrziPredmetResponse().getPredmetSifra(); // OK je šifra

                    String sg = ispit.getDrziPredmetResponse().getSkolskaGodinaNaziv(); // "2024/2025"
                    if (sg != null && !sg.isBlank()) {
                        String first = sg.contains("/") ? sg.substring(0, sg.indexOf('/')) : sg;
                        try { godinaSkolskeGodine = Integer.parseInt(first.trim()); } catch (Exception ignored) {}
                    }
                }

                String rokNaziv = "N/A";
                if (ispit.getIspitniRokResponse() != null) {
                    var rok = ispit.getIspitniRokResponse();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    rokNaziv = rok.getGodina() + " | " + rok.getPocetak().format(df) + " - " + rok.getKraj().format(df);
                }

                String datumIspita = ispit.getDatumIspita().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        + " " + ispit.getVremePocetkaIspita().format(DateTimeFormatter.ofPattern("HH:mm"));

                params.put("pNazivPredmeta", nazivPredmeta);
                params.put("pIspitniRok", rokNaziv);
                params.put("pDatumIspita", datumIspita);
                params.put("pProsecnaOcena", prosek);
                params.put("pBrojUcesnika", rezultati.size());

                // 5) Jasper (compile -> fill -> export + PREVIEW)
                InputStream jrxml = getClass().getResourceAsStream("/reports/rezultati_ispita.jrxml");
                if (jrxml == null) {
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Ne mogu da nađem /reports/rezultati_ispita.jrxml").showAndWait());
                    return;
                }

                JasperReport report = JasperCompileManager.compileReport(jrxml);
                JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(rezultati));

                String fileName = "rezultati_ispita_" + ispitId + ".pdf";
                JasperExportManager.exportReportToPdfFile(print, fileName);

                JasperViewer.viewReport(print, false);

                Platform.runLater(() ->
                        new Alert(Alert.AlertType.INFORMATION, "✅ Rezultati sačuvani i otvoreni: " + fileName).showAndWait()
                );

            } catch (Exception ex) {
                ex.printStackTrace();
                String msg = (ex.getMessage() != null) ? ex.getMessage() : ex.getClass().getSimpleName();
                Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "❌ Greška: " + msg).showAndWait()
                );
            }
        }).start();
    }

}
