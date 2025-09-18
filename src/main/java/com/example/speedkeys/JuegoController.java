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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main controller for the typing game screen.
 * <p>
 * Handles level progression, timer, random word selection, user input validation,
 * feedback messages, points, and navigation to the Game Over screen.
 * </p>
 */
public class JuegoController {

    // ====== Constants ======

    /** Initial time per level (seconds). */
    private static final int TIEMPO_INICIAL = 20;
    /** Minimum allowed time per level (seconds). */
    private static final int TIEMPO_MINIMO  = 2;
    /** Time reduction applied every {@link #CADA_N_NIVELES} levels (seconds). */
    private static final int DESCUENTO      = 2;
    /** Number of levels after which the time is reduced. */
    private static final int CADA_N_NIVELES = 5;

    // ====== State ======

    /** Current level. */
    private int nivel = 1;
    /** Player points. */
    private int puntos = 0;
    /** Current target word to type. */
    private String objetivo = "";

    /** Level countdown timeline. */
    private Timeline temporizador;

    /** Short pause shown after a correct answer. */
    private final PauseTransition pausaCorrecto   = new PauseTransition(Duration.seconds(0.5));
    /** Short pause shown after an incorrect attempt. */
    private final PauseTransition pausaIncorrecto = new PauseTransition(Duration.seconds(0.5));

    /**
     * Lock: becomes {@code true} when the level is already resolved
     * (either success or game over) to avoid race conditions.
     */
    private boolean nivelCerrado = false;

    /**
     * Lock: {@code true} while feedback (correct/incorrect) is being shown
     * to prevent Enter spamming.
     */
    private boolean feedbackActivo = false;

    /** Random generator for picking words. */
    private final Random rng = new Random();
    /** Word list loaded from {@code /words.txt}. */
    private final List<String> palabras = new ArrayList<>();

    // ====== FXML references ======

    /** Label that shows the current level. */
    @FXML private Label lblNivel;
    /** Label that shows the current target word. */
    @FXML private Label lblPalabra;
    /** Label that shows the remaining time. */
    @FXML private Label lblTiempo;
    /** Label that shows the current points. */
    @FXML private Label lblPuntos;
    /** Text field where the user types the word. */
    @FXML private TextField txtEntrada;

    /**
     * Initializes the controller after the FXML is loaded.
     * <ul>
     *   <li>Loads the word list from resources.</li>
     *   <li>Binds Enter key to validation.</li>
     *   <li>Initializes the points label.</li>
     *   <li>Starts the first level.</li>
     * </ul>
     */
    @FXML
    public void initialize() {
        cargarPalabras();

        // Enter validates the current input
        txtEntrada.setOnAction(e -> validarEntrada());

        if (lblPuntos != null) lblPuntos.setText("Puntos: " + puntos);

        iniciarNivel();
    }

    // ====== Data loading ======

