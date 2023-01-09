package util.io;

import java.io.*;
import java.io.FileReader;
import java.util.Iterator;

/**
 * Построчный итератор на файл.
 */
class FileIterator implements Iterator<String>, Closeable {
    private final BufferedReader reader;

    /**
     * Создает новый FileIterator к заданному файлу.
     *
     * @param path путь к файлу
     * @throws FileNotFoundException если файл не найден
     */
    public FileIterator(String path) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(path));
    }

    /**
     * @return Возвращает информацию, есть ли в файле следующая строка.
     * @throws UncheckedIOException если произошла ошибка ввода-вывода
     */
    @Override
    public boolean hasNext() throws UncheckedIOException {
        try {
            return reader.ready();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @return Возвращает следующую строку файла, или null если файл закончился
     * @throws UncheckedIOException если произошла ошибка ввода-вывода
     */
    @Override
    public String next() throws UncheckedIOException {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Освобождает ресурсы. После вызова close() все
     * попытки вызова next() будут выбрасывать UncheckedIOException
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}
