/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.example.xtremo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import org.controlsfx.control.ToggleSwitch;
import javafx.scene.layout.VBox;
import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.ui.ActionHandler;
import org.example.xtremo.ui.ButtonStyleManager;
import org.example.xtremo.ui.UIInitializer;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * FXML Controller class
 *
 * @author Abdelrahman
 */
public class PrimaryController implements Initializable {

    @FXML
    private Button stopBtn;

    @FXML
    private FontIcon stopBtnIcon;

    @FXML
    private Button restartBtn;

    @FXML
    private FontIcon restartBtnIcon;

    @FXML
    private LineChart<Number, Number> playerChart;

    @FXML
    private VBox logContainer;

    @FXML
    private ToggleSwitch matchmakingToggle;

    @FXML
    private ToggleSwitch chatToggle;

    @FXML
    private TableView<GameDTO> matchesTable;

    private UIInitializer uiInitializer;
    private ActionHandler actionHandler;
    @FXML
    private FontIcon logoIcon;
    @FXML
    private FontIcon activePlayersIcon;
    @FXML
    private FontIcon liveMatchesIcon;
    @FXML
    private ScrollPane terminalScroll;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actionHandler = new ActionHandler();
        setupActionHandlers();
        Platform.runLater(() -> {
            Parent root = getScene();
            if (root != null) {
                uiInitializer = new UIInitializer(
                        root,
                        playerChart,
                        matchesTable,
                        logContainer,
                        terminalScroll);
                uiInitializer.initialize();
                LoggerManager.getInstance().init(logContainer, terminalScroll);
            }
        });
    }

    private Parent getScene() {
        if (stopBtn != null && stopBtn.getScene() != null) {
            return stopBtn.getScene().getRoot();
        }
        return null;
    }

    private void setupActionHandlers() {
        setupStopButton();
        setupRestartButton();
        setupToggles();
    }

    private void setupStopButton() {
        if (stopBtn != null) {
            stopBtn.setOnAction(e
                    -> ButtonStyleManager.updateStopButtonStyle(stopBtn, stopBtnIcon, actionHandler.handleStopAction())
            );
        }
    }

    private void setupRestartButton() {
        if (restartBtn != null) {
            restartBtn.setOnAction(e -> {
                ButtonStyleManager.showRestartButtonLoading(restartBtn, restartBtnIcon);
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
            Platform.runLater(() -> 
                    ButtonStyleManager.hideRestartButtonLoading(restartBtn, restartBtnIcon));
        });
    }

    private void setupToggles() {
        if (matchmakingToggle != null) {
            matchmakingToggle.selectedProperty().addListener((obs, oldVal, newVal)
                    -> actionHandler.handleMatchmakingToggle(newVal)
            );
        }

        if (chatToggle != null) {
            chatToggle.selectedProperty().addListener((obs, oldVal, newVal)
                    -> actionHandler.handleChatToggle(newVal)
            );
        }
    }

}
