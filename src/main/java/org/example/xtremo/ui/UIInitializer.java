package org.example.xtremo.ui;

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

    public UIInitializer(
            Button stopBtn,
            BarChart<String, Number> chart,
            TableView<GameDTO> matchesTable
    ) {
        this.animationManager = new AnimationManager(stopBtn);
        this.chartManager = new ChartManager(chart);
        this.tableManager = new TableManager(matchesTable);
    }

    public void initialize() {
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
            LoggerManager.getInstance().success("Loading chart data...");

            List<XYChart.Data<String, Number>> chartData = new ArrayList<>();
            chartData.add(new XYChart.Data<>("Online", 40));
            chartData.add(new XYChart.Data<>("Offline", 30));
            chartData.add(new XYChart.Data<>("In-Game", 20));

            Platform.runLater(() -> {
                chartManager.setupChart(FXCollections.observableArrayList(chartData));
                LoggerManager.getInstance().info("Chart data loaded successfully");
            });
        }).start();
    }

    // ONLY DUMMY DATA
    private void loadTableDataAsync() {
        new Thread(() -> {
            LoggerManager.getInstance().warn("Loading table data...");

            List<GameDTO> tableData = new ArrayList<>();
            tableData.add(new GameDTO(
                    1,
                    GameType.TIC_TAC_TOE,
                    101,
                    102,
                    101,
                    GameResult.WIN,
                    LocalDateTime.now().minusHours(2),
                    LocalDateTime.now().minusHours(1).minusMinutes(45),
                    true,
                    "/records/game1.mp4"
            ));

            tableData.add(new GameDTO(
                    2,
                    GameType.TIC_TAC_TOE,
                    103,
                    104,
                    null,
                    GameResult.DRAW,
                    LocalDateTime.now().minusDays(1).minusHours(3),
                    LocalDateTime.now().minusDays(1).minusHours(2).minusMinutes(30),
                    false,
                    null
            ));

            Platform.runLater(() -> {
                tableData.sort((a, b) -> Integer.compare(a.gameId(), b.gameId()));
                tableManager.setData(tableData);
                LoggerManager.getInstance().success("Table data loaded successfully");
            });
        }).start();
    }
}
