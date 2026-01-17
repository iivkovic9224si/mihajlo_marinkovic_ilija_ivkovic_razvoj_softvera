package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import org.raflab.studsluzbadesktopclient.services.PredmetClientService;
import org.raflab.studsluzbadesktopclient.services.StudProgramClientService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.PredmetRequest;
import request.StudijskiProgramRequest;
import response.PredmetResponse;
import response.StudijskiProgramResponse;

import java.io.InputStream;
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

    @FXML private Label prosekLabel;

    @FXML private TextField sifraTf, nazivTf, espbTf, opisTf;
    @FXML private CheckBox obavezanCb;
    @FXML private TextField godinaOdTf, godinaDoTf;
    @FXML private Spinner<Integer> godinaStudijaSpinner;


    public StudProgramiPredmetiController(
            StudProgramClientService studProgramService,
            PredmetClientService predmetService) {
        this.studProgramService = studProgramService;
        this.predmetService = predmetService;
    }

    @FXML
    public void initialize() {
        // Postavi kolone za TableView studijskih programa
        oznakaCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getOznaka()));
        nazivCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNaziv()));
        godinaCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getGodinaAkreditacije())));

        // Postavi kolone za TableView predmeta
        sifraCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSifra()));
        nazivPredmetCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNaziv()));
        espbCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getEspb())));
        obavezanCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isObavezan() ? "Da" : "Ne"));

        prosekLabel.setText("---");

        studProgramService.getAll()
                .doOnSubscribe(s -> System.out.println("Fetching all studijski programi..."))
                .doOnError(err -> System.out.println("Error fetching studijski programi: " + err))
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(list -> Platform.runLater(() -> {
                    System.out.println("Received studijski programi: " + list.size());
                    studProgramTable.setItems(FXCollections.observableArrayList(list));
                    // ❌ Po default-u NE selektuj ništa
                    studProgramTable.getSelectionModel().clearSelection();
                    selectedProgram = null;
                    predmetTable.setItems(FXCollections.observableArrayList());
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
            predmetTable.setItems(FXCollections.observableArrayList());
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Nije selektovan studijski program");
            alert.setContentText("Morate izabrati studijski program.");
            alert.showAndWait();
            return;
        }

        if (espbTf.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("ESPB polje je prazno!");
            alert.showAndWait();
            espbTf.requestFocus();
            return;
        }

        int espb = Integer.parseInt(espbTf.getText().trim());

        if(espb <= 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("ESPB mora biti veci od 0");
            alert.showAndWait();
            return;
        }

        String sifra = sifraTf.getText();

        if (sifra == null || !sifra.matches("PR\\d{3}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Neispravna šifra");
            alert.setContentText("Šifra mora biti u formatu PR123.");
            alert.showAndWait();
            return;
        }

        PredmetRequest req = new PredmetRequest();
        req.setSifra(sifra);
        req.setNaziv(nazivTf.getText());
        req.setEspb(Integer.parseInt(espbTf.getText()));
        req.setOpis(opisTf.getText());
        req.setObavezan(obavezanCb.isSelected());
        req.setOznakaStudProgram(selectedProgram.getOznaka());
        req.setGodinaStudija(godinaStudijaSpinner.getValue());

        predmetService.save(req)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        v -> Platform.runLater(() -> {
                            loadPredmeti(); // refresh iz baze

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Uspešno");
                            alert.setHeaderText("Predmet sačuvan");
                            alert.showAndWait();
                        }),
                        err -> Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Greška");
                            alert.setHeaderText("Duplikat šifre");
                            alert.setContentText("Predmet sa tom šifrom već postoji.");
                            alert.showAndWait();
                        })
                );
    }


    @FXML
    public void handleProsek() {
        if (selectedPredmet == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Nije selektovan predmet");
            alert.setContentText("Molimo izaberite predmet.");
            alert.showAndWait();
            return;
        }

        if (godinaOdTf.getText().isBlank() || godinaDoTf.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Godine nisu unete");
            alert.setContentText("Molimo unesite godinu od i godinu do.");
            alert.showAndWait();
            return;
        }


        try {
            int od = Integer.parseInt(godinaOdTf.getText());
            int doG = Integer.parseInt(godinaDoTf.getText());

            if (od > doG) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greška");
                alert.setHeaderText("Neispravan opseg godina");
                alert.setContentText("Godina OD ne može biti veća od godine DO.");
                alert.showAndWait();
                return;
            }


            System.out.println("📊 Calculating prosek for: " + selectedPredmet.getNaziv() +
                    " (" + od + " - " + doG + ")");

            predmetService.getProsecnaOcena(selectedPredmet.getSifra(), od, doG)
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe(
                            prosek -> Platform.runLater(() -> {
                                if (prosek == null || prosek == 0.0) {
                                    prosekLabel.setText("---");

                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Nema rezultata");
                                    alert.setHeaderText("Nema ocena");
                                    alert.setContentText("Ne postoje ocene za izabrani period.");
                                    alert.showAndWait();
                                } else {
                                    prosekLabel.setText(String.format("%.2f", prosek));
                                }
                            }),
                            err -> Platform.runLater(() -> {
                                prosekLabel.setText("---");

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Nema rezultata");
                                alert.setHeaderText("Nema ocena");
                                alert.setContentText("Nije moguće izračunati prosek.");
                                alert.showAndWait();
                            })
                    );

        } catch (NumberFormatException e) {
            System.out.println("Greška: Nevalidne godine!");
            prosekLabel.setText("Greška");
        }
    }

    @FXML
    public void handleIzvestaj() {
        if (selectedProgram == null || selectedPredmet == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Izaberite program i predmet");
            alert.showAndWait();
            return;
        }

        new Thread(() -> {
            try {
                int godinaOd = Integer.parseInt(godinaOdTf.getText());
                int godinaDo = Integer.parseInt(godinaDoTf.getText());

                // 1) Prosečna ocena (već postoji)
                Double prosek = predmetService.getProsecnaOcena(selectedPredmet.getSifra(), godinaOd, godinaDo)
                        .block(); // blokirajući poziv

                // 2) Parametri iz selekcije
                Map<String, Object> params = new HashMap<>();
                params.put("pOznakaStudPrograma", selectedProgram.getOznaka());
                params.put("pNazivStudPrograma", selectedProgram.getNaziv());
                params.put("pPredmetSifra", selectedPredmet.getSifra());
                params.put("pPredmetNaziv", selectedPredmet.getNaziv());
                params.put("pGodinaOd", godinaOd);
                params.put("pGodinaDo", godinaDo);
                params.put("pUkupnoEspb", selectedPredmet.getEspb().toString());
                params.put("pProsekPoena", prosek != null ? prosek : 0.0);

                // 3) Jasper
                InputStream jrxml = getClass().getResourceAsStream("/reports/prosecneOcenePredmeta.jrxml");
                if (jrxml == null) {
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "❌ Nije nađen JRXML").showAndWait());
                    return;
                }

                JasperReport report = JasperCompileManager.compileReport(jrxml);
                JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource()); // prazna tabela OK

                String fileName = String.format("prosek_%s_%s_%d-%d.pdf",
                        selectedPredmet.getSifra(), selectedProgram.getOznaka(), godinaOd, godinaDo);
                JasperExportManager.exportReportToPdfFile(print, fileName);

                Platform.runLater(() ->
                        new Alert(Alert.AlertType.INFORMATION, "✅ PDF: " + fileName).showAndWait()
                );

            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() ->
                        new Alert(Alert.AlertType.ERROR, "❌ Greška: " + ex.getMessage()).showAndWait()
                );
            }
        }).start();
    }

}
