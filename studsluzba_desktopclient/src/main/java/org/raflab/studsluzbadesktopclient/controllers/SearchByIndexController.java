package org.raflab.studsluzbadesktopclient.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import response.StudentPodaciResponse;

@Component
public class SearchByIndexController {

    private final StudentService studentService;

    @FXML
    private TextField indeksTf;

    public SearchByIndexController(StudentService studentService) {
        this.studentService = studentService;
    }

    @FXML
    public void handleSearchByIndex(ActionEvent event) {
        String indeks = indeksTf.getText();
        if (indeks == null || indeks.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Unesite broj indeksa!");
            alert.showAndWait();
            return;
        }

        studentService.searchStudentByIndex(indeks)
                .doOnError(ex -> Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Došlo je do greške prilikom pretrage: " + ex.getMessage());
                    alert.showAndWait();
                }))
                .subscribe(studentIndexDTO -> Platform.runLater(() -> {
                    if (studentIndexDTO != null && studentIndexDTO.getStudent() != null) {
                        StudentPodaciResponse student = studentIndexDTO.getStudent();
                        showStudentDetail(student); // otvara novi prozor
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Student sa unetim indeksom nije pronađen!");
                        alert.showAndWait();
                    }
                }));
    }

    private void showStudentDetail(StudentPodaciResponse student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentDetailView.fxml"));
            Parent root = loader.load();

            StudentDetailController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = new Stage();
            stage.setTitle("Detalji studenta");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Neuspešno otvaranje detalja studenta: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
