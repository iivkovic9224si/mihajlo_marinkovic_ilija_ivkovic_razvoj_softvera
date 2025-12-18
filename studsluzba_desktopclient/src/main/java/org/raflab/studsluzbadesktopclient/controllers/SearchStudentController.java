package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.context.ApplicationContext;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;


import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SearchStudentController {

    private final StudentService studentService;
    private final ApplicationContext applicationContext;
    private int currentPage = 0;
    private int pageSize = 15;
    private int totalPages = 1;

    @FXML
    private TextField imeStudentaTf;

    @FXML
    private TextField prezimeStudentaTf;

    @FXML
    private TableView<StudentPodaciResponse> tabelaStudenti;

    @FXML
    private Button prethodnaBtn;

    @FXML
    private Button sledecaBtn;

    @FXML
    private Label pageInfoLabel;

    public SearchStudentController(StudentService studentService, ApplicationContext applicationContext) {
        this.studentService = studentService;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        updateButtons();

        tabelaStudenti.setRowFactory(tv -> {
            javafx.scene.control.TableRow<StudentPodaciResponse> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    StudentPodaciResponse student = row.getItem();
                    showIndexSelection(student);  // poziv metode
                }
            });
            return row;
        });
    }

    private void showIndexSelection(StudentPodaciResponse student) {
        // 1. Povuci DTO-ove
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
        dialog.setHeaderText("Student: " + student.getIme() + " " + student.getPrezime());
        dialog.setContentText("Izaberite indeks:");

        java.util.Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentDetailView.fxml"));
                loader.setControllerFactory(applicationContext::getBean);
                Parent root = loader.load();

                StudentDetailController controller = loader.getController();

                // pronađi izabrani indeks DTO po stringu iz dijaloga
                int idx = indeksiZaPrikaz.indexOf(selected);
                StudentIndeksResponse izabraniIndeks = indeksDtoList.get(idx);

                controller.setStudentAndIndex(student, izabraniIndeks);

                Stage stage = new Stage();
                stage.setTitle("Detalji studenta - " + student.getIme() + " " + student.getPrezime());
                stage.setScene(new Scene(root));
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
