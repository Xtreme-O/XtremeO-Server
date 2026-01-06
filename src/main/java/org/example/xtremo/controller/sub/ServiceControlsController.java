package org.example.xtremo.controller.sub;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.kordamp.ikonli.javafx.FontIcon;
import org.example.xtremo.ui.ActionHandler;
import org.example.xtremo.ui.ButtonStyleManager;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Abdelrahman
 */
public class ServiceControlsController {

    private final Button stopBtn;
    private final FontIcon stopBtnIcon;
    private final Button restartBtn;
    private final FontIcon restartBtnIcon;
    private final ActionHandler actionHandler;

    public ServiceControlsController(
            Button stopBtn, FontIcon stopBtnIcon,
            Button restartBtn, FontIcon restartBtnIcon,
            ActionHandler actionHandler) {
        this.stopBtn = stopBtn;
        this.stopBtnIcon = stopBtnIcon;
        this.restartBtn = restartBtn;
        this.restartBtnIcon = restartBtnIcon;
        this.actionHandler = actionHandler;
    }

    public void initialize() {
        setupStopButton();
        setupRestartButton();
        updateButtonStates();
    }

    private void setupStopButton() {
        if (stopBtn != null) {
            stopBtn.setOnAction(e -> {
                boolean isActive = actionHandler.handleStopAction();
                ButtonStyleManager.updateStopButtonStyle(stopBtn, stopBtnIcon, isActive);
                updateButtonStates();
            });
        }
    }

    private void setupRestartButton() {
        if (restartBtn != null) {
            restartBtn.setOnAction(e -> {
                ButtonStyleManager.showRestartButtonLoading(restartBtn, restartBtnIcon);
                stopBtn.setDisable(true);
                actionHandler.handleRestartAction();
                hideRestartButtonAfterDelay();
            });
        }
    }

    private void hideRestartButtonAfterDelay() {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                ButtonStyleManager.hideRestartButtonLoading(restartBtn, restartBtnIcon);
                updateButtonStates();
            });
        });
    }

    private void updateButtonStates() {
        boolean isStopped = !actionHandler.isStopButtonActive();
        restartBtn.setDisable(!isStopped);
        boolean isRestarting = restartBtn.isDisabled() 
                && restartBtnIcon.getIconLiteral().equals("fas-spinner");
        stopBtn.setDisable(isRestarting);
    }
}
