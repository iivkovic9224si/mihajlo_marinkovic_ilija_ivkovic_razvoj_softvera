package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.raflab.studsluzbadesktopclient.services.IzlazakNaIspitClientService;
import org.raflab.studsluzbadesktopclient.services.PrijavaIspitaClientService;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import request.IzlazakNaIspitRequest;
import request.PrijavaIspitaRequest;
import response.IzlazakNaIspitResponse;
import response.PrijavaIspitaResponse;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StudentIspitiController {

    private final PrijavaIspitaClientService prijavaService;
    private final IzlazakNaIspitClientService izlazakService;

    private StudentPodaciResponse student;
    private StudentIndeksResponse studentIndex;

    @FXML private Label brojPrijavljenihLabel;
    @FXML private Label brojMojihPrijavljeniLabel;

    @FXML private TableView<PrijavaIspitaResponse> dostupniIspitiTable;
    @FXML private TableView<PrijavaIspitaResponse> mojePrijaveTable;

    // Dostupni ispiti
    @FXML private TableColumn<PrijavaIspitaResponse, String> dostupniSifraCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> dostupniPredmetCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> dostupniTerminCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> dostupniStatusCol;
    @FXML private TableColumn<PrijavaIspitaResponse, Void> dostupniPrijaviCol;

    // Moje prijave
    @FXML private TableColumn<PrijavaIspitaResponse, String> mojeSifraCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> mojePredmetCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> mojeTerminCol;
    @FXML private TableColumn<PrijavaIspitaResponse, LocalDate> mojeDatumPrijaveCol;

    // Izlasci
    @FXML private TableView<PrijavaIspitaResponse> prijaveZaIzlazakTable;
    @FXML private TableView<IzlazakNaIspitResponse> mojiIzlasciTable;

    @FXML private Label brojIzlazakaLabel;
    @FXML private Label brojMojihIzlazakaLabel;

    @FXML private HBox prijaveZaIzlazakHeader;
    @FXML private HBox mojiIzlasciHeader;

    // LEVO: prijave za izlazak
    @FXML private TableColumn<PrijavaIspitaResponse, String> izlazakSifraCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> izlazakPredmetCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> izlazakTerminCol;
    @FXML private TableColumn<PrijavaIspitaResponse, String> izlazakStatusCol;
    @FXML private TableColumn<PrijavaIspitaResponse, Void> izlazakEvidentirajCol;

    // DESNO: moji izlasci
    @FXML private TableColumn<IzlazakNaIspitResponse, String> mojiIzlasciSifraCol;
    @FXML private TableColumn<IzlazakNaIspitResponse, String> mojiIzlasciPredmetCol;
    @FXML private TableColumn<IzlazakNaIspitResponse, String> mojiIzlasciTerminCol;
    @FXML private TableColumn<IzlazakNaIspitResponse, String> mojiIzlasciPoeniCol;
    @FXML private TableColumn<IzlazakNaIspitResponse, Boolean> mojiIzlasciPonistenCol;

    public StudentIspitiController(PrijavaIspitaClientService prijavaService,
                                   IzlazakNaIspitClientService izlazakService) {
        this.prijavaService = prijavaService;
        this.izlazakService = izlazakService;
    }

    @FXML
    public void initialize() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // ---------------- Dostupni ispiti ----------------
        dostupniSifraCol.setCellValueFactory(c -> new SimpleStringProperty(getPredmetSifra(c.getValue())));
        dostupniPredmetCol.setCellValueFactory(c -> new SimpleStringProperty(getPredmetNaziv(c.getValue())));
        dostupniTerminCol.setCellValueFactory(c -> new SimpleStringProperty(getIspitTermin(c.getValue(), dateFormatter, timeFormatter)));
        dostupniStatusCol.setCellValueFactory(c -> new SimpleStringProperty("Dostupan"));

        // ✅ BITNO: button kolona treba i cellValueFactory
        dostupniPrijaviCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(null));
        dostupniPrijaviCol.setCellFactory(col -> new PrijaviButtonCell());

        // ---------------- Moje prijave ----------------
        mojeSifraCol.setCellValueFactory(c -> new SimpleStringProperty(getPredmetSifra(c.getValue())));
        mojePredmetCol.setCellValueFactory(c -> new SimpleStringProperty(getPredmetNaziv(c.getValue())));
        mojeTerminCol.setCellValueFactory(c -> new SimpleStringProperty(getIspitTermin(c.getValue(), dateFormatter, timeFormatter)));
        mojeDatumPrijaveCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDatumPrijave()));

        mojeDatumPrijaveCol.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });

        // ---------------- Prijave za izlazak ----------------
        izlazakSifraCol.setCellValueFactory(c -> new SimpleStringProperty(getPredmetSifra(c.getValue())));
        izlazakPredmetCol.setCellValueFactory(c -> new SimpleStringProperty(getPredmetNaziv(c.getValue())));
        izlazakTerminCol.setCellValueFactory(c -> new SimpleStringProperty(getIspitTermin(c.getValue(), dateFormatter, timeFormatter)));
        izlazakStatusCol.setCellValueFactory(c -> new SimpleStringProperty("Može da izađe"));

        // ✅ BITNO: button kolona treba i cellValueFactory
        izlazakEvidentirajCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(null));
        izlazakEvidentirajCol.setCellFactory(col -> new EvidentirajButtonCell());

        // ---------------- Moji izlasci ----------------
        mojiIzlasciSifraCol.setCellValueFactory(c ->
                new SimpleStringProperty(getPredmetSifra(c.getValue().getPrijavaIspita())));
        mojiIzlasciPredmetCol.setCellValueFactory(c ->
                new SimpleStringProperty(getPredmetNaziv(c.getValue().getPrijavaIspita())));
        mojiIzlasciTerminCol.setCellValueFactory(c ->
                new SimpleStringProperty(getIspitTermin(c.getValue().getPrijavaIspita(), dateFormatter, timeFormatter)));

        mojiIzlasciPoeniCol.setCellValueFactory(c -> {
            double ukupno = c.getValue().getBrojPoenaSaIspita() + c.getValue().getBrojPredispitnihPoena();
            return new SimpleStringProperty(String.valueOf(ukupno));
        });

        mojiIzlasciPonistenCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().isPonistava()));

        // Inicijalno prazne tabele
        dostupniIspitiTable.setItems(FXCollections.observableArrayList());
        mojePrijaveTable.setItems(FXCollections.observableArrayList());
        prijaveZaIzlazakTable.setItems(FXCollections.observableArrayList());
        mojiIzlasciTable.setItems(FXCollections.observableArrayList());

        brojPrijavljenihLabel.setText("0");
        brojMojihPrijavljeniLabel.setText("(0)");
        brojIzlazakaLabel.setText("0");
        brojMojihIzlazakaLabel.setText("(0)");
    }

    public void setStudentAndIndex(StudentPodaciResponse student, StudentIndeksResponse index) {
        this.student = student;
        this.studentIndex = index;

        if (student != null && index != null) {
            osveziIspite();
        } else {
            dostupniIspitiTable.setItems(FXCollections.observableArrayList());
            mojePrijaveTable.setItems(FXCollections.observableArrayList());
            prijaveZaIzlazakTable.setItems(FXCollections.observableArrayList());
            mojiIzlasciTable.setItems(FXCollections.observableArrayList());
            brojPrijavljenihLabel.setText("0");
            brojMojihPrijavljeniLabel.setText("(0)");
            brojIzlazakaLabel.setText("0");
            brojMojihIzlazakaLabel.setText("(0)");
        }
    }

    @FXML
    private void handleOsvezi() {
        if (studentIndex != null) osveziIspite();
    }

    private void osveziIspite() {
        if (studentIndex == null) return;

        // 1) Moje prijave
        prijavaService.getPrijaveZaStudenta(studentIndex.getGodina(), studentIndex.getBroj(), studentIndex.getStudProgramOznaka())
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(mojePrijave -> Platform.runLater(() -> {
                    mojePrijaveTable.setItems(FXCollections.observableArrayList(mojePrijave));
                    brojMojihPrijavljeniLabel.setText("(" + mojePrijave.size() + ")");
                    brojPrijavljenihLabel.setText(String.valueOf(mojePrijave.size()));
                }), err -> Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Greška pri učitavanju mojih prijava: " + err.getMessage())));

        // 2) Aktivni dostupni ispiti
        prijavaService.getAktivniDostupniIspiti(studentIndex.getGodina(), studentIndex.getBroj(), studentIndex.getStudProgramOznaka())
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(dostupni -> Platform.runLater(() ->
                        dostupniIspitiTable.setItems(FXCollections.observableArrayList(dostupni))
                ), err -> Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Greška pri učitavanju dostupnih ispita: " + err.getMessage())));

        // 3) Prijave za izlazak (nemaju izlazak)
        izlazakService.getPrijaveZaIzlazak(studentIndex.getGodina(), studentIndex.getBroj(), studentIndex.getStudProgramOznaka())
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(prijaveZaIzlazak -> Platform.runLater(() ->
                        prijaveZaIzlazakTable.setItems(FXCollections.observableArrayList(prijaveZaIzlazak))
                ), err -> Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Greška pri učitavanju prijava za izlazak: " + err.getMessage())));

        // 4) Moji izlasci
        izlazakService.getIzlasciZaStudenta(studentIndex.getGodina(), studentIndex.getBroj(), studentIndex.getStudProgramOznaka())
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(mojiIzlasci -> Platform.runLater(() -> {
                    mojiIzlasciTable.setItems(FXCollections.observableArrayList(mojiIzlasci));
                    brojIzlazakaLabel.setText(String.valueOf(mojiIzlasci.size()));
                    brojMojihIzlazakaLabel.setText("(" + mojiIzlasci.size() + ")");
                }), err -> Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Greška pri učitavanju izlazaka: " + err.getMessage())));
    }

    // ---------------- Helper metode ----------------

    private String getPredmetSifra(PrijavaIspitaResponse prijava) {
        try {
            return prijava.getSlusaPredmetResponse() != null ? prijava.getSlusaPredmetResponse().getPredmet() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private String getPredmetNaziv(PrijavaIspitaResponse prijava) {
        try {
            return prijava.getSlusaPredmetResponse() != null ? prijava.getSlusaPredmetResponse().getPredmet() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private String getIspitTermin(PrijavaIspitaResponse prijava, DateTimeFormatter dateFormatter, DateTimeFormatter timeFormatter) {
        try {
            var ispit = prijava.getIspitResponse();
            if (ispit != null && ispit.getDatumIspita() != null && ispit.getVremePocetkaIspita() != null) {
                LocalDateTime dt = LocalDateTime.of(ispit.getDatumIspita(), ispit.getVremePocetkaIspita());
                return dt.format(dateFormatter) + " " + ispit.getVremePocetkaIspita().format(timeFormatter);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    // ---------------- Button cells ----------------

    private class PrijaviButtonCell extends TableCell<PrijavaIspitaResponse, Void> {
        private final Button button = new Button("Prijavi");

        PrijaviButtonCell() {
            button.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 11px;");
            button.setOnAction(e -> {
                PrijavaIspitaResponse ispitPrijava = getTableView().getItems().get(getIndex());
                prijavaNaIspit(ispitPrijava);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : button);
        }
    }

    private class EvidentirajButtonCell extends TableCell<PrijavaIspitaResponse, Void> {
        private final Button button = new Button("Evidentiraj");

        EvidentirajButtonCell() {
            button.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 11px;");
            button.setOnAction(e -> {
                PrijavaIspitaResponse prijavaZaIzlazak = getTableView().getItems().get(getIndex());
                evidentirajIzlazak(prijavaZaIzlazak);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : button);
        }
    }

    // ---------------- Akcije ----------------

    private void prijavaNaIspit(PrijavaIspitaResponse ispitPrijava) {
        if (studentIndex == null) {
            showAlert(Alert.AlertType.WARNING, "Nema izabranog indeksa!");
            return;
        }

        PrijavaIspitaRequest request = new PrijavaIspitaRequest(
                0L,
                LocalDate.now(),
                ispitPrijava.getIspitResponse().getId(),
                studentIndex.getGodina(),
                studentIndex.getBroj(),
                studentIndex.getStudProgramOznaka()
        );

        prijavaService.sacuvajPrijavu(request)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(res -> Platform.runLater(() -> {
                    osveziIspite();
                    showAlert(Alert.AlertType.INFORMATION, "Uspešna prijava na ispit!");
                }))
                .doOnError(err -> Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Greška pri prijavi: " + err.getMessage())))
                .subscribe();
    }

    private void evidentirajIzlazak(PrijavaIspitaResponse prijavaZaIzlazak) {
        if (prijavaZaIzlazak == null || prijavaZaIzlazak.getId() == 0l) {
            showAlert(Alert.AlertType.WARNING, "Neispravna prijava (nema ID)!");
            return;
        }

        // Minimalan UI: pitaj korisnika za poene
        TextInputDialog dialogPred = new TextInputDialog("0");
        dialogPred.setTitle("Evidentiraj izlazak");
        dialogPred.setHeaderText("Unesi predispitne poene (0-100)");
        dialogPred.setContentText("Predispitni poeni:");
        var predOpt = dialogPred.showAndWait();
        if (predOpt.isEmpty()) return;

        TextInputDialog dialogIspit = new TextInputDialog("0");
        dialogIspit.setTitle("Evidentiraj izlazak");
        dialogIspit.setHeaderText("Unesi poene sa ispita (0-100)");
        dialogIspit.setContentText("Poeni sa ispita:");
        var ispitOpt = dialogIspit.showAndWait();
        if (ispitOpt.isEmpty()) return;

        double predispitni;
        double poeniSaIspita;
        try {
            predispitni = Double.parseDouble(predOpt.get().trim());
            poeniSaIspita = Double.parseDouble(ispitOpt.get().trim());
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Poeni moraju biti broj.");
            return;
        }

        IzlazakNaIspitRequest req = new IzlazakNaIspitRequest();
        req.setId(null); // save
        req.setPrijavaIspitaID(prijavaZaIzlazak.getId());
        req.setBrojPredispitnihPoena(predispitni);
        req.setBrojPoenaSaIspita(poeniSaIspita);
        req.setNapomena("Evidentirano preko klijenta");
        req.setPonistava(false);

        izlazakService.sacuvajIzlazak(req)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(res -> Platform.runLater(() -> {
                    osveziIspite();
                    showAlert(Alert.AlertType.INFORMATION, "Izlazak evidentiran!");
                }))
                .doOnError(err -> Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Greška pri evidentiranju: " + err.getMessage())))
                .subscribe();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Platform.runLater(() -> new Alert(type, message).showAndWait());
    }
}
