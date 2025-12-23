package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordleDictionary implements Iterable<String> {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    // Нормализовать словарь под нижный регистр, поменять е на ё и 5 символов
    public List<String> normalizeDirtyDictionary() {
        List<String> normalized = new ArrayList<>();

        for (String word : words) {
            if (word != null && !word.trim().isEmpty()) {
                String normalizedWord = word.trim().toLowerCase();
                normalizedWord = normalizedWord.replace('ё', 'е');

                if (normalizedWord.length() == 5) {
                    normalized.add(normalizedWord);
                }
            }
        }

        return normalized;
    }

    // Проверка содержит ли словарь слово
    public boolean contains(String word) {
        if (word == null) return false;
        String normalizedWord = word.toLowerCase().replace('ё', 'е');
        return normalizeDirtyDictionary().contains(normalizedWord);
    }

    @Override
    public Iterator<String> iterator() {
        return words.iterator();
    }
}