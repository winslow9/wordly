package ru.yandex.practicum;

import customExceptions.EniviromentExceptions.DictonaryIsNotCreatedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordleDictionaryLoader {

    public WordleDictionaryLoader() {
    }

    // Загрузить полный словарь в список
    public WordleDictionary loadDirtyDictionary() throws DictonaryIsNotCreatedException {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(
                    Paths.get("words_ru.txt"),
                    StandardCharsets.UTF_8
            );
        } catch (IOException ex) {
            throw new DictonaryIsNotCreatedException("Не удалось загрузить словарь из файла: " + ex.getMessage());
        }

        if (lines == null || lines.isEmpty()) {
            throw new DictonaryIsNotCreatedException("Файл словаря пуст или не найден");
        }

        return new WordleDictionary(lines);
    }
}