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
 * Controller for the main menu screen.
 * <p>
 * Manages the main menu interactions: start the game,
 * show the info screen, or return to the menu.
 * Also applies a glowing blinking effect to the title image.
 * </p>
 */
public class MenuController {

    /** Timeline for blinking the title glow effect. */
    private Timeline titleBlink;

    /** Title image that blinks with a glow effect. */
    @FXML private ImageView imgTitle;

    /** "Play" button image. */
    @FXML private ImageView imgPlay;

    /** "Info" button image. */
    @FXML private ImageView imgInfo;

    /** "Back" button image, used only in the info-view. */
    @FXML private ImageView imgBack;

    /**
     * Initializes the menu.
     * <p>
     * Adds a glow-blinking effect to the title image if present.
     * </p>
     */
    @FXML
    public void initialize() {
        if (imgTitle != null) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.WHITE);
            glow.setRadius(30);
            glow.setSpread(0.7);

            titleBlink = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> imgTitle.setEffect(glow)),
                    new KeyFrame(Duration.seconds(0.4), e -> imgTitle.setEffect(null))
            );
            titleBlink.setCycleCount(Animation.INDEFINITE);
            titleBlink.setAutoReverse(true);
            titleBlink.play();
        }
    }

    /**
     * Navigates to the game screen.
     * <p>
     * Called when clicking the "Play" button.
     * Resets the {@link GameState}.
     * </p>
     *
     * @param e mouse click event.
     */
    @FXML
    private void goToGame(MouseEvent e) {
        GameState.get().reset();
        Navigator.go((Node) e.getSource(), "countdown-view.fxml", 720, 540);
    }

    /**
     * Navigates to the info screen.
     *
     * @param e mouse click event.
     */
    @FXML
    private void goToInfo(MouseEvent e) {
        Navigator.go((Node) e.getSource(), "info-view.fxml", 720, 540);
    }

    /**
     * Navigates back to the main menu.
     * <p>
     * Used in the info-view when clicking the "Back" button.
     * </p>
     *
     * @param e mouse click event.
     */
    @FXML
    private void goBackToMenu(MouseEvent e) {
        Navigator.go((Node) e.getSource(), "hello-view.fxml", 720, 540);
    }
}
