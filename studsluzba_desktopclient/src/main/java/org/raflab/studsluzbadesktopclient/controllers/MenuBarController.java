package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;

@Component
public class MenuBarController {

    private final MainView mainView;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button backBtn;

    @FXML
    private Button forwardBtn;

    public MenuBarController(StudentService studentService, MainView mainView) {
        this.mainView = mainView;
    }

    // ---------------- STUDENTI ----------------
    @FXML
    public void searchByNameSurname() {
        mainView.changeRoot("searchStudent");
        refreshHistoryButtons();
    }

    @FXML
    public void searchByHighSchool() {
        mainView.changeRoot("searchByHighSchoolStudent");
        refreshHistoryButtons();
    }

    @FXML
    public void searchByIndex() {
        mainView.changeRoot("searchStudentByIndex");
        refreshHistoryButtons();
    }

    @FXML
    public void openNewStudent() {
        mainView.changeRoot("newStudent");
    }

    @FXML
    public void openStudProgramiPredmeti() {
        mainView.changeRoot("studProgramiPredmeti");
        refreshHistoryButtons();
    }

    // ---------------- ISPITI ----------------
    @FXML
    public void openIspitniRokovi() {
        mainView.changeRoot("ispitView");
        refreshHistoryButtons();
    }

    // ---------------- NAVIGACIJA ----------------
    @FXML
    private void goBack() {
        mainView.goBack();
        refreshHistoryButtons();
        clearFocus();
    }

    @FXML
    private void goForward() {
        mainView.goForward();
        refreshHistoryButtons();
        clearFocus();
    }

    @FXML
    public void initialize() {
        refreshHistoryButtons(); // na startu sve DISABLED
    }

    public void refreshHistoryButtons() {
        backBtn.setDisable(!mainView.canGoBack());
        forwardBtn.setDisable(!mainView.canGoForward());
    }

    private void clearFocus() {
        menuBar.requestFocus(); // skida fokus sa dugmeta
    }
}
