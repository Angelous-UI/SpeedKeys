package com.example.speedkeys;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Small helper for scene navigation.
 *
 * <p>Loads an FXML by name and swaps it into the current window (stage).
 * Intended for quick, simple navigation without additional plumbing.</p>
 */
public class Navigator {

    /**
     * Loads an FXML and sets it as the current scene.
     *
     * @param anyNode any node currently attached to the target window
     * @param fxml    FXML file name (resolved relative to {@code Navigator.class})
     * @param width   scene width in pixels
     * @param height  scene height in pixels
     */
    public static void go(Node anyNode, String fxml, int width, int height) {
        try {
            Parent root = FXMLLoader.load(Navigator.class.getResource(fxml));
            Stage stage = (Stage) anyNode.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (Exception e) {
            // Keep it simple: log for now
            e.printStackTrace();
        }
    }
}
