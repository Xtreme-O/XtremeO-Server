/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.example.xtremo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import org.controlsfx.control.ToggleSwitch;
import javafx.scene.layout.VBox;
import org.example.xtremo.controller.sub.ServiceControlsController;
import org.example.xtremo.controller.sub.TogglesController;
import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.ui.ActionHandler;
import org.example.xtremo.ui.UIInitializer;
import org.kordamp.ikonli.javafx.FontIcon;

/**
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
    private ToggleSwitch matchmakingToggle;
    @FXML
    private ToggleSwitch chatToggle;
    @FXML
    private VBox logContainer;
    @FXML
    private ScrollPane terminalScroll;
    @FXML
    private BarChart<String, Number> playerStatusChart;
    @FXML
    private TableView<GameDTO> matchesTable;

    @FXML
    private ScrollPane mainScrollPane;

    private ActionHandler actionHandler;
    private ServiceControlsController serviceControls;
    private TogglesController togglesController;
    private UIInitializer uIInitializer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(() -> {
            actionHandler = new ActionHandler();
            uIInitializer = new UIInitializer(stopBtn, playerStatusChart, matchesTable);
            uIInitializer.initialize();
            LoggerManager.getInstance().init(logContainer, terminalScroll);
            serviceControls = new ServiceControlsController(stopBtn, stopBtnIcon, restartBtn, restartBtnIcon, actionHandler);
            togglesController = new TogglesController(matchmakingToggle, chatToggle, actionHandler);
            serviceControls.initialize();
            togglesController.initialize();
            enhanceScrolling();

        });
    }

    private void enhanceScrolling() {
        mainScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double contentHeight = mainScrollPane.getContent().getBoundsInLocal().getHeight();
            double viewportHeight = mainScrollPane.getViewportBounds().getHeight();

            double scrollAmount = event.getDeltaY() / (contentHeight - viewportHeight);

            mainScrollPane.setVvalue(
                    mainScrollPane.getVvalue() - scrollAmount
            );

            event.consume();
        });
    }
}
