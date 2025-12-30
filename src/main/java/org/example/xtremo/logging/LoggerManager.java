/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Abdelrahman
 */
package org.example.xtremo.logging;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.example.xtremo.utils.LogStyle;

public final class LoggerManager {

    private static final LoggerManager INSTANCE = new LoggerManager();

    private volatile VBox logContainer;
    private volatile ScrollPane scrollPane;

    private final Queue<Runnable> pendingLogs = new ConcurrentLinkedQueue<>();

    private final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LoggerManager() {
    }

    public static LoggerManager getInstance() {
        return INSTANCE;
    }

    public void init(VBox box, ScrollPane pane) {
        if (logContainer != null && pane != null) return;
        synchronized (this) {
            if (logContainer != null && scrollPane != null) {
                return;
            }
            this.logContainer = box;
            this.scrollPane = pane;
            pendingLogs.forEach(Platform::runLater);
            pendingLogs.clear();
        }

    }

    public void log(String message) {
        log(message, null);
    }

    public void info(String msg) {
        log(msg, LogStyle.INFO);
    }

    public void success(String msg) {
        log(msg, LogStyle.SUCCESS);
    }

    public void warn(String msg) {
        log(msg, LogStyle.WARN);
    }

    public void error(String msg) {
        log(msg, LogStyle.ERROR);
    }

    private void log(String message, LogStyle style) {
        Runnable task = () -> {
            if (logContainer == null || scrollPane == null) {
                return;
            }

            Label label = new Label(
                    "[" + LocalTime.now().format(formatter) + "] " + message
            );

            label.getStyleClass().add("terminal-log-line");
            if (style != null) {
                label.getStyleClass().add(style.getCssClass());
            }

            logContainer.getChildren().add(label);
            scrollPane.setVvalue(1.0);
        };

        if (logContainer == null) {
            pendingLogs.add(task);
        } else {
            Platform.runLater(task);
        }
    }

}
