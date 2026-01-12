package org.example.xtremo.controller.sub;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.xtremo.service.statistics.StateService;
import org.example.xtremo.model.enums.PlayerStatus;

public class ServerStatsMonitor {
    private final StateService statsService;
    private Timeline timeline;

    public ServerStatsMonitor(StateService statsService) {
        this.statsService = statsService;
    }

    public void startMonitoring(Text activePlayersText, Text liveMatchesText) {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    int onlineCount = statsService.getCount(PlayerStatus.ONLINE);
                    int matchesCount = statsService.getActiveMatchesCount();

                    Platform.runLater(() -> {
                        activePlayersText.setText(String.valueOf(onlineCount));
                        liveMatchesText.setText(String.valueOf(matchesCount));
                    });
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}