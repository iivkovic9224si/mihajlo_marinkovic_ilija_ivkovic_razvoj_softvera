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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;

@Component
public class SearchByIndexController {

    private final StudentService studentService;
    private final ApplicationContext applicationContext; // ← DODAJO SE

    @FXML private TextField indeksTf;

    public SearchByIndexController(StudentService studentService, ApplicationContext applicationContext) {
        this.studentService = studentService;
        this.applicationContext = applicationContext; // ← Spring Context
    }

    @FXML
    public void handleSearchByIndex(ActionEvent event) {
        String indeksInput = indeksTf.getText().trim().toUpperCase();

        // VALIDACIJA
        if (!(indeksInput.matches("[A-Z]{2}\\d+") || indeksInput.matches("[a-z]{2}\\d+"))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogrešan format");
            alert.setHeaderText("Format: oznaka + godina + broj");
            alert.setContentText("RN25100");
            alert.showAndWait();
            indeksTf.selectAll();
            return;
        }


        // ✅ FIX: Koristi specijalni "NOT_FOUND" objekat
        studentService.searchStudentByIndex(indeksInput)
                .onErrorReturn(new StudentIndeksResponse()) // prazni DTO (ne null)
                .defaultIfEmpty(new StudentIndeksResponse()) // prazni DTO
                .subscribe(dto -> Platform.runLater(() -> {

                    // ✅ SVE GREŠKE U JEDNU
                    if (dto.getStudent() == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Student ne postoji");
                        alert.setHeaderText("Student sa indeksom **'" + indeksInput + "' ne postoji**");
                        alert.setContentText("Proveri oznaku, godinu i broj.");
                        alert.showAndWait();
                        indeksTf.selectAll();
                    } else {
                        showStudentDetail(dto.getStudent(), dto);
                    }
                }));
    }


    private void showStudentDetail(StudentPodaciResponse student, StudentIndeksResponse indeks) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentDetailView.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            loader.setClassLoader(getClass().getClassLoader());

            Parent root = loader.load();

            StudentDetailController controller = loader.getController();
            controller.setStudentAndIndex(student, indeks); // BITNO: kao "profil"

            Stage stage = new Stage();
            stage.setTitle("Detalji studenta - " + student.getIme() + " " + student.getPrezime());
            stage.setScene(new Scene(root));

            // DODAJ OVO: Postavi stage u kontroler
            controller.setStage(stage);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Neuspešno otvaranje detalja studenta: " + e.getMessage()).showAndWait();
        }
    }

}
