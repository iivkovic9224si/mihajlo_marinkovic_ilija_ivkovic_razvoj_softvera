package org.raflab.studsluzbadesktopclient;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainView {

    private final ContextFXMLLoader fxmlLoader;
    private final HistoryManager history = HistoryManager.getInstance();

    private BorderPane mainRoot;

    // Za swipe/drag detekciju
    private double dragStartX;

    public MainView(ContextFXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Scene createScene() {
        try {
            mainRoot = fxmlLoader.getLoader("/fxml/main.fxml").load();
            Scene scene = new Scene(mainRoot, 1000, 800);

            // Početna scena
            history.navigate((Parent) mainRoot.getCenter(), "start");

            // ----------------------------------
            // Dodavanje podrške za navigaciju
            // ----------------------------------

            // Mouse button 4 i 5
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMouseButton);

            // Swipe preko drag-a (swing left/right)
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> dragStartX = e.getSceneX());
            scene.addEventFilter(MouseEvent.MOUSE_RELEASED, this::handleSwipe);

            // Tastatura Ctrl+[ i Ctrl+]
            scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyNavigation);

            return scene;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeRoot(String fxml) {
        try {
            Parent newCenter = fxmlLoader
                    .getLoader("/fxml/" + fxml + ".fxml")
                    .load();

            history.navigate(newCenter, fxml);
            mainRoot.setCenter(newCenter);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goBack() {
        HistoryManager.SceneState state = history.goBack();
        if (state != null) {
            mainRoot.setCenter(state.getRoot());
        }
    }

    public void goForward() {
        HistoryManager.SceneState state = history.goForward();
        if (state != null) {
            mainRoot.setCenter(state.getRoot());
        }
    }

    public boolean canGoBack() {
        return history.canGoBack();
    }

    public boolean canGoForward() {
        return history.canGoForward();
    }

    // --------------------------
    // Mouse button 4/5
    // --------------------------
    private void handleMouseButton(MouseEvent event) {
        if (event.getButton() == MouseButton.BACK) { // Mouse button 4
            goBack();
            event.consume();
        } else if (event.getButton() == MouseButton.FORWARD) { // Mouse button 5
            goForward();
            event.consume();
        }
    }

    // --------------------------
    // Swipe/drag navigacija
    // --------------------------
    private void handleSwipe(MouseEvent event) {
        double deltaX = event.getSceneX() - dragStartX;

        // Threshold za swipe (da slučajno ne okida)
        if (deltaX > 100) {
            goBack();
        } else if (deltaX < -100) {
            goForward();
        }
    }

    // --------------------------
    // Tastatura Ctrl+[ i Ctrl+]
    // --------------------------
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
}
