package com.example.speedkeys;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the "Game Over" screen.
 * <p>
 * Displays the final score and level reached by the player,
 * and allows returning to the main menu.
 * </p>
 */
public class GameOverController {

    /** Label where the score and level are shown. */
    @FXML private Label lblScore;

    /** Image/button to return to the main menu. */
    @FXML private ImageView imgBack;

    /**
     * Event that navigates back to the main menu
     * when clicking the back button.
     *
     * @param e mouse click event on the {@link ImageView}.
     */
    @FXML
    private void goBackToMenu(MouseEvent e) {
        Navigator.go((Node) e.getSource(), "hello-view.fxml", 720, 540);
    }

    /**
     * Sets and displays the final score and level.
     *
     * @param score total points achieved.
     * @param level maximum level reached.
     */
    public void setScore(int score, int level) {
        if (lblScore != null) {
            lblScore.setText("Puntos: " + score + "  |  Nivel: " + level);
        }
    }
}
