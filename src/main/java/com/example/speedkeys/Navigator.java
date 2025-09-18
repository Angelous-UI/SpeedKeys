package com.example.speedkeys;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class for scene navigation within the application.
 * <p>
 * Provides a single static method {@link #go(Node, String, int, int)} that allows
 * switching between FXML views easily by specifying the source node, target FXML,
 * and window dimensions.
 * </p>
 */
public class Navigator {

    /**
     * Changes the current scene to a new one defined by the given FXML file.
     *
     * @param anyNode a {@link Node} from the current scene (used to obtain the {@link Stage})
     * @param fxml    the name of the FXML file to load (must be in the same package as {@link Navigator})
     * @param w       width of the new scene
     * @param h       height of the new scene
     */
    public static void go(Node anyNode, String fxml, int w, int h) {
        try {
            Parent root = FXMLLoader.load(Navigator.class.getResource(fxml));
            Stage stage = (Stage) anyNode.getScene().getWindow();
            stage.setScene(new Scene(root, w, h));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
