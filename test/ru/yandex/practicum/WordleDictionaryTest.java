package ru.yandex.practicum;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WordleDictionaryTest {

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
    public void testWordsAfterNormalization() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем, что остались только слова из 5 букв
        assertEquals("После нормализации должно быть 4 слова", 4, normalized.size());
    }

    @Test
    public void testLowerCaseAfterNormalization() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем нижний регистр
        assertTrue("Слово 'дверь' должно быть в нижнем регистре", normalized.contains("дверь"));
    }

    @Test
    public void testChangeEChar() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем замену ё на е
        assertTrue("'ёлкаб' должен стать 'елкаб'", normalized.contains("елкаб"));
    }

    @Test
    public void testLongWordsNotInResult() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем, что длинные слова отфильтрованы
        assertFalse("Слово 'кровать' не должно быть в результате", normalized.contains("кровать"));
    }

    @Test
    public void testEmptyLinesNotInResult() {
        List<String> normalized = dictionary.normalizeDirtyDictionary();
        // Проверяем, что пустые строки и null отфильтрованы
        assertFalse("Пустая строка не должна быть в результате", normalized.contains(""));
    }

    @Test
    public void testContainsMethod() {
        // Проверяем существующие слова
        assertTrue("Должно находить 'дверь'", dictionary.contains("дверь"));
        assertTrue("Должно находить 'ДВЕРЬ'", dictionary.contains("ДВЕРЬ"));
        assertTrue("Должно находить 'ёлкаб'", dictionary.contains("ёлкаб"));
        assertTrue("Должно находить 'елкаб' (после замены ё)", dictionary.contains("елкаб"));

        // Проверяем отсутствующие слова
        assertFalse("Не должно находить 'кровать'", dictionary.contains("кровать"));
        assertFalse("Не должно находить пустую строку", dictionary.contains(""));
        assertFalse("Не должно находить null", dictionary.contains(null));
    }
}