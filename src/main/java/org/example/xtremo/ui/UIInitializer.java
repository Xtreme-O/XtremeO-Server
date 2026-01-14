package org.example.xtremo.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.model.entity.Game;
import org.example.xtremo.model.enums.PlayerStatus;
import org.example.xtremo.network.Server;
import org.example.xtremo.service.GameService;
import org.example.xtremo.service.statistics.StateService;
import org.example.xtremo.ui.table.TableManager;

public class UIInitializer {

    private final AnimationManager animationManager;
    private final ChartManager chartManager;
    private final TableManager tableManager;
    private final StateService statsService;

    private Timeline chartTimeline;

    public UIInitializer(
            Button stopBtn,
            BarChart<String, Number> chart,
            TableView<GameDTO> matchesTable
    ) throws SQLException {
        this.animationManager = new AnimationManager(stopBtn);
        this.chartManager = new ChartManager(chart);
        this.tableManager = new TableManager(matchesTable);
        this.statsService = new StateService();
    }

    public void initialize() {

        Platform.runLater(() -> {
            animationManager.initializeAnimations();
            LoggerManager.getInstance().info("Animations initialized");
        });

        LoggerManager.getInstance().log("Initializing UI data");

        startChartMonitoring();
        loadTableDataAsync();
    }

    private void startChartMonitoring() {

        updateChart();

        chartTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateChart())
        );

        chartTimeline.setCycleCount(Animation.INDEFINITE);
        chartTimeline.play();
    }

    private void updateChart() {

        int online = Server.activePlayers.size();
        int offline = statsService.getCount(PlayerStatus.OFFLINE);
        int inGame = Server.sessionManager.getSessionsCount() * 2;

        List<XYChart.Data<String, Number>> chartData = List.of(
                new XYChart.Data<>("Online", online),
                new XYChart.Data<>("Offline", offline),
                new XYChart.Data<>("In-Game", inGame)
        );

        chartManager.setupChart(FXCollections.observableArrayList(chartData));
    }

    public void stopChartMonitoring() {
        if (chartTimeline != null) {
            chartTimeline.stop();
        }
    }


    private void loadTableDataAsync() {

        new Thread(() -> {
            try {
                GameService gameService = GameService.getGameService();

                List<GameDTO> tableData = new ArrayList<>(
                        gameService.findAll()
                                .stream()
                                .map(Game::toGameDTO)
                                .toList()
                );

                tableData.sort((a, b) -> Integer.compare(a.gameId(), b.gameId()));

                Platform.runLater(() -> {
                    tableManager.setData(tableData);
                    LoggerManager.getInstance().success("Table data loaded successfully");
                });

            } catch (SQLException e) {
                LoggerManager.getInstance().error("Failed to load table data");
            }
        }).start();
    }
}
