package org.raflab.studsluzbadesktopclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StudsluzbaFxClientApp extends Application {

    protected ConfigurableApplicationContext springContext;

    static {
        FXMLLoader.setDefaultClassLoader(StudsluzbaFxClientApp.class.getClassLoader());
    }

    public static void main(String[] args) {
        launch(StudsluzbaFxClientApp.class);
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StudsluzbaFxClientApp.class);
        springContext = builder.headless(false).run();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RAF Studentska služba");
        MainView mainView = springContext.getBean(MainView.class);
        primaryStage.setScene(mainView.createScene());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
        Platform.exit();
    }
}
