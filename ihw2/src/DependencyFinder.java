import util.graph.OrientedGraph;
import util.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Класс, позволяющий искать зависимости между файлами в коренном каталоге и всех подкаталогах.
 */
public class DependencyFinder {
    private String rootDirPath;
    private final OrientedGraph graph = new OrientedGraph();

    public DependencyFinder(String rootDirPath) {
        this.rootDirPath = rootDirPath;
    }

    /**
     * Сеттер для пути к коренному каталогу.
     *
     * @param rootDirPath Путь к коренному каталогу.
     */
    public void setRootDirPath(String rootDirPath) {
        this.rootDirPath = rootDirPath;
    }

    /**
     * Склеивает все файлы корневого каталога и всех подкаталогов в порядке их зависимости друг от друга.
     * Записывает результат в указанный файл. Если выходной файл непуст, перезаписывает его.
     * Если выходного файла не существует, создает его.
     * Если присутствует циклическая зависимость, не записывает ничего в выходной файл.
     * Если путь коренного каталога некорректный, не записывает ничего в выходной файл.
     *
     * @param outputFilePath Путь до выходного файла.
     * @return true - в случае успеха. false - если действие невозможно.
     * @throws IOException В случае ошибки ввода-вывода.
     */
    public boolean concatFilesByDependenciesOrder(String outputFilePath) throws IOException {
        if (!isDirPathCorrect(rootDirPath)) {
            System.out.printf("Fatal error. Invalid root directory path: %s\n", rootDirPath);
            return false;
        }

        rebuildGraph();
        if (graph.isCyclic()) {
            System.out.println("Fatal error. Cyclic dependence was found.");
            return false;
        }

        var sortedFileList = graph.topologicalSort();

        File outputFile = new File(outputFilePath);
        outputFile.delete();
        outputFile.createNewFile();
        for (var filePath : sortedFileList) {
            FileUtils.writeAllLines(outputFilePath, FileUtils.getAllLines(filePath));
        }

        return true;
    }

    private boolean isFilePathCorrect(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    private boolean isDirPathCorrect(String path) {
        File dir = new File(path);
        return dir.exists() && dir.isDirectory();
    }

    /**
     * Перестаривает граф зависимостей файлов.
     */
    private void rebuildGraph() {
        graph.clear();
        processDirectoryForGraphBuilding(new File(rootDirPath));
    }

    /**
     * Метод для обработки очередного каталога в процессе построения графа зависимостей.
     *
     * @param currentDir Каталог для обработки.
     */
    private void processDirectoryForGraphBuilding(File currentDir) {
        var subDirs = currentDir.listFiles(File::isDirectory);
        if (subDirs != null) {
            for (var subDir : subDirs) {
                processDirectoryForGraphBuilding(subDir);
            }
        }

        var files = currentDir.listFiles((curDir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (var file : files) {
                try {
                    processFileForGraphBuilding(file);
                } catch (SecurityException e) {
                    System.out.printf("Error. Can't read file: %s\n", file.getAbsolutePath());
                } catch (IOException e) {
                    System.out.printf("Error. Unknown error with file: %s\n", file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Метод для обработки очередного файла в процессе построения графа зависимостей.
     *
     * @param currentFile Файл для обработки.
     * @throws IOException В случае ошибки ввода-вывода.
     */
    private void processFileForGraphBuilding(File currentFile) throws IOException {
        graph.addVertex(currentFile.getAbsolutePath());

        var relPaths = FileUtils.getAllRequires(currentFile.getAbsolutePath());
        for (var relPath : relPaths) {
            String absPath = getAbsPath(relPath);
            if (!isFilePathCorrect(absPath)) {
                System.out.printf("Error. Incorrect file path: %s\n", absPath);
                continue;
            }

            File requiredFile = new File(absPath);
            graph.addDirectedEdge(requiredFile.getAbsolutePath(), currentFile.getAbsolutePath());
        }
    }

    /**
     * Метод, создающий абсолютный путь на основе пути коренного каталога и относительного пути.
     *
     * @param relPath Относительный путь.
     * @return Абсолютный путь.
     */
    private String getAbsPath(String relPath) {
        return rootDirPath + File.separator + relPath;
    }
}
