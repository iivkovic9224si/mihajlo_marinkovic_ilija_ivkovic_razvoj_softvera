package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import pomocniDTO.PageResultDTO;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;


import java.util.ArrayList;
import java.util.List;

@Component
public class SearchByHighSchoolController {

    private final StudentService studentService;

    private int currentPage = 0;
    private int pageSize = 15;
    private int totalPages = 1;
    private final ApplicationContext applicationContext;


    @FXML
    private TextField srednjaSkolaSifraTf;

    @FXML
    private TableView<StudentPodaciResponse> tabelaStudenti;

    @FXML
    private Button prethodnaBtn;

    @FXML
    private Button sledecaBtn;

    @FXML
    private Label pageInfoLabel;

    private List<StudentPodaciResponse> trenutniRezultati =  new ArrayList<>();

    public SearchByHighSchoolController(StudentService studentService, ApplicationContext applicationContext) {
        this.studentService = studentService;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        updateButtons();

        tabelaStudenti.setRowFactory(tv -> {
            TableRow<StudentPodaciResponse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showIndexSelection(row.getItem());
                }
            });
            return row;
        });
    }

    // 🔎 SEARCH
    @FXML
    public void handleSearchStudent(ActionEvent actionEvent) {
        currentPage = 0;
        loadPage();
    }

    @FXML
    public void handlePrethodna(ActionEvent actionEvent) {
        if (currentPage > 0) {
            currentPage--;
            loadPage();
        }
    }

    @FXML
    public void handleSledeca(ActionEvent actionEvent) {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadPage();
        }
    }

    private void loadPage() {
        String sifra = srednjaSkolaSifraTf.getText();

        if (sifra == null || sifra.isBlank()) {
            tabelaStudenti.setItems(FXCollections.observableArrayList());
            totalPages = 1;
            updateButtons();
            return;
        }

        studentService
                .searchStudentsByHighSchoolPaged(sifra, currentPage, pageSize)
                .subscribe(result -> Platform.runLater(() -> updateTable(result)),
                        error -> error.printStackTrace());
    }

    private void updateTable(pomocniDTO.PageResultDTO<StudentPodaciResponse> result) {
        tabelaStudenti.setItems(
                FXCollections.observableArrayList(result.getContent())
        );

        totalPages = result.getTotalPages();
        updateButtons();
    }

    private void updateButtons() {
        prethodnaBtn.setDisable(currentPage == 0);
        sledecaBtn.setDisable(currentPage >= totalPages - 1);
        pageInfoLabel.setText("Strana " + (currentPage + 1) + " od " + totalPages);
    }


    private void showIndexSelection(StudentPodaciResponse student) {
        // 1. Povuci listu indeks DTO-ova
        List<StudentIndeksResponse> indeksDtoList = studentService.getIndeksiForStudent(student.getId());

        if (indeksDtoList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Indeksi");
            alert.setHeaderText(null);
            alert.setContentText("Student nema nijedan indeks.");
            alert.showAndWait();
            return;
        }

        // 2. Napravi listu stringova za prikaz: broj/godinaSTUDOZNAKA
        List<String> indeksiZaPrikaz = indeksDtoList.stream()
                .map(i -> i.getBroj() + "/" + i.getGodina() + i.getStudProgramOznaka().toUpperCase())
                .toList();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(indeksiZaPrikaz.get(0), indeksiZaPrikaz);
        dialog.setTitle("Odabir indeksa");
        dialog.setHeaderText(
                "Student: " + student.getIme() + " " + student.getPrezime()
        );
        dialog.setContentText("Izaberite indeks:");

        dialog.showAndWait().ifPresent(selected -> {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/fxml/studentDetailView.fxml"));
                loader.setControllerFactory(applicationContext::getBean);
                Parent root = loader.load();

                int idx = indeksiZaPrikaz.indexOf(selected);
                StudentIndeksResponse izabraniIndeks = indeksDtoList.get(idx);

                StudentDetailController controller = loader.getController();
                controller.setStudentAndIndex(student, izabraniIndeks);

                Stage stage = new Stage();
                stage.setTitle("Detalji studenta");
                stage.setScene(new javafx.scene.Scene(root));

                // DODAJ OVO: Postavi stage u kontroler
                controller.setStage(stage);

                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
