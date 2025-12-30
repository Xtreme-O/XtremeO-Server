package org.example.xtremo.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class ChartManager {

    private final LineChart<Number, Number> chart;

    public ChartManager(LineChart<Number, Number> playerChart) {
        this.chart = playerChart;
    }

    public void setupChart(ObservableList<XYChart.Data<Number, Number>> chartData) {
        if (chart == null || chartData == null) return;

        configureChart();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Player Concurrency");
        series.setData(chartData);

        Platform.runLater(() -> chart.getData().add(series));
    }

    private void configureChart() {
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.setAnimated(true);
    }
}
