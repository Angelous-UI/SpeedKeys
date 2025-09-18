package com.example.speedkeys;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller for the countdown screen before starting the game.
 * <p>
 * Displays a countdown (3 → 2 → 1) and, once finished,
 * automatically switches to the main game view ({@code juego-view.fxml}).
 * </p>
 */
public class CountdownController {

    /** Label that shows the countdown (3, 2, 1). */
    @FXML private Label CuentaRetro;

    /**
     * Called automatically when the FXML is loaded.
     * <p>
     * Starts a {@link Timeline} animation that updates the {@link Label}'s text
     * every second until it reaches 1.
     * After 2.5 seconds, it navigates to the game scene.
     * </p>
     */
    @FXML
    public void initialize() {
        // If the label is not present in the FXML, do nothing
        if (CuentaRetro == null) return;

        CuentaRetro.setText("3");

        // Timeline animation that controls the countdown
        Timeline cuenta = new Timeline(
                new KeyFrame(Duration.seconds(0),   e -> CuentaRetro.setText("3")),
                new KeyFrame(Duration.seconds(1),   e -> CuentaRetro.setText("2")),
                new KeyFrame(Duration.seconds(2),   e -> CuentaRetro.setText("1")),
                new KeyFrame(Duration.seconds(2.5), e ->
                        Navigator.go(CuentaRetro, "juego-view.fxml", 720, 540))
        );

        // Run only once
        cuenta.setCycleCount(1);
        cuenta.play();
    }
}
