package org.raflab.studsluzbadesktopclient;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ContextFXMLLoader {

    private final ConfigurableApplicationContext context;

    public ContextFXMLLoader(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public FXMLLoader getLoader(URL url) {
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean); // Spring bean factory
        return loader;
    }

    public FXMLLoader getLoader(String fxmlPath) {
        URL url = this.getClass().getResource(fxmlPath);
        if (url == null) {
            throw new IllegalArgumentException("FXML file not found: " + fxmlPath);
        }
        return getLoader(url);
    }
}
