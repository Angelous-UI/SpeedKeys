package com.example.speedkeys;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Repository of playable words loaded from {@code /words.txt}.
 *
 * <p>Implements the <b>Singleton</b> pattern and provides a fast
 * {@link #random()} method to retrieve a random word.</p>
 *
 * <p>Expected file location: {@code src/main/resources/words.txt}</p>
 */
public class WordRepository {

    /** Single global instance (Singleton). */
    private static final WordRepository INSTANCE = new WordRepository();

    /** In-memory list of all words. */
    private final List<String> words = new ArrayList<>();

    /** RNG for {@link #random()}. */
    private final Random rng = new Random();

    /**
     * Loads {@code /words.txt} from the classpath into memory.
     *
     * @throws RuntimeException if the resource is missing, empty, or can't be read
     */
    private WordRepository() {
        try {
            InputStream is = getClass().getResourceAsStream("/words.txt");
            if (is == null) {
                throw new RuntimeException("Resource /words.txt not found on classpath");
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String w = line.trim();
                    if (!w.isEmpty()) words.add(w);
                }
            }
            if (words.isEmpty()) {
                throw new RuntimeException("words.txt is present but contains no words");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load words.txt", e);
        }
    }

    /**
     * Returns the global repository instance.
     *
     * @return the unique {@code WordRepository} instance
     */
    public static WordRepository get() {
        return INSTANCE;
    }

    /**
     * Returns a random word from the repository.
     *
     * @return a random non-empty word
     */
    public String random() {
        return words.get(rng.nextInt(words.size()));
    }
}