    /**
     * Loads words from {@code /words.txt} (one per line) into {@link #palabras}.
     * Throws a runtime exception if the file is missing or empty.
     */
    private void cargarPalabras() {
        if (!palabras.isEmpty()) return;

        try (InputStream is = getClass().getResourceAsStream("/words.txt")) {
            if (is == null) throw new RuntimeException("No pude encontrar /words.txt en resources");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String w = linea.trim();
                    if (!w.isEmpty()) palabras.add(w);
                }
            }
            if (palabras.isEmpty()) throw new RuntimeException("words.txt está vacío");
        } catch (Exception e) {
            throw new RuntimeException("Error cargando words.txt", e);
        }
    }

    // ====== Time logic ======

    /**
     * Computes the time (in seconds) for a given level.
     * Reduces {@link #DESCUENTO} seconds every {@link #CADA_N_NIVELES} levels,
     * never going below {@link #TIEMPO_MINIMO}.
     *
     * @param n level number (1-based)
     * @return allowed time in seconds
     */
    private int tiempoParaNivel(int n) {
        int recortes = (n - 1) / CADA_N_NIVELES;
        int t = TIEMPO_INICIAL - recortes * DESCUENTO;
        return Math.max(TIEMPO_MINIMO, t);
    }

    // ====== Level lifecycle ======

    /**
     * Prepares and starts a new level:
     * <ul>
     *   <li>Resets locks and UI styles.</li>
     *   <li>Chooses a random target word.</li>
     *   <li>Focuses the input field.</li>
     *   <li>Starts the level timer.</li>
     * </ul>
     */
    private void iniciarNivel() {
        nivelCerrado = false;
        feedbackActivo = false;

        if (lblNivel != null) lblNivel.setText("Nivel " + nivel);

        objetivo = palabras.get(rng.nextInt(palabras.size()));
        if (lblPalabra != null) lblPalabra.setText(objetivo);

        txtEntrada.clear();
        txtEntrada.setDisable(false);
        txtEntrada.setEditable(true);
        txtEntrada.setStyle(null);
        txtEntrada.requestFocus();

        iniciarTemporizador(tiempoParaNivel(nivel));
    }

    /**
     * Starts the per-level countdown timeline.
     *
     * @param segundos seconds to count down
     */
    private void iniciarTemporizador(int segundos) {
        if (temporizador != null) temporizador.stop();

        lblTiempo.setText(String.valueOf(segundos));

        temporizador = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    int actual = Integer.parseInt(lblTiempo.getText());
                    int nuevo  = actual - 1;
                    lblTiempo.setText(String.valueOf(nuevo));
                    if (nuevo <= 0) {
                        temporizador.stop();
                        finalizarPorTiempo();
                    }
                })
        );
        temporizador.setCycleCount(segundos);
        temporizador.playFromStart();
    }

    // ====== Validation helpers ======

    /**
     * Checks if the user's input exactly matches the current {@link #objetivo}.
     *
     * @return {@code true} if input equals target word; {@code false} otherwise
     */
    private boolean coincideConObjetivo() {
        return txtEntrada.getText().trim().equals(objetivo);
    }

    /**
     * Adds one point, shows "correct" feedback, then advances to the next level.
     */
    private void sumarPuntoYSeguir() {
        puntos++;
        if (lblPuntos != null) lblPuntos.setText("Puntos: " + puntos);

        // correct feedback and go to the next level
        feedbackCorrecto(() -> {
            nivel++;
            iniciarNivel();
        });
    }

    // ====== Events ======

    /**
     * Triggered by pressing Enter:
     * <ul>
     *   <li>Ignores input if a feedback is active or the level is already closed.</li>
     *   <li>If input matches, stops the timer and advances.</li>
     *   <li>Otherwise, shows incorrect feedback.</li>
     * </ul>
     */
    private void validarEntrada() {
        if (nivelCerrado || feedbackActivo) return;

        if (coincideConObjetivo()) {
            if (temporizador != null) temporizador.stop(); // avoid race with 0
            nivelCerrado = true;
            sumarPuntoYSeguir();
        } else {
            feedbackIncorrecto();
        }
    }

    /**
     * Called when time reaches zero:
     * <ul>
     *   <li>If the current text equals the target word, counts as success (auto-accepts).</li>
     *   <li>Otherwise, navigates to Game Over.</li>
     * </ul>
     */
    private void finalizarPorTiempo() {
        if (nivelCerrado) return;

        if (coincideConObjetivo()) {
            nivelCerrado = true;
            sumarPuntoYSeguir();
        } else {
            nivelCerrado = true;
            irAGameOver();
        }
    }

    // ====== Feedback ======

    /**
     * Shows "Correcto" in green for 0.5s (input disabled), then runs {@code luego}.
     *
     * @param luego callback to execute after the feedback ends (e.g., start next level)
     */
    private void feedbackCorrecto(Runnable luego) {
        feedbackActivo = true;
        txtEntrada.setEditable(false);
        txtEntrada.setStyle("-fx-text-fill: #00ff00;");
        txtEntrada.setText("Correcto");

        pausaCorrecto.stop();
        pausaCorrecto.setOnFinished(ev -> {
            txtEntrada.clear();
            txtEntrada.setStyle(null);
            txtEntrada.setEditable(true);
            feedbackActivo = false;
            if (luego != null) luego.run();
        });
        pausaCorrecto.playFromStart();
    }

    /**
     * Shows a short red blink (0.5s) without altering the user's text.
     * Prevents Enter spamming while the feedback is active.
     */
    private void feedbackIncorrecto() {
        feedbackActivo = true;

        String estiloOriginal = txtEntrada.getStyle();
        txtEntrada.setStyle("-fx-text-fill: #ff4d4d;");

        pausaIncorrecto.stop();
        pausaIncorrecto.setOnFinished(ev -> {
            txtEntrada.setStyle(estiloOriginal);
            feedbackActivo = false;
        });
        pausaIncorrecto.playFromStart();

        // If you prefer to temporarily show "Incorrecto" instead,
        // replace with the commented block below (be careful with race at t=0).
        /*
        String anterior = txtEntrada.getText();
        txtEntrada.setStyle("-fx-text-fill: #ff4d4d;");
        txtEntrada.setText("Incorrecto");
        pausaIncorrecto.stop();
        pausaIncorrecto.setOnFinished(ev -> {
            txtEntrada.setStyle(null);
            txtEntrada.setText(anterior);
            txtEntrada.positionCaret(anterior.length());
            feedbackActivo = false;
        });
        pausaIncorrecto.playFromStart();
        */
    }

    // ====== Navigation ======

    /**
     * Navigates to the Game Over screen and passes the final score and level.
     * Stops the timer if running.
     */
    private void irAGameOver() {
        try {
            if (temporizador != null) temporizador.stop();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("gameover-view.fxml"));
            Parent root = loader.load();

            GameOverController go = loader.getController();
            go.setPuntos(puntos, nivel);

            Stage stage = (Stage) lblPalabra.getScene().getWindow();
            stage.setScene(new Scene(root, 720, 540));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
