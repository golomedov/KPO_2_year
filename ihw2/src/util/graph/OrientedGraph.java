package util.graph;

import java.util.*;

/**
 * Класс, представляющий ориентированный граф.
 */
public class OrientedGraph {
    /**
     * Enum представляет цвет вершины, используемый при поиске цикла и при топологической сортировке.
     */
    private enum VertexColor {
        WHITE,
        GRAY,
        BLACK
    }

    /**
     * Класс, представляющий вершину графа.
     */
    private class Vertex {
        Vertex(String name) {
            this.name = name;
        }

        public final String name;

        public List<String> incidenceList = new ArrayList<>();

        // Вспомогательная переменная, используемая при поиске цикла и при топологической сортировке.
        public VertexColor color = VertexColor.WHITE;
    }

    private HashMap<String, Vertex> vertexes = new HashMap<>();

    /**
     * Добавляет изолированную вершину в граф.
     * Если вершина уже существует, не выполняет никаких действий.
     *
     * @param name Имя вершины.
     */
    public void addVertex(String name) {
        if (!vertexes.containsKey(name)) {
            vertexes.put(name, new Vertex(name));
        }
    }

    /**
     * Добавляет направленное ребро из вершины source в вершину destination.
     * Если source/destination не существует, добавляет недостающую вершину в граф.
     * Если ребро существует, не выполняет никаких действий.
     *
     * @param source      Имя вершины, из которой выходит направленное ребро.
     * @param destination Имя вершины, в которую приходит направленное ребро.
     */
    public void addDirectedEdge(String source, String destination) {
        addVertex(source);
        addVertex(destination);
        var vertex = vertexes.get(source);
        if (!vertex.incidenceList.contains(destination)) {
            vertex.incidenceList.add(destination);
        }
    }

    /**
     * Сбрасывает цвета вершин для нового обхода в глубину.
     */
    private void resetVertexColors() {
        for (var vertex : vertexes.values()) {
            vertex.color = VertexColor.WHITE;
        }
    }

    /**
     * Проверяет, является ли граф циклическим.
     *
     * @return true - если граф циклический. false - в противном случае.
     */
    public boolean isCyclic() {
        // Будем считать, что WHITE - нетронутая вершина. GRAY - вершина, которую мы обрабатываем.
        // BLACK - обработанная вершина.
        resetVertexColors();

        for (var vertex : vertexes.values()) {
            if (vertex.color == VertexColor.WHITE) {
                if (isCyclicRecursive(vertex)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Рекурсивный метод для поиска цикла в подграфе.
     *
     * @param currentVertex Вершина, для подграфа которой нужно проверить цикличность.
     * @return true - если подграф циклический. false - в противном случае.
     */
    private boolean isCyclicRecursive(Vertex currentVertex) {
        currentVertex.color = VertexColor.GRAY; // Отмечаем вход в вершину.

        for (var destVertexName : currentVertex.incidenceList) {
            var destVertex = vertexes.get(destVertexName);
            if (destVertex.color == VertexColor.WHITE) {
                if (isCyclicRecursive(destVertex)) {
                    return true;
                }
            } else if (destVertex.color == VertexColor.GRAY) {
                return true;
            }
        }

        currentVertex.color = VertexColor.BLACK; // Отмечаем выход из вершины.
        return false;
    }

    /**
     * Топологическая сортировка вершин графа.
     *
     * @return Отсортированный список вершин. Или null, если граф циклический.
     */
    public List<String> topologicalSort() {
        if (isCyclic()) {
            return null;
        }

        // Будем считать, что WHITE - непосещенная вершина, BLACK - посещенная вершина.
        resetVertexColors();

        LinkedList<String> sortedList = new LinkedList<>();

        for (var vertex : vertexes.values()) {
            if (vertex.color == VertexColor.WHITE) {
                topologicalSortRecursive(vertex, sortedList);
            }
        }

        return sortedList;
    }

    /**
     * Вспомогательный метод топологической сортировки.
     *
     * @param currentVertex Вершина, которую мы сейчас обрабатываем.
     * @param sortedList    Список, в который мы добавляем вершины в порядке топологической сортировки.
     */
    private void topologicalSortRecursive(Vertex currentVertex, LinkedList<String> sortedList) {
        currentVertex.color = VertexColor.BLACK; // Отмечаем, что посетили вершину.

        for (var destVertexName : currentVertex.incidenceList) {
            var destVertex = vertexes.get(destVertexName);
            if (destVertex.color == VertexColor.WHITE) {
                topologicalSortRecursive(destVertex, sortedList);
            }
        }

        sortedList.push(currentVertex.name);
    }

    /**
     * Очищает содержимое графа.
     */
    public void clear() {
        vertexes.clear();
    }
}
