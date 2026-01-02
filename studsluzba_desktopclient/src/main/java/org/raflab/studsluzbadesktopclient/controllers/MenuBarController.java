package org.raflab.studsluzbadesktopclient.controllers;

import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

@Component
public class MenuBarController {

    private final MainView mainView;
    private final StudentService studentService;

    @FXML
    private MenuBar menuBar;

    public MenuBarController(StudentService studentService, MainView mainView) {
        this.studentService = studentService;
        this.mainView = mainView;
    }

    // ---------------- STUDENTI ----------------
    @FXML
    public void searchByNameSurname() {
        mainView.changeRoot("searchStudent");
    }

    @FXML
    public void searchByHighSchool() {
        mainView.changeRoot("searchByHighSchoolStudent");
    }

    @FXML
    public void searchByIndex() {
        mainView.changeRoot("searchStudentByIndex");
    }

    @FXML
    public void openNewStudent() {
        mainView.openModal("newStudent", "Novi student", 1200, 900);
    }

    @FXML
    public void openStudProgramiPredmeti() {
        mainView.changeRoot("studProgramiPredmeti");
    }

    // ---------------- ISPITI ----------------
    @FXML
    public void openIspitniRokovi() {
        mainView.changeRoot("ispitView");
    }

    // Dodaj back/forward dugmad (ili shortcut)
    @FXML
    private void goBack() { mainView.goBack(); }

    @FXML
    private void goForward() { mainView.goForward(); }
    // ---------------- OSTALO ----------------
    @FXML
    public void initialize() {
        // inicijalizacija ako je potrebna
    }
}
