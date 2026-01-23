package org.raflab.studsluzbadesktopclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzbadesktopclient.StudentDetailHistoryManager;
import org.raflab.studsluzbadesktopclient.services.StudentUspehClientService;
import pomocniDTO.UverenjeData;
import request.StudentIndeksRequest;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Data;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class StudentDetailController {

    @FXML
    private GridPane osnovniPodaciGrid;

    @FXML
    private GridPane kontaktPodaciGrid;

    @FXML
    private Button sacuvajBtn;

    @FXML
    private StudentAktivnostiController studentAktivnostiController;

    private StudentPodaciResponse student;
    private StudentIndeksResponse studentIndex;

    @FXML
    private VBox uplateInclude;   // root node (opciono)

    @FXML
    private StudentUplateController uplateIncludeController;

    @FXML
    private StudentUspehController uspehIncludeController;

    @FXML private StudentIspitiController ispitiIncludeController;

    @FXML private Button generisiUverenjeBtn;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button nazadBtn;

    @FXML
    private Button napredBtn;

    @Autowired
    private StudentService studentService;

    private Map<String, Node> polja = new HashMap<>();
    private ToggleGroup polGroup;

    private StudentDetailHistoryManager historyManager;
    private double dragStartX;

    private Stage stage; // Dodajemo referencu na stage

    public StudentDetailController() {
        this.historyManager = new StudentDetailHistoryManager();
    }

    @FXML
    public void initialize() {
        if (generisiUverenjeBtn != null) {
            generisiUverenjeBtn.setOnAction(e -> generisiUverenje());
        }

        if (historyManager != null) {
            historyManager.clear();
        }

        // Prvo ažuriraj dugmad
        updateNavigationButtons();

        // Onda dodaj listener za tab navigaciju
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getContent() != null) {
                String tabName = newTab.getText();
                Parent content = null;

                // Pronađi pravi Parent element za tab
                if (newTab.getContent() instanceof ScrollPane) {
                    ScrollPane scrollPane = (ScrollPane) newTab.getContent();
                    content = (Parent) scrollPane.getContent();
                } else if (newTab.getContent() instanceof Parent) {
                    content = (Parent) newTab.getContent();
                }

                if (content != null) {
                    historyManager.navigate(content, tabName);
                    updateNavigationButtons();
                }
            }
        });

        // Postavi inicijalni state, ali samo ako je tabPane već postavljen
        Tab initialTab = tabPane.getSelectionModel().getSelectedItem();
        if (initialTab != null && initialTab.getContent() != null) {
            // Sačekaj malo da se sve inicijalizuje
            javafx.application.Platform.runLater(() -> {
                Parent initialContent = null;
                if (initialTab.getContent() instanceof ScrollPane) {
                    ScrollPane scrollPane = (ScrollPane) initialTab.getContent();
                    initialContent = (Parent) scrollPane.getContent();
                } else if (initialTab.getContent() instanceof Parent) {
                    initialContent = (Parent) initialTab.getContent();
                }

                if (initialContent != null) {
                    historyManager.navigate(initialContent, initialTab.getText());
                    updateNavigationButtons();
                }
            });
        }
    }

    private void updateNavigationButtons() {
        if (nazadBtn != null) {
            // Proveri da li smo na prvom tabu
            Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
            boolean isFirstTab = currentTab != null && currentTab.getText().equals("Licni Podaci");

            // Ako smo na prvom tabu, dugme treba da bude omogućeno
            // (da možemo da zatvorimo prozor)
            if (isFirstTab) {
                nazadBtn.setDisable(false);
            } else {
                nazadBtn.setDisable(!historyManager.canGoBack());
            }
        }

        if (napredBtn != null) {
            napredBtn.setDisable(!historyManager.canGoForward());
        }
    }

    public void setStudentAndIndex(StudentPodaciResponse student, StudentIndeksResponse index) {
        this.student = student;
        this.studentIndex = index;

        popuniLicnePodatke();

        if (studentAktivnostiController != null) {
            studentAktivnostiController.setStudent(student);
            studentAktivnostiController.setIzabraniIndeks(index);
        }

        if (uplateIncludeController != null) {
            uplateIncludeController.setSelectedStudent(student);
        }

        if (uspehIncludeController != null && index != null) {
            StudentIndeksRequest req = new StudentIndeksRequest();
            req.setGodina(index.getGodina());
            req.setBroj(index.getBroj());
            req.setStudProgramOznaka(index.getStudProgramOznaka());
            uspehIncludeController.loadUspehForStudent(req, this);
        }

        if (ispitiIncludeController != null) {
            ispitiIncludeController.setStudentAndIndex(student, index);
        }
    }


    // ostaje ako negde koristiš samo studenta
    public void setStudent(StudentPodaciResponse student) {
        this.student = student;
        popuniLicnePodatke();

        if (studentAktivnostiController != null) {
            studentAktivnostiController.setStudent(student);
        }
    }

    // =========================================================

    private void popuniLicnePodatke() {
        if (student == null) return;

        osnovniPodaciGrid.getChildren().clear();
        kontaktPodaciGrid.getChildren().clear();
        polja.clear();

        int row = 0;

        addRowEditable(osnovniPodaciGrid, "Ime:", student.getIme(), row++);
        addRowEditable(osnovniPodaciGrid, "Prezime:", student.getPrezime(), row++);
        addRowEditable(osnovniPodaciGrid, "Srednje ime:", student.getSrednjeIme(), row++);
        addRowEditable(osnovniPodaciGrid, "JMBG:", student.getJmbg(), row++);

        DatePicker datumPicker = new DatePicker(student.getDatumRodjenja());
        addRowControl(osnovniPodaciGrid, "Datum rođenja:", datumPicker, row++);

        addRowEditable(osnovniPodaciGrid, "Mesto rođenja:", student.getMestoRodjenja(), row++);

        polGroup = new ToggleGroup();
        RadioButton m = new RadioButton("M");
        RadioButton f = new RadioButton("F");
        m.setToggleGroup(polGroup);
        f.setToggleGroup(polGroup);

        if (student.getPol() != null) {
            if (student.getPol() == 'M') m.setSelected(true);
            else f.setSelected(true);
        }

        addRowControl(osnovniPodaciGrid, "Pol:", new HBox(10, m, f), row++);

        addRowEditable(osnovniPodaciGrid, "Broj lične karte:", student.getBrojLicneKarte(), row++);
        addRowEditable(osnovniPodaciGrid, "Licnu kartu izdao:", student.getLicnuKartuIzdao(), row++);

        row = 0;
        addRowEditable(kontaktPodaciGrid, "Mesto prebivališta:", student.getMestoPrebivalista(), row++);
        addRowEditable(kontaktPodaciGrid, "Adresa:", student.getAdresa(), row++);
        addRowEditable(kontaktPodaciGrid, "Mobilni:", student.getBrojTelefonaMobilni(), row++);
        addRowEditable(kontaktPodaciGrid, "Fiksni:", student.getBrojTelefonaFiksni(), row++);
        addRowEditable(kontaktPodaciGrid, "Email:", student.getEmail(), row++);
        addRowEditable(kontaktPodaciGrid, "Mesto stanovanja:", student.getMestoStanovanja(), row++);
        addRowEditable(kontaktPodaciGrid, "Adresa stanovanja:", student.getAdresaStanovanja(), row++);

        sacuvajBtn.setOnAction(e -> sacuvajPromene());
    }

    private void addRowEditable(GridPane grid, String label, String value, int row) {
        Label l = new Label(label);
        TextField tf = new TextField(value != null ? value : "");
        grid.add(l, 0, row);
        grid.add(tf, 1, row);
        polja.put(label, tf);
    }

    private void addRowControl(GridPane grid, String label, Node node, int row) {
        Label l = new Label(label);
        grid.add(l, 0, row);
        grid.add(node, 1, row);
        polja.put(label, node);
    }

    private void sacuvajPromene() {
        if (student == null) return;

        student.setIme(((TextField) polja.get("Ime:")).getText());
        student.setPrezime(((TextField) polja.get("Prezime:")).getText());
        student.setSrednjeIme(((TextField) polja.get("Srednje ime:")).getText());
        student.setJmbg(((TextField) polja.get("JMBG:")).getText());
        student.setDatumRodjenja(((DatePicker) polja.get("Datum rođenja:")).getValue());

        RadioButton selected = (RadioButton) polGroup.getSelectedToggle();
        if (selected != null) student.setPol(selected.getText().charAt(0));

        studentService.update(student.getId(), student);

        new Alert(Alert.AlertType.INFORMATION, "Student sačuvan").showAndWait();
    }

    private void generisiUverenje() {
        if (student == null || studentIndex == null) {
            new Alert(Alert.AlertType.WARNING, "Odaberi studenta i indeks!").showAndWait();
            return;
        }

        try {
            UverenjeData data = studentService.getUverenjeData(student, studentIndex);

            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/reports/uverenje_o_studiranju.jrxml"));

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(List.of(data));
            JasperPrint jp = JasperFillManager.fillReport(report, new HashMap<>(), ds);

            String fileName = "uverenje_" + student.getJmbg() + "_" + studentIndex.getBroj() + "_" + studentIndex.getGodina() + ".pdf";
            JasperExportManager.exportReportToPdfFile(jp, fileName);

            // ✅ Preview (sada radi jer je headless=false)
            javax.swing.SwingUtilities.invokeLater(() -> {
                net.sf.jasperreports.view.JasperViewer.viewReport(jp, false);
            });

            new Alert(Alert.AlertType.INFORMATION, "✅ Uverenje sačuvano i otvoreno: " + fileName).showAndWait();

        } catch (Exception ex) {
            String msg = (ex.getMessage() != null) ? ex.getMessage() : ex.getClass().getSimpleName();
            new Alert(Alert.AlertType.ERROR, "❌ Greška: " + msg).showAndWait();
            ex.printStackTrace();
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        setupNavigationHandlers();
    }

    private void setupNavigationHandlers() {
        if (stage == null || stage.getScene() == null) return;

        Scene scene = stage.getScene();

        // Mouse button 4/5 navigacija
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMouseButton);

        // Swipe navigacija
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> dragStartX = e.getSceneX());
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, this::handleSwipe);

        // Keyboard navigacija
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyNavigation);
    }

    private void handleMouseButton(MouseEvent event) {
        if (event.getButton() == MouseButton.BACK) { // Mouse button 4
            goBack();
            event.consume();
        } else if (event.getButton() == MouseButton.FORWARD) { // Mouse button 5
            goForward();
            event.consume();
        }
    }

    private void handleSwipe(MouseEvent event) {
        double deltaX = event.getSceneX() - dragStartX;

        // Threshold za swipe
        if (deltaX > 100) {
            goBack();
        } else if (deltaX < -100) {
            goForward();
        }
    }

    private void handleKeyNavigation(KeyEvent event) {
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.OPEN_BRACKET) { // Ctrl + [
                goBack();
                event.consume();
            } else if (event.getCode() == KeyCode.CLOSE_BRACKET) { // Ctrl + ]
                goForward();
                event.consume();
            }
        }
    }

    // Dodaj ove dve metode u StudentDetailController
    public boolean canGoBack() {
        return historyManager.canGoBack();
    }

    public boolean canGoForward() {
        return historyManager.canGoForward();
    }

    // U StudentDetailController.goBack():
    @FXML
    public void goBack() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        boolean isFirstTab = currentTab != null && currentTab.getText().equals("Licni Podaci");

        if (isFirstTab) {
            // Ako smo na prvom tabu, SAKRIJ prozor
            if (stage != null) {
                stage.hide();

                // Vrati fokus na owner prozor
                Stage owner = (Stage) stage.getOwner();
                if (owner != null) {
                    owner.requestFocus();
                }
            }
            return;
        }

        // Inače, idi nazad kroz history
        StudentDetailHistoryManager.SceneState state = historyManager.goBack();
        if (state != null) {
            navigateToTabWithContent(state.getRoot());
        }

        updateNavigationButtons();
    }


    @FXML
    public void goForward() {
        StudentDetailHistoryManager.SceneState state = historyManager.goForward();
        if (state != null) {
            navigateToTabWithContent(state.getRoot());
        }
        updateNavigationButtons();
    }

    private void navigateToTabWithContent(Parent content) {
        for (Tab tab : tabPane.getTabs()) {
            Object tabContent = tab.getContent();
            Parent tabParent = null;

            if (tabContent instanceof ScrollPane) {
                ScrollPane scrollPane = (ScrollPane) tabContent;
                tabParent = (Parent) scrollPane.getContent();
            } else if (tabContent instanceof Parent) {
                tabParent = (Parent) tabContent;
            }

            if (tabParent != null && tabParent.equals(content)) {
                tabPane.getSelectionModel().select(tab);
                break;
            }
        }
    }
}
