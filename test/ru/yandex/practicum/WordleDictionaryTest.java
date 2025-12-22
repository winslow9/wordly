package ru.yandex.practicum;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private List<String> testWords;

    @Before
    public void setUp() {
        testWords = Arrays.asList(
                "Стол",
                "стул",
                "окно",
                "ДВЕРЬ",
                "кровать",
                "лампа",
                "  диван  ",
                "",
                null,
                "ёлкаб",
                "ЕЖИК"
        );
        dictionary = new WordleDictionary(testWords);
    }

    @Test
    void WordsAfterNormalizadeTest() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем, что остались только слова из 5 букв
        assertEquals(4, normalized.size()); // стол, стул, окно, дверь, ёлка, ежик (ё заменится на е)
    }

    @Test
    void LowerCaseAfterNormalizadeTest() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем нижний регистр
        assertTrue(normalized.contains("дверь"));
    }

    @Test
    void changeECharTest() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем замену ё на е
        assertTrue(normalized.contains("елкаб"));
    }

    @Test
    void longWordsNotInTest() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем, что длинные слова отфильтрованы
        assertFalse(normalized.contains("кровать"));
    }

    @Test
    void emptyLinesNotInTest() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем, что пустые строки и null отфильтрованы
        assertFalse(normalized.contains(""));
    }
}