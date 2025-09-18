package com.example.speedkeys;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the Game Over view.
 * <p>
 * Responsible for:
 * <ul>
 *     <li>Displaying the final score and the level reached by the player.</li>
 *     <li>Allowing navigation back to the main menu.</li>
 * </ul>
 * </p>
 */
public class GameOverController {

    /**
     * Label that displays the final score and the level reached.
     * Injected from the corresponding FXML file.
     */
    @FXML private Label lblPuntos;

    /**
     * Image/button used to return to the main menu.
     * Injected from the corresponding FXML file.
     */
    @FXML private ImageView imgVolver;

    /**
     * Handles the click event on the "back" button/image.
     * Navigates the player back to the main menu scene.
     *
     * @param e mouse click event triggered by the {@link ImageView}.
     */
    @FXML
    private void irAEscenaVolver(MouseEvent e) {
        Navigator.go((Node)e.getSource(), "hello-view.fxml", 720, 540);
    }

    /**
     * Updates the label with the final score and the level reached.
     *
     * @param puntos total points earned by the player.
     * @param nivel  highest level reached by the player.
     */
    public void setPuntos(int puntos, int nivel) {
        if (lblPuntos != null) {
            lblPuntos.setText("Points: " + puntos + "  |  Level: " + nivel);
        }
    }
}
