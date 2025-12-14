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

import org.raflab.studsluzbadesktopclient.dtos.PageResultDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchByHighSchoolController {

    private final StudentService studentService;

    private int currentPage = 0;
    private int pageSize = 15;
    private int totalPages = 1;

    @FXML
    private TextField srednjaSkolaSifraTf;

    @FXML
    private TableView<StudentDTO> tabelaStudenti;

    @FXML
    private Button prethodnaBtn;

    @FXML
    private Button sledecaBtn;

    @FXML
    private Label pageInfoLabel;

    public SearchByHighSchoolController(StudentService studentService) {
        this.studentService = studentService;
    }

    @FXML
    public void initialize() {
        updateButtons();

        tabelaStudenti.setRowFactory(tv -> {
            TableRow<StudentDTO> row = new TableRow<>();
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

    private void updateTable(PageResultDTO<StudentDTO> result) {
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

    // 👇 OSTALO OSTAVLJENO KAKO JE VEĆ RADILO
    private void showIndexSelection(StudentDTO student) {
        List<String> indeksi = studentService.getIndeksiForStudent(student.getId());

        if (indeksi.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Indeksi");
            alert.setHeaderText(null);
            alert.setContentText("Student nema nijedan indeks.");
            alert.showAndWait();
            return;
        }

        ChoiceDialog<String> dialog =
                new ChoiceDialog<>(indeksi.get(0), indeksi);

        dialog.setTitle("Odabir indeksa");
        dialog.setHeaderText(
                "Student: " + student.getIme() + " " + student.getPrezime()
        );
        dialog.setContentText("Izaberite indeks:");

        dialog.showAndWait().ifPresent(selected -> {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/fxml/studentDetailView.fxml"));
                Parent root = loader.load();

                StudentDetailController controller = loader.getController();
                controller.setStudent(student);

                Stage stage = new Stage();
                stage.setTitle("Detalji studenta");
                stage.setScene(new javafx.scene.Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
