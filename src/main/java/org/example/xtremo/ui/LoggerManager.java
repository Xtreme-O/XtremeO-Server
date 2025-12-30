package org.example.xtremo.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.example.xtremo.utils.LogStyle;

public class LoggerManager {

    private final VBox logContainer;
    private final ScrollPane scrollPane;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LoggerManager(VBox logContainer, ScrollPane scrollPane) {
        this.logContainer = logContainer;
        this.scrollPane = scrollPane;
    }

    public void log(String message) {
        Platform.runLater(() -> {
            String timestamp = LocalTime.now().format(timeFormatter);
            Label logLine = new Label("[" + timestamp + "] " + message);
            logLine.getStyleClass().add("terminal-log-line");
            logContainer.getChildren().add(logLine);

            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }

    public void log(String message, LogStyle style) {
    Platform.runLater(() -> {
        String timestamp = LocalTime.now().format(timeFormatter);
        Label logLine = new Label("[" + timestamp + "] " + message);
        logLine.getStyleClass().addAll("terminal-log-line", style.getCssClass());

        logContainer.getChildren().add(logLine);
        scrollPane.layout();
        scrollPane.setVvalue(1.0); // auto-scroll
    });
}
}
