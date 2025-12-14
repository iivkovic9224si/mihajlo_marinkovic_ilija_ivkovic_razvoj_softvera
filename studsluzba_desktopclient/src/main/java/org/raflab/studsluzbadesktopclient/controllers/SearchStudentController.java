package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.raflab.studsluzbadesktopclient.dtos.PageResultDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SearchStudentController {

    private final StudentService studentService;
    private int currentPage = 0;
    private int pageSize = 15;
    private int totalPages = 1;

    @FXML
    private TextField imeStudentaTf;

    @FXML
    private TextField prezimeStudentaTf;

    @FXML
    private TableView<StudentDTO> tabelaStudenti;

    @FXML
    private Button prethodnaBtn;

    @FXML
    private Button sledecaBtn;

    @FXML
    private Label pageInfoLabel;

    public SearchStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @FXML
    public void initialize() {
        updateButtons();

        tabelaStudenti.setRowFactory(tv -> {
            javafx.scene.control.TableRow<StudentDTO> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    StudentDTO student = row.getItem();
                    showIndexSelection(student);  // poziv metode
                }
            });
            return row;
        });
    }

    private void showIndexSelection(StudentDTO student) {
        List<String> indeksi = studentService.getIndeksiForStudent(student.getId());

        if(indeksi.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Indeksi");
            alert.setHeaderText(null);
            alert.setContentText("Student nema nijedan indeks.");
            alert.showAndWait();
            return;
        }

        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(indeksi.get(0), indeksi);
        dialog.setTitle("Odabir indeksa");
        dialog.setHeaderText("Student: " + student.getIme() + " " + student.getPrezime());
        dialog.setContentText("Izaberite indeks:");

        java.util.Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            try {
                // Otvaranje novog prozora sa detaljima studenta
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/studentDetailView.fxml"));
                javafx.scene.Parent root = loader.load();

                // Dohvati controller i prosledi studenta
                StudentDetailController controller = loader.getController();
                controller.setStudent(student);

                javafx.stage.Stage stage = new javafx.stage.Stage();
                stage.setTitle("Detalji studenta - " + student.getIme() + " " + student.getPrezime());
                stage.setScene(new javafx.scene.Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleSearchStudent(ActionEvent actionEvent) {
        currentPage = 0; // resetuje stranicu
        loadPage();
    }

    @FXML
    public void handlePrethodna(ActionEvent actionEvent) {
        if(currentPage > 0) {
            currentPage--;
            loadPage();
        }
    }

    @FXML
    public void handleSledeca(ActionEvent actionEvent) {
        if(currentPage < totalPages - 1) {
            currentPage++;
            loadPage();
        }
    }

    private void loadPage() {
        String ime = imeStudentaTf.getText().isEmpty() ? null : imeStudentaTf.getText();
        String prezime = prezimeStudentaTf.getText().isEmpty() ? null : prezimeStudentaTf.getText();

        studentService.searchStudentsPaged(ime, prezime, currentPage, pageSize)
                .doOnError(error -> System.err.println("Greška u Mono: " + error))
                .subscribe(result -> {
                    javafx.application.Platform.runLater(() -> {
                        tabelaStudenti.setItems(FXCollections.observableArrayList(result.getContent()));
                        totalPages = result.getTotalPages();
                        updateButtons();
                    });
                });
    }

    private void updateButtons() {
        prethodnaBtn.setDisable(currentPage == 0);
        sledecaBtn.setDisable(currentPage >= totalPages - 1);
        pageInfoLabel.setText("Strana " + (currentPage + 1) + " od " + totalPages);
    }
}
