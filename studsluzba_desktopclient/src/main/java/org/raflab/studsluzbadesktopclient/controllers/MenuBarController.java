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

    @FXML
    public void searchByNameSurname() {
        System.out.println("Pretraga po imenu/prezimenu");
        mainView.changeRoot("searchStudent");
    }

    @FXML
    public void searchByHighSchool() {
        System.out.println("Pretraga po srednjoj školi");
        mainView.changeRoot("searchByHighSchoolStudent");
    }

    @FXML
    public void searchByIndex() {
        System.out.println("Pretraga po indeksu");
        mainView.changeRoot("searchStudentByIndex");
    }

    @FXML
    public void openNewStudent() {
        mainView.changeRoot("newStudent");
    }

    @FXML
    public void initialize() {
        // dodatna inicijalizacija ako je potrebna
    }
}
