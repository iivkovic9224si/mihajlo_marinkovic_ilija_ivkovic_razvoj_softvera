package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import response.DrziPredmetResponse;
import response.IspitResponse;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrijavljeniStudentiZaIspitController {

    private final StudentService studentService;

    public PrijavljeniStudentiZaIspitController(StudentService studentService) {
        this.studentService = studentService;
    }

    @FXML private TableView<StudentIndeksView> tableView;
    @FXML private TableColumn<StudentIndeksView, String> imeCol;
    @FXML private TableColumn<StudentIndeksView, String> prezimeCol;
    @FXML private TableColumn<StudentIndeksView, String> indeksCol;

    private IspitResponse ispit;

    public void postaviIspit(IspitResponse ispit) {
        this.ispit = ispit;
        loadData();
    }

    private void loadData() {
        Platform.runLater(() -> {
            List<StudentIndeksResponse> studenti = studentService.getPrijavljeniStudentiZaIspit(ispit);

            // mapiranje indeksa -> StudentIndeksView
            List<StudentIndeksView> viewList = studenti.stream().map(s -> {
                // formiraj oznaku za fast search
                String oznakaSmera = s.getStudProgramOznaka().toUpperCase();
                String godina = String.valueOf(s.getGodina() % 100);
                String indeksShort = oznakaSmera + godina + s.getBroj();

                // pozovi fast search da dobiješ StudentPodaciResponse
                StudentPodaciResponse podaci = studentService
                        .searchStudentByIndex(indeksShort)
                        .block().getStudent(); // block() jer je Mono, jer smo u FX thread-u

                String ime = podaci != null ? podaci.getIme() : "";
                String prezime = podaci != null ? podaci.getPrezime() : "";
                String indeks = s.getBroj() + "/" + godina + oznakaSmera;

                return new StudentIndeksView(ime, prezime, indeks);
            }).collect(Collectors.toList());

            imeCol.setCellValueFactory(new PropertyValueFactory<>("ime"));
            prezimeCol.setCellValueFactory(new PropertyValueFactory<>("prezime"));
            indeksCol.setCellValueFactory(new PropertyValueFactory<>("indeks"));

            tableView.setItems(FXCollections.observableArrayList(viewList));
        });
    }


    public void zatvori(Stage stage) {
        stage.close();
    }

    public static class StudentIndeksView {
        private final String ime;
        private final String prezime;
        private final String indeks;

        public StudentIndeksView(String ime, String prezime, String indeks) {
            this.ime = ime;
            this.prezime = prezime;
            this.indeks = indeks;
        }

        public String getIme() { return ime; }
        public String getPrezime() { return prezime; }
        public String getIndeks() { return indeks; }
    }
}
