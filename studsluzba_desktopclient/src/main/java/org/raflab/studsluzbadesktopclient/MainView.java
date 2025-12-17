package org.raflab.studsluzbadesktopclient;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class MainView {

    private final ContextFXMLLoader appFXMLLoader;
    private Scene scene;

    public MainView(ContextFXMLLoader appFXMLLoader) {
        this.appFXMLLoader = appFXMLLoader;
    }

    public Scene createScene() {
        try {
            FXMLLoader loader = appFXMLLoader.getLoader("/fxml/main.fxml");
            BorderPane borderPane = loader.load();
            this.scene = new Scene(borderPane, 1000, 800);
            scene.getStylesheets().add(
                    Objects.requireNonNull(MainView.class.getResource("/css/stylesheet.css")).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.scene;
    }

    public void changeRoot(String fxml) {
        try {
            FXMLLoader loader = appFXMLLoader.getLoader("/fxml/" + fxml + ".fxml");
            scene.setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node loadPane(String fxml) {
        try {
            FXMLLoader loader = appFXMLLoader.getLoader("/fxml/" + fxml + ".fxml");
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void openModal(String fxml) { openModal(fxml, "", 400, 300); }

    public void openModal(String fxml, String title) { openModal(fxml, title, 400, 300); }

    public void openModal(String fxml, String title, int width, int height) {
        try {
            FXMLLoader loader = appFXMLLoader.getLoader("/fxml/" + fxml + ".fxml");
            Parent parent = loader.load();
            Scene scene = new Scene(parent, width, height);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
