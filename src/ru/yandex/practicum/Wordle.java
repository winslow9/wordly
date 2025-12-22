package ru.yandex.practicum;

import customExceptions.EniviromentExceptions.CreateLogException;
import customExceptions.EniviromentExceptions.DictionaryIsEmptyException;
import customExceptions.EniviromentExceptions.DictonaryIsNotCreatedException;
import customExceptions.GameProcessExceptions.ForbidenLanguageException;
import customExceptions.GameProcessExceptions.WordNotFoundInDictionary;
import customExceptions.GameProcessExceptions.WrongLengthException;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OutputStreamWriter writer = null;

        try {
            // Пытаемся создать лог-файл
            writer = createLogFile("log.txt");

            System.out.println("Файл log.txt создан");
            System.out.println("Введите слово из 5 букв или нажмите Enter для подсказки");

            WordleDictionaryLoader wdl = new WordleDictionaryLoader();

            try {
                WordleDictionary wd = wdl.loadDirtyDictionary();

                // Проверка на пустой словарь
                if (wd.normalizeDirtyDictionary().isEmpty()) {
                    throw new DictionaryIsEmptyException("Словарь пуст после нормализации");
                }

                WordleGame wg = new WordleGame();

                wg.setLogger(writer);
                wg.setDictionary(wd);
                wg.chooseAnswer(wd);

                // Для тестирования покажем загаданное слово
                System.out.println("(Для тестирования) Загадано слово: " + wg.getAnswer());

                while (wg.getSteps() > 0 && !wg.isIsWin()) {
                    System.out.println("\nОсталось попыток: " + wg.getSteps());
                    System.out.println("Введите слово из 5 букв (или Enter для подсказки):");
                    String guess = scanner.nextLine().trim();

                    if (guess.isEmpty()) {
                        // Запрошена подсказка
                        String hint = wg.getHint();
                        System.out.println("Подсказка: " + hint);
                        continue;
                    }

                    try {
                        String result = wg.checkGuess(guess);
                        System.out.println("Результат: " + result);
                        wg.setSteps(wg.getSteps() - 1);

                        if (wg.isIsWin()) {
                            System.out.println("Поздравляем! Вы угадали слово!");
                            break;
                        }

                    } catch (WrongLengthException e) {
                        writer.write("ОШИБКА: " + e.getMessage() + "\n");
                        writer.flush();
                        System.out.println("Ошибка: " + e.getMessage());
                        System.out.println("У вас осталось " + wg.getSteps() + " попыток.");
                    } catch (ForbidenLanguageException e) {
                        writer.write("ОШИБКА: " + e.getMessage() + "\n");
                        writer.flush();
                        System.out.println("Ошибка: " + e.getMessage());
                    } catch (WordNotFoundInDictionary e) {
                        writer.write("ОШИБКА: " + e.getMessage() + "\n");
                        writer.flush();
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }

                if (!wg.isIsWin()) {
                    System.out.println("\nИгра окончена! У вас закончились попытки.");
                    System.out.println("Загаданное слово было: " + wg.getAnswer());
                }

            } catch (DictionaryIsEmptyException e) {
                if (writer != null) {
                    writer.write("ОШИБКА: " + e.getMessage() + "\n");
                    writer.flush();
                }
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Игра не может начаться без словаря.");
            } catch (DictonaryIsNotCreatedException e) {
                if (writer != null) {
                    writer.write("ОШИБКА: " + e.getMessage() + "\n");
                    writer.flush();
                }
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Невозможно загрузить словарь.");
            }

        } catch (CreateLogException e) {
            System.out.println("Ошибка создания лог-файла: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Закрываем ресурсы
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Ошибка при закрытии лог-файла: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }

    // Метод для создания лог-файла, который бросает CreateLogException
    private static OutputStreamWriter createLogFile(String filename) throws CreateLogException {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            return new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CreateLogException("Не удалось создать лог-файл '" + filename + "': " + e.getMessage());
        }
    }
}