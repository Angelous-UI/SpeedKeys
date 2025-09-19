package com.example.speedkeys;

/**
 * Global game state.
 *
 * <p>Implements the <b>Singleton</b> pattern so only one shared instance exists
 * across the entire application. Tracks current level, score, and time rules.</p>
 */
public class GameState {

    /** Single global instance (Singleton). */
    private static final GameState INSTANCE = new GameState();

    // ================= Time rules =================

    /** Initial time per level (seconds). */
    public static final int TIME_INITIAL = 10;

    /** Minimum time allowed (seconds). */
    public static final int TIME_MIN = 2;

    /** Time discount applied every {@link #LEVELS_STEP} levels (seconds). */
    public static final int TIME_DISCOUNT = 2;

    /** Number of levels after which {@link #TIME_DISCOUNT} is applied. */
    public static final int LEVELS_STEP = 5;

    // ================= Mutable state =================

    /** Current level. Starts at 1. */
    private int level = 1;

    /** Current score. Starts at 0. */
    private int score = 0;

    /** Private constructor to prevent external instantiation. */
    private GameState() {}

    /**
     * Returns the global singleton instance.
     *
     * @return the unique {@code GameState} instance
     */
    public static GameState get() {
        return INSTANCE;
    }

    /**
     * Resets level and score to their initial values.
     * Level = 1, Score = 0.
     */
    public void reset() {
        level = 1;
        score = 0;
    }

    /**
     * Current level getter.
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Current score getter.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /** Increments the score by one. */
    public void addPoint() {
        score++;
    }

    /** Increments the level by one. */
    public void nextLevel() {
        level++;
    }

    /**
     * Computes the time allowed for the current level.
     *
     * <p>Every {@link #LEVELS_STEP} levels, {@link #TIME_DISCOUNT} seconds
     * are subtracted from {@link #TIME_INITIAL}, but the value will never drop
     * below {@link #TIME_MIN}.</p>
     *
     * @return time (in seconds) for the current level
     */
    public int timeForLevel() {
        int cuts = (level - 1) / LEVELS_STEP;
        int t = TIME_INITIAL - cuts * TIME_DISCOUNT;
        return Math.max(TIME_MIN, t);
    }
}
