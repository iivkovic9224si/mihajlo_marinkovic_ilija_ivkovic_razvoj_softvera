package org.raflab.studsluzbadesktopclient;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.raflab.studsluzbadesktopclient.HistoryManager;
import org.springframework.stereotype.Component;

@Component
public class MainView {

    private final ContextFXMLLoader appFXMLLoader;
    private final HistoryManager history = HistoryManager.getInstance();
    private Scene scene;
    private boolean isInitialSceneLoaded = false; // flag da ne pushujemo start scene u istoriju

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

            // push samo kad se stvarno navigira
            history.push(borderPane, "main");
            isInitialSceneLoaded = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.scene;
    }

    /**
     * Promena root node-a glavne scene i čuvanje u istoriji.
     */
    public void changeRoot(String fxml) {
        try {
            FXMLLoader loader = appFXMLLoader.getLoader("/fxml/" + fxml + ".fxml");
            Parent newRoot = loader.load();

            // push-uj trenutni root u istoriju SAMO ako nije prvi load
            if (scene.getRoot() != null && isInitialSceneLoaded) {
                history.push(scene.getRoot(), fxml);
            }

            scene.setRoot(newRoot);
            isInitialSceneLoaded = true; // sada su sve promene validne

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Idi nazad u istoriji.
     */
    public void goBack() {
        HistoryManager.SceneState state = history.goBack();
        if (state != null) {
            scene.setRoot(state.getRoot());
        }
    }

    /**
     * Idi napred u istoriji.
     */
    public void goForward() {
        HistoryManager.SceneState state = history.goForward();
        if (state != null) {
            scene.setRoot(state.getRoot());
        }
    }

    public Parent loadPane(String fxml) {
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
