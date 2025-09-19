package com.example.speedkeys;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the main gameplay screen.
 * <p>
 * Handles the game loop logic:
 * <ul>
 *   <li>Displays random words from {@link WordRepository}.</li>
 *   <li>Manages timer per level with decreasing time limits.</li>
 *   <li>Processes user input and validates against the target word.</li>
 *   <li>Gives feedback (green/red) for correct/incorrect input.</li>
 *   <li>Increments score and level, or triggers game over.</li>
 * </ul>
 * </p>
 */
public class GameController {

    // ====== Constants ======
    private static final int TIME_INITIAL = 20;
    private static final int TIME_MIN     = 2;
    private static final int TIME_STEP    = 2;
    private static final int LEVELS_STEP  = 5;

    // ====== State ======
    private int level   = 1;
    private int score   = 0;
    private String target = "";

    private Timeline timer;
    private final PauseTransition okPause   = new PauseTransition(Duration.seconds(0.5));
    private final PauseTransition badPause  = new PauseTransition(Duration.seconds(0.5));

    private boolean levelClosed   = false;   // true if level already resolved
    private boolean feedbackOnAir = false;   // true while showing feedback

    // ====== FX ======
    @FXML private Label lblLevel;
    @FXML private Label lblWord;
    @FXML private Label lblTime;
    @FXML private Label lblScore;
    @FXML private TextField txtInput;

    /**
     * Initializes the game.
     * <p>
     * Loads the words repository and starts the first level.
     * Sets Enter key action to validate input.
     * </p>
     */
    @FXML
    public void initialize() {
        txtInput.setOnAction(e -> validateInput());
        if (lblScore != null) lblScore.setText("Puntos: " + score);
        startLevel();
    }

    // ====== Time per level ======
    private int timeForLevel(int n) {
        int cuts = (n - 1) / LEVELS_STEP;
        int t = TIME_INITIAL - cuts * TIME_STEP;
        return Math.max(TIME_MIN, t);
    }

    // ====== Level start ======
    private void startLevel() {
        levelClosed = false;
        feedbackOnAir = false;

        if (lblLevel != null) lblLevel.setText("Nivel " + level);

        target = WordRepository.get().random();
        if (lblWord != null) lblWord.setText(target);

        txtInput.clear();
        txtInput.setDisable(false);
        txtInput.setEditable(true);
        txtInput.setStyle(null);
        txtInput.requestFocus();

        startTimer(timeForLevel(level));
    }

    private void startTimer(int seconds) {
        if (timer != null) timer.stop();
        lblTime.setText(String.valueOf(seconds));

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    int current = Integer.parseInt(lblTime.getText());
                    int next = current - 1;
                    lblTime.setText(String.valueOf(next));
                    if (next <= 0) {
                        timer.stop();
                        timeUp();
                    }
                })
        );
        timer.setCycleCount(seconds);
        timer.playFromStart();
    }

    // ====== Validations ======
    private boolean matchesTarget() {
        return txtInput.getText().trim().equals(target);
    }

    private void addPointAndContinue() {
        score++;
        if (lblScore != null) lblScore.setText("Puntos: " + score);

        feedbackOK(() -> {
            level++;
            startLevel();
        });
    }

    // ====== Events ======
    private void validateInput() {
        if (levelClosed || feedbackOnAir) return;

        if (matchesTarget()) {
            if (timer != null) timer.stop(); // avoid race with 0
            levelClosed = true;
            addPointAndContinue();
        } else {
            feedbackBad();
        }
    }

    private void timeUp() {
        if (levelClosed) return;

        if (matchesTarget()) {
            levelClosed = true;
            addPointAndContinue();
        } else {
            levelClosed = true;
            goToGameOver();
        }
    }

    // ====== Feedback ======
    private void feedbackOK(Runnable then) {
        feedbackOnAir = true;
        txtInput.setEditable(false);
        txtInput.setStyle("-fx-text-fill: #00ff00;");
        txtInput.setText("Correcto");

        okPause.stop();
        okPause.setOnFinished(ev -> {
            txtInput.clear();
            txtInput.setStyle(null);
            txtInput.setEditable(true);
            feedbackOnAir = false;
            if (then != null) then.run();
        });
        okPause.playFromStart();
    }

    private void feedbackBad() {
        feedbackOnAir = true;

        String original = txtInput.getStyle();
        txtInput.setStyle("-fx-text-fill: #ff4d4d;");

        badPause.stop();
        badPause.setOnFinished(ev -> {
            txtInput.setStyle(original);
            feedbackOnAir = false;
        });
        badPause.playFromStart();
    }

    // ====== Navigation ======
    private void goToGameOver() {
        try {
            if (timer != null) timer.stop();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameover-view.fxml"));
            Parent root = loader.load();

            GameOverController go = loader.getController();
            go.setScore(score, level);

            Stage stage = (Stage) lblWord.getScene().getWindow();
            stage.setScene(new Scene(root, 720, 540));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
