package org.raflab.studsluzbadesktopclient;

import javafx.scene.Parent;
import java.util.ArrayDeque;
import java.util.Deque;

public class StudentDetailHistoryManager {
    private static final int MAX_HISTORY = 10;

    private final Deque<SceneState> backStack = new ArrayDeque<>();
    private final Deque<SceneState> forwardStack = new ArrayDeque<>();

    private SceneState current;

    public void navigate(Parent root, String name) {
        if (current != null && current.getName().equals(name)) {
            return;
        }

        if (current != null) {
            backStack.push(current);
            if (backStack.size() > MAX_HISTORY) {
                backStack.removeLast();
            }
        }

        current = new SceneState(root, name);
        forwardStack.clear();
    }

    public SceneState goBack() {
        if (!canGoBack()) return null;

        forwardStack.push(current);
        current = backStack.pop();
        return current;
    }

    public SceneState goForward() {
        if (!canGoForward()) return null;

        backStack.push(current);
        current = forwardStack.pop();
        return current;
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public void clear() {
        backStack.clear();
        forwardStack.clear();
        current = null;
    }

    public static class SceneState {
        private final Parent root;
        private final String name;

        public SceneState(Parent root, String name) {
            this.root = root;
            this.name = name;
        }

        public Parent getRoot() {
            return root;
        }

        public String getName() {
            return name;
        }
    }
}