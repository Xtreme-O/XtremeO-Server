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
import java.util.concurrent.atomic.AtomicBoolean;

import org.example.xtremo.utils.LogStyle;

public final class LoggerManager {

    private static final LoggerManager INSTANCE = new LoggerManager();
    private static final int MAX_LOG_ENTRIES = 100;

    private volatile VBox logContainer;
    private volatile ScrollPane scrollPane;

    private final Queue<Label> pendingLabels = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean updateScheduled = new AtomicBoolean(false);

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
            if (!pendingLabels.isEmpty()) {
                scheduleFlush();
            }
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
        Label label = createLabel(message, style);
        pendingLabels.add(label);
        
        if (logContainer != null && 
                updateScheduled.compareAndSet(false, true)) {
            scheduleFlush();
        }
    }

    private Label createLabel(String message, LogStyle style) {
        Label label = new Label(
                "[" + LocalTime.now().format(formatter) + "] " + message
        );
        label.getStyleClass().add("terminal-log-line");
        if (style != null) {
            label.getStyleClass().add(style.getCssClass());
        }
        return label;
    }

    private void scheduleFlush() {
        Platform.runLater(this::flushPendingLogs);
    }

    private void flushPendingLogs() {
        updateScheduled.set(false);
        
        if (logContainer == null || scrollPane == null) {
            return;
        }

        Label label;
        while ((label = pendingLabels.poll()) != null) {
            logContainer.getChildren().add(label);
        }

        while (logContainer.getChildren().size() > MAX_LOG_ENTRIES) {
            logContainer.getChildren().remove(0);
        }

        scrollPane.setVvalue(1.0);
    }
}
