package org.example.xtremo.ui;

import java.sql.SQLException;

import javafx.scene.text.Text;
import org.example.xtremo.model.entity.Game;
import org.example.xtremo.model.enums.PlayerStatus;
import org.example.xtremo.service.GameService;
import org.example.xtremo.service.statistics.StateService;
import org.example.xtremo.ui.table.TableManager;
import java.time.LocalDateTime;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import org.example.xtremo.logging.LoggerManager;
import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.model.enums.GameResult;
import org.example.xtremo.model.enums.GameType;

/**
 * All data fetching happens here (dummy data for now, will replace with
 * service/DAO later).
 */
public class UIInitializer {

    private final AnimationManager animationManager;
    private final ChartManager chartManager;
    private final TableManager tableManager;
    private final StateService statsService;

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

        statsService.listenToUpdates(this::loadChartDataAsync);

        Platform.runLater(() -> {
            animationManager.initializeAnimations();
            LoggerManager.getInstance().error("Animations initialized");
        });

        LoggerManager.getInstance().log("Data is being initialized");

        loadChartDataAsync();
        loadTableDataAsync();

    }

    private void loadChartDataAsync() {
        new Thread(() -> {

            int online = statsService.getCount(PlayerStatus.ONLINE);
            int offline = statsService.getCount(PlayerStatus.OFFLINE);
            int inGame = statsService.getCount(PlayerStatus.INGAME);

            System.out.println(online);

            LoggerManager.getInstance().success("Loading chart data...");

            List<XYChart.Data<String, Number>> chartData = new ArrayList<>();
            chartData.add(new XYChart.Data<>("Online", online));
            chartData.add(new XYChart.Data<>("Offline", offline));
            chartData.add(new XYChart.Data<>("In-Game", inGame));

            Platform.runLater(() -> {

                chartManager.setupChart(FXCollections.observableArrayList(chartData));
                LoggerManager.getInstance().info("Chart data loaded successfully");
            });
        }).start();
    }

    // ONLY DUMMY DATA
    private void loadTableDataAsync() {
        try {
            System.out.println("mona");
            GameService gameService = GameService.getGameService();
            List<GameDTO> tableData = new ArrayList<>(gameService.findAll().stream().map(Game::toGameDTO).toList());
            Platform.runLater(() -> {
                System.out.println("monnnnna");

                tableData.sort((a, b) -> Integer.compare(a.gameId(), b.gameId()));
                tableManager.setData(tableData);
                LoggerManager.getInstance().success("Table data loaded successfully");
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            LoggerManager.getInstance().warn("Loading table data...");




        }).start();
    }
}
