package org.raflab.studsluzbadesktopclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzbadesktopclient.MainView;
import org.raflab.studsluzbadesktopclient.coder.CoderFactory;
import org.raflab.studsluzbadesktopclient.coder.CoderType;
import org.raflab.studsluzbadesktopclient.coder.SimpleCode;
import org.raflab.studsluzbadesktopclient.services.StudentService;
import org.springframework.stereotype.Component;
import response.StudentPodaciResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class StudentController {

    private final StudentService studentService;
    private final CoderFactory coderFactory;

    @FXML
    private TextField imeTf;
    @FXML
    private TextField prezimeTf;
    @FXML
    private TextField srednjeImeTf;
    @FXML
    private RadioButton muski;
    @FXML
    private RadioButton zenski;
    @FXML
    private TextField jmbgTf;
    @FXML
    private DatePicker datumRodjenjaDp;
    @FXML
    private DatePicker datumAktivacijeDp;
    @FXML
    ComboBox<SimpleCode> mestoRodjenjaCb;
    @FXML
    private TextField emailPrivatniTf;
    @FXML
    private TextField emailFakultetTf;
    @FXML
    TextField brojTelefonaTf;
    @FXML
    TextField adresaTf;
    @FXML
    ComboBox<SimpleCode> mestoStanovanjaCb;
    @FXML
    ComboBox<SimpleCode> drzavaRodjenjaCb;
    @FXML
    ComboBox<SimpleCode> drzavljanstvoCb;
    @FXML
    TextField nacionalnostTf;
    @FXML
    TextField brojLicneKarteTf;
    @FXML
    TextField godinaUpisaTf;
    @FXML
    TextField brojIndeksaTf;
    @FXML
    TextField godinaIndeksaTf;
    @FXML
    TextField uspehSrednjaSkolaTf;
    @FXML
    TextField uspehPrijemniTf;

//    @FXML
//    ComboBox<StudProgram> studProgramCb;
//    @FXML
//    ComboBox<SrednjaSkola> srednjaSkolaCb;
//    @FXML
//    ComboBox<VisokoskolskaUstanova> visokoskolskaUstanovaCb;

    public StudentController(StudentService studentService,CoderFactory coderFactory) {
        this.studentService = studentService;
        this.coderFactory = coderFactory;
    }

    @FXML
    public void initialize(){
        drzavaRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        drzavaRodjenjaCb.setValue(new SimpleCode("Serbia"));

        drzavljanstvoCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.DRZAVA).getCodes()));
        drzavljanstvoCb.setValue(new SimpleCode("Serbia"));

        mestoRodjenjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        mestoRodjenjaCb.setValue(new SimpleCode("Beograd"));

        mestoStanovanjaCb.setItems(FXCollections.observableArrayList(coderFactory.getSimpleCoder(CoderType.MESTO).getCodes()));
        mestoStanovanjaCb.setValue(new SimpleCode("Beograd"));
    }

    public void handleOpenModalSrednjeSkole(ActionEvent ae) {
        //mainViewManager.openModal("addSrednjaSkola");
    }

    public void handleOpenModalVisokoskolskeUstanove(ActionEvent ae) {
        //mainViewManager.openModal("addVisaUstanovaForStudent");
    }

    public void handleSaveStudent(ActionEvent event) {
        StudentPodaciResponse studentDTO = new StudentPodaciResponse(
                0L, // id, ako se radi novi unos može 0 ili null
                imeTf.getText(),
                prezimeTf.getText(),
                srednjeImeTf.getText(),
                jmbgTf.getText(),
                datumRodjenjaDp.getValue(), // LocalDate
                mestoRodjenjaCb.getValue() != null ? mestoRodjenjaCb.getValue().getCode() : null,
                mestoStanovanjaCb.getValue() != null ? mestoStanovanjaCb.getValue().getCode() : null, // mestoPrebivalista
                drzavaRodjenjaCb.getValue() != null ? drzavaRodjenjaCb.getValue().getCode() : null,
                drzavljanstvoCb.getValue() != null ? drzavljanstvoCb.getValue().getCode() : null,
                nacionalnostTf.getText(),
                muski.isSelected() ? 'M' : 'Ž',
                adresaTf.getText(),
                brojTelefonaTf.getText(),
                null, // brojTelefonaFiksni, ako nemaš
                emailFakultetTf.getText(),
                brojLicneKarteTf.getText(),
                null, // licnuKartuIzdao, ako nemaš
                mestoStanovanjaCb.getValue() != null ? mestoStanovanjaCb.getValue().getCode() : null,
                adresaTf.getText() // adresaStanovanja, ako je ista kao adresa
        );

        studentService.saveStudent(studentDTO);
        resetForm();
    }


    public void handleIzvestaj() throws JRException {

        List<StudentPodaciResponse> studenti = studentService.sviStudenti();

        JasperReport report = JasperCompileManager.compileReport(
                MainView.class.getResourceAsStream("/reports/sviStudenti.jrxml")
        );

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studenti);
        JasperPrint jp = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

        JasperExportManager.exportReportToPdfFile(jp, "sviStudenti.pdf");
    }


    private void resetForm() {
        imeTf.clear();
        prezimeTf.clear();
        srednjeImeTf.clear();

        muski.setSelected(false);
        zenski.setSelected(false);

        jmbgTf.clear();
        datumRodjenjaDp.setValue(null);
        datumAktivacijeDp.setValue(null);

        mestoRodjenjaCb.setValue(null);
        emailPrivatniTf.clear();
        emailFakultetTf.clear();
        brojTelefonaTf.clear();
        adresaTf.clear();

        mestoStanovanjaCb.setValue(null);
        drzavaRodjenjaCb.setValue(null);
        drzavljanstvoCb.setValue(null);

        nacionalnostTf.clear();
        brojLicneKarteTf.clear();
        godinaUpisaTf.clear();
        brojIndeksaTf.clear();
        godinaIndeksaTf.clear();
        uspehSrednjaSkolaTf.clear();
        uspehPrijemniTf.clear();
    }


}
