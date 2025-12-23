package ru.yandex.practicum;

import java.util.*;

public class HintSystem {
    // Буквы, которые точно есть в слове и их позиции известны
    private Map<Character, Integer> correctPositions;

    // Буквы, которые точно есть в слове, но позиция неизвестна
    private Set<Character> presentLetters;

    // Буквы, которых точно нет в слове
    private Set<Character> absentLetters;

    // Буквы, которые точно не могут быть на определенных позициях
    private Map<Character, Set<Integer>> wrongPositions;

    public HintSystem() {
        correctPositions = new HashMap<>();
        presentLetters = new HashSet<>();
        absentLetters = new HashSet<>();
        wrongPositions = new HashMap<>();
    }

    public void updateFromResult(String guess, String result) {
        for (int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            char resultChar = result.charAt(i);

            if (resultChar == '+') {
                // Правильная буква на правильной позиции
                correctPositions.put(guessChar, i);
                presentLetters.add(guessChar);
            } else if (resultChar == '^') {
                // Буква есть в слове, но на другой позиции
                presentLetters.add(guessChar);
                wrongPositions.putIfAbsent(guessChar, new HashSet<>());
                wrongPositions.get(guessChar).add(i);
            } else if (resultChar == '-') {
                // Буквы нет в слове (или она уже использована где-то еще)
                // Проверяем, не встречалась ли эта буква как присутствующая
                if (!presentLetters.contains(guessChar)) {
                    absentLetters.add(guessChar);
                }
            }
        }
    }

    public List<String> getPossibleWords(List<String> dictionary) {
        List<String> possibleWords = new ArrayList<>();

        for (String word : dictionary) {
            if (isWordPossible(word)) {
                possibleWords.add(word);
            }
        }

        return possibleWords;
    }

    private boolean isWordPossible(String word) {
        // 1. Проверяем правильные позиции
        for (Map.Entry<Character, Integer> entry : correctPositions.entrySet()) {
            char letter = entry.getKey();
            int position = entry.getValue();

            if (word.charAt(position) != letter) {
                return false;
            }
        }

        // 2. Проверяем присутствующие буквы
        for (Character letter : presentLetters) {
            if (word.indexOf(letter) == -1) {
                return false;
            }
        }

        // 3. Проверяем отсутствующие буквы
        for (Character letter : absentLetters) {
            if (word.indexOf(letter) != -1) {
                return false;
            }
        }

        // 4. Проверяем неправильные позиции
        for (Map.Entry<Character, Set<Integer>> entry : wrongPositions.entrySet()) {
            char letter = entry.getKey();
            Set<Integer> forbiddenPositions = entry.getValue();

            // Буква должна быть в слове (уже проверено в presentLetters)
            int index = word.indexOf(letter);
            if (index == -1) continue;

            // Проверяем, что буква не стоит на запрещенных позициях
            for (int pos : forbiddenPositions) {
                if (word.charAt(pos) == letter) {
                    return false;
                }
            }
        }

        return true;
    }

    public void reset() {
        correctPositions.clear();
        presentLetters.clear();
        absentLetters.clear();
        wrongPositions.clear();
    }
}