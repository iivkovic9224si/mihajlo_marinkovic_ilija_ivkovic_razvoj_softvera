package org.raflab.studsluzbadesktopclient;

import javafx.scene.Parent;

import java.util.Stack;

/**
 * Singleton za čuvanje istorije prikaza scena (back/forward).
 */
public class HistoryManager {

    private static HistoryManager instance;

    private final Stack<SceneState> backStack = new Stack<>();
    private final Stack<SceneState> forwardStack = new Stack<>();

    private HistoryManager() { }

    public static HistoryManager getInstance() {
        if (instance == null) instance = new HistoryManager();
        return instance;
    }

    /**
     * Dodaje novu scenu u istoriju.
     * @param root Root node scene
     * @param name Naziv scene (npr. ime FXML fajla)
     */
    public void push(Parent root, String name) {
        backStack.push(new SceneState(root, name));
        forwardStack.clear(); // nova scena briše "napred" istoriju
    }

    /**
     * Idi nazad u istoriji.
     */
    public SceneState goBack() {
        if (backStack.size() > 1) { // uvek ostavi trenutnu u stacku
            SceneState current = backStack.pop();
            forwardStack.push(current);
            return backStack.peek();
        }
        return null;
    }

    /**
     * Idi napred u istoriji.
     */
    public SceneState goForward() {
        if (!forwardStack.isEmpty()) {
            SceneState state = forwardStack.pop();
            backStack.push(state);
            return state;
        }
        return null;
    }

    public boolean canGoBack() { return backStack.size() > 1; }
    public boolean canGoForward() { return !forwardStack.isEmpty(); }

    /**
     * Reprezentacija stanja scene
     */
    public static class SceneState {
        private final Parent root;
        private final String name;

        public SceneState(Parent root, String name) {
            this.root = root;
            this.name = name;
        }

        public Parent getRoot() { return root; }
        public String getName() { return name; }
    }
}
