package com.example.speedkeys;

/**
 * Represents the global state of the game.
 * <p>
 * Implements the <b>Singleton</b> pattern to ensure there is only
 * one shared instance across the entire application.
 * Stores dynamic data such as the current level, score,
 * and timing rules.
 * </p>
 */
public class GameState {

    /** Unique instance of the game state (Singleton). */
    private static final GameState INSTANCE = new GameState();

    // ================== Time configuration ==================

    /** Initial time (in seconds) at the start of the game. */
    public static final int TIEMPO_INICIAL = 10;

    /** Minimum allowed time per level (in seconds). */
    public static final int TIEMPO_MINIMO  = 2;

    /** Time discount (in seconds) applied every few levels. */
    public static final int DESCUENTO      = 2;

    /** Number of levels after which the time discount is applied. */
    public static final int CADA_N_NIVELES = 5;

    // ================== Dynamic state ==================

    /** Current level of the player. */
    private int nivel = 1;

    /** Player's accumulated points. */
    private int puntos = 0;

    /**
     * Private constructor to prevent external instantiation.
     * (Singleton pattern).
     */
    private GameState() {}

    /**
     * Returns the single global instance of the game state.
     *
     * @return global {@link GameState} instance.
     */
    public static GameState get() {
        return INSTANCE;
    }

    /**
     * Resets the game state to its initial values.
     * <p>
     * Sets level = 1 and points = 0.
     * </p>
     */
    public void reset() {
        nivel = 1;
        puntos = 0;
    }

    /**
     * Gets the current level.
     *
     * @return the current level.
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Gets the current points.
     *
     * @return the accumulated points.
     */
    public int getPuntos() {
        return puntos;
    }

    /**
     * Increases the score by 1 point.
     */
    public void sumarPunto() {
        puntos++;
    }

    /**
     * Increases the current level by 1.
     */
    public void subirNivel() {
        nivel++;
    }

    /**
     * Calculates the available time based on the current level.
     * <p>
     * Every {@link #CADA_N_NIVELES} levels, {@link #DESCUENTO} seconds
     * are subtracted, but the result will never go below {@link #TIEMPO_MINIMO}.
     * </p>
     *
     * @return available time (in seconds) for the current level.
     */
    public int tiempoParaNivel() {
        int recortes = (nivel - 1) / CADA_N_NIVELES;
        int t = TIEMPO_INICIAL - recortes * DESCUENTO;
        return Math.max(TIEMPO_MINIMO, t);
    }
}
