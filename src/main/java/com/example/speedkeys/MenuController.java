package com.example.speedkeys;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Controller for the main menu and info screens of the game.
 * <p>
 * Handles navigation between menu, info, countdown, and game views.
 * Also manages the title glow animation on the main menu screen.
 * </p>
 */
public class MenuController {

    /** Timeline used to create the blinking glow effect on the title image. */
    private Timeline tituloBlink;

    /** Title image in the main menu (blinks with glow). */
    @FXML private ImageView imgTitulo;

    /** Button image to start a new game. */
    @FXML private ImageView imgJugar;

    /** Button image to go to the info/help screen. */
    @FXML private ImageView imgInfo;

    /** Button image to go back to the main menu (used in info-view). */
    @FXML private ImageView imgVolver;

    /**
     * Called automatically when the FXML is loaded.
     * <p>
     * If the current scene contains the title image, applies a glow effect
     * that blinks indefinitely using a {@link Timeline}.
     * </p>
     */
    @FXML
    public void initialize() {
        if (imgTitulo != null) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.WHITE);
            glow.setRadius(30);
            glow.setSpread(0.7);

            tituloBlink = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> imgTitulo.setEffect(glow)),
                    new KeyFrame(Duration.seconds(0.4), e -> imgTitulo.setEffect(null))
            );
            tituloBlink.setCycleCount(Animation.INDEFINITE);
            tituloBlink.setAutoReverse(true);
            tituloBlink.play();
        }
    }

    /**
     * Starts a new game when the "Play" button is clicked.
     * <p>
     * Resets the global {@link GameState} and navigates to the
     * countdown screen ({@code conteo-view.fxml}).
     * </p>
     *
     * @param e mouse click event triggered on the "Play" button.
     */
    @FXML
    private void irAEscenaJuego(MouseEvent e) {
        GameState.get().reset();
        Navigator.go((Node)e.getSource(), "conteo-view.fxml", 720, 540);
    }

    /**
     * Opens the info/help screen when the "Info" button is clicked.
     *
     * @param e mouse click event triggered on the "Info" button.
     */
    @FXML
    private void irAEscenaInfo(MouseEvent e) {
        Navigator.go((Node)e.getSource(), "info-view.fxml", 720, 540);
    }

    /**
     * Returns to the main menu when the "Back" button is clicked
     * (used in {@code info-view.fxml}).
     *
     * @param e mouse click event triggered on the "Back" button.
     */
    @FXML
    private void irAEscenaVolver(MouseEvent e) {
        Navigator.go((Node)e.getSource(), "hello-view.fxml", 720, 540);
    }
}
