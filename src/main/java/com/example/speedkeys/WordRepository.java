package com.example.speedkeys;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Repository for managing and providing random words for the game.
 * <p>
 * Implements the <b>Singleton</b> pattern to ensure that the word list is loaded
 * only once from the {@code words.txt} resource file. All controllers can then
 * access the same instance to retrieve random words.
 * </p>
 */
public class WordRepository {

    /** The single shared instance of this repository (Singleton). */
    private static final WordRepository INSTANCE = new WordRepository();

    /** Internal list containing all words loaded from the resource file. */
    private final List<String> palabras = new ArrayList<>();

    /** Random generator used for selecting random words. */
    private final Random rng = new Random();

    /**
     * Private constructor that loads the words from {@code /words.txt} located in resources.
     * <p>
     * Each line in the file is treated as a separate word. Empty lines are ignored.
     * </p>
     *
     * @throws RuntimeException if the file is missing, empty, or cannot be read.
     */
    private WordRepository() {
        try {
            InputStream is = getClass().getResourceAsStream("/words.txt");
            if (is == null) {
                throw new RuntimeException("Could not find /words.txt in resources");
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String w = line.trim();
                    if (!w.isEmpty()) palabras.add(w);
                }
            }
            if (palabras.isEmpty()) {
                throw new RuntimeException("words.txt is empty");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading words.txt", e);
        }
    }

    /**
     * Provides the single global instance of the repository.
     *
     * @return the shared {@link WordRepository} instance.
     */
    public static WordRepository get() {
        return INSTANCE;
    }

    /**
     * Returns a random word from the repository.
     *
     * @return a random word string.
     */
    public String random() {
        return palabras.get(rng.nextInt(palabras.size()));
    }
}
