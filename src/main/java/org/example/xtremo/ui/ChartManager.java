package org.example.xtremo.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class ChartManager {

    private final BarChart<String, Number> chart;

    public ChartManager(BarChart<String, Number> playerChart) {
        this.chart = playerChart;
    }

    public void setupChart(ObservableList<XYChart.Data<String, Number>> chartData) {
        if (chart == null || chartData == null) return;


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Player Status");
        series.setData(chartData);

        Platform.runLater(() -> chart.getData().add(series));
    }

}
