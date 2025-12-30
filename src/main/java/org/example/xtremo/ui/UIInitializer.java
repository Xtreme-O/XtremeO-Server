package org.example.xtremo.ui;

import org.example.xtremo.ui.table.TableManager;
import java.time.LocalDateTime;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TableView;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.List;
import org.example.xtremo.model.dto.GameDTO;
import org.example.xtremo.model.enums.GameResult;
import org.example.xtremo.model.enums.GameType;
import org.example.xtremo.utils.LogStyle;

/**
 * All data fetching happens here (dummy data for now, will replace with service/DAO later).
 */
public class UIInitializer {

    private final AnimationManager animationManager;
    private final ChartManager chartManager;
    private final TableManager tableManager;
    private final LoggerManager logger; // Add logger

    public UIInitializer(
            Parent root,
            LineChart<Number, Number> chart,
            TableView<GameDTO> matchesTable,
            VBox logContainer,
            ScrollPane terminalScroll
    ) {
        this.animationManager = new AnimationManager(root);
        this.chartManager = new ChartManager(chart);
        this.tableManager = new TableManager(matchesTable);
        this.logger = new LoggerManager(logContainer, terminalScroll);
    }

    public void initialize() {
        Platform.runLater(() -> {
            animationManager.initializeAnimations();
            logger.log("Animations initialized", LogStyle.INFO);
        });

        loadChartDataAsync();
        loadTableDataAsync();

    }

    private void loadChartDataAsync() {
        new Thread(() -> {
            logger.log("Loading chart data...");

            List<XYChart.Data<Number, Number>> chartData = new ArrayList<>();
            double[] hourlyData = {
                    250, 180, 120, 95, 110, 180,
                    320, 480, 650, 720, 680, 750,
                    820, 880, 920, 980, 1050, 1150,
                    1200, 1180, 1100, 950, 680, 420
            };
            for (int hour = 0; hour < hourlyData.length; hour++) {
                chartData.add(new XYChart.Data<>(hour, hourlyData[hour]));
            }

            Platform.runLater(() -> {
                chartManager.setupChart(FXCollections.observableArrayList(chartData));
                logger.log("Chart data loaded successfully", LogStyle.SUCCESS);
            });
        }).start();
    }

    
    // ONLY DUMMY DATA
    private void loadTableDataAsync() {
        new Thread(() -> {
            logger.log("Loading table data...", LogStyle.INFO);

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
                logger.log("Table data loaded successfully", LogStyle.SUCCESS);
            });
        }).start();
    }

    public LoggerManager getLogger() {
        return logger;
    }
}
