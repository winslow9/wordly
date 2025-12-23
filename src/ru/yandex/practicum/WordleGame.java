package ru.yandex.practicum;

import custom.exceptions.eniviroment.exceptions.DictionaryIsEmptyException;
import custom.exceptions.game.process.exceptions.ForbidenLanguageException;
import custom.exceptions.game.process.exceptions.WordNotFoundInDictionary;
import custom.exceptions.game.process.exceptions.WrongLengthException;

import java.io.OutputStreamWriter;
import java.util.*;

public class WordleGame {
    static Scanner scanner = new Scanner(System.in);
    Random random = new Random();
    private String answer;
    private static int steps = 5;
    private static boolean isWin = false;
    private WordleDictionary dictionary;
    private OutputStreamWriter logger;
    private String lastResult;
    private HintSystem hintSystem;
    private List<String> allWords;
    private List<String> remainingWords;
    public static int availibleHints = 3;

    public WordleGame() {
        this.hintSystem = new HintSystem();
    }

    public void setDictionary(WordleDictionary dictionary) throws DictionaryIsEmptyException {
        this.dictionary = dictionary;
        this.allWords = dictionary.normalizeDirtyDictionary();

        // Проверяем, что словарь не пустой
        if (allWords.isEmpty()) {
            throw new DictionaryIsEmptyException("Нормализованный словарь пуст");
        }

        this.remainingWords = new ArrayList<>(allWords);
    }

    public void setLogger(OutputStreamWriter writer) {
        this.logger = writer;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void chooseAnswer(WordleDictionary dictionary) throws DictionaryIsEmptyException {
        List<String> normalizedDict = dictionary.normalizeDirtyDictionary();
        if (normalizedDict.isEmpty()) {
            throw new DictionaryIsEmptyException("Словарь пуст, невозможно выбрать слово");
        }
        answer = normalizedDict.get(random.nextInt(normalizedDict.size()));
        resetGameState();
    }

    public void chooseAnswerFromList(List<String> wordList) throws DictionaryIsEmptyException {
        if (wordList == null || wordList.isEmpty()) {
            throw new DictionaryIsEmptyException("Список слов пуст");
        }
        answer = wordList.get(random.nextInt(wordList.size()));
        resetGameState();
    }

    private void resetGameState() {
        hintSystem.reset();
        if (allWords != null) {
            remainingWords = new ArrayList<>(allWords);
        }
    }

    public static boolean isIsWin() {
        return isWin;
    }

    public int getSteps() {
        return steps;
    }

    public static void setSteps(int steps) {
        WordleGame.steps = steps;
    }

    public String getAnswer() {
        return answer;
    }

    public String checkGuess(String guess) throws WrongLengthException, ForbidenLanguageException, WordNotFoundInDictionary {
        guess = guess.toLowerCase();

        // Проверка длины
        if (guess.length() != 5) {
            throw new WrongLengthException("Слово должно содержать 5 букв. Вы ввели: '" + guess + "' (" + guess.length() + " букв)");
        }

        // Проверка языка (русские буквы)
        if (!guess.matches("[а-яё]+")) {
            throw new ForbidenLanguageException("Слово должно содержать только русские буквы. Вы ввели: '" + guess + "'");
        }

        // Проверка наличия в словаре
        if (dictionary != null && !dictionary.normalizeDirtyDictionary().contains(guess)) {
            throw new WordNotFoundInDictionary("Слово '" + guess + "' не найдено в словаре");
        }

        char[] result = new char[5];
        Arrays.fill(result, '-');
        boolean[] usedInAnswer = new boolean[5];

        // Первый проход: точные совпадения
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                usedInAnswer[i] = true;
            }
        }

        // Второй проход: буквы на других позициях
        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') {
                continue;
            }

            char currentChar = guess.charAt(i);

            for (int j = 0; j < 5; j++) {
                if (currentChar == answer.charAt(j) && !usedInAnswer[j]) {
                    result[i] = '^';
                    usedInAnswer[j] = true;
                    break;
                }
            }
        }

        lastResult = new String(result);

        // Обновляем систему подсказок
        if (!guess.equals("")) {
            hintSystem.updateFromResult(guess, lastResult);
            updateRemainingWords();
        }

        if (lastResult.equals("+++++")) {
            isWin = true;
        }

        return lastResult;
    }

    public String getHint() {
        if (remainingWords == null || remainingWords.isEmpty()) {
            return "Нет подходящих слов!";
        }
        if (availibleHints <= 0) {
            return "Подсказок не осталось";
        }
        // Выбираем случайное слово из оставшихся возможных
        String hint = remainingWords.get(random.nextInt(remainingWords.size()));
        availibleHints--;
        return hint;
    }

    public List<String> getPossibleWords() {
        if (remainingWords == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(remainingWords);
    }

    private void updateRemainingWords() {
        if (allWords != null && hintSystem != null) {
            remainingWords = hintSystem.getPossibleWords(allWords);
        }
    }
}