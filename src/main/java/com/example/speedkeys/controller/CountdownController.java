package com.example.speedkeys;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller for the countdown screen before the game starts.
 * <p>
 * Displays a countdown 3 → 2 → 1, then automatically
 * navigates to the main game view.
 * </p>
 */
public class CountdownController {

    /** Label that shows the countdown (3, 2, 1). */
    @FXML private Label countLabel;

    /**
     * Initializes the countdown.
     * <p>
     * Starts a {@link Timeline} that updates the label
     * each second until reaching 1.
     * After 2.5 seconds, navigates to the game view.
     * </p>
     */
    @FXML
    public void initialize() {
        if (countLabel == null) return;

        countLabel.setText("3");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),   e -> countLabel.setText("3")),
                new KeyFrame(Duration.seconds(1),   e -> countLabel.setText("2")),
                new KeyFrame(Duration.seconds(2),   e -> countLabel.setText("1")),
                new KeyFrame(Duration.seconds(2.5), e -> Navigator.go(countLabel, "game-view.fxml", 720, 540))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
