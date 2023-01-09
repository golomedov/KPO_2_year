package util.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс предоставляет набор методов для чтения из файлов.
 */
public class FileUtils {
    /**
     * Считывает все строки из заданного файла.
     *
     * @param path Путь к файлу.
     * @return Список строк файла.
     * @throws IOException При ошибке ввода-вывода.
     */
    public static List<String> getAllLines(String path) throws IOException {
        List<String> result = new ArrayList<>();

        try (FileIterator iterator = new FileIterator(path)) {
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
        }

        return result;
    }

    /**
     * Находит в файле все директивы формата require "argument", которые записаны с новой строки.
     *
     * @param path Путь к файлу.
     * @return Список аргументов директив require.
     * @throws IOException При ошибке ввода-вывода.
     */
    public static List<String> getAllRequires(String path) throws IOException {
        List<String> result = new ArrayList<>();

        try (FileIterator iterator = new FileIterator(path)) {
            while (iterator.hasNext()) {
                String currentLine = iterator.next().trim();
                if (currentLine.length() >= 7 && currentLine.substring(0, 7).equals("require")) {
                    currentLine = currentLine.substring(7).trim();
                    if (currentLine.length() >= 2 && currentLine.charAt(0) == '"' && currentLine.charAt(currentLine.length() - 1) == '"') {
                        currentLine = currentLine.substring(1, currentLine.length() - 1);
                        result.add(currentLine);
                    }
                }
            }
        }

        return result;
    }
}
