import java.util.ArrayList;
import java.util.Stack;

public class GameField {
    private Stack<Cell[][]> stateHistory;
    private Cell[][] currentState;

    GameField() {
        stateHistory = new Stack<Cell[][]>();
        currentState = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentState[i][j] = new Cell();
            }
        }
    }

    public void setupStartState() {
        stateHistory.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentState[i][j].setEmpty();
            }
        }
        setChipForce(new Coords2D(3, 3), Color.WHITE);
        setChipForce(new Coords2D(4, 4), Color.WHITE);
        setChipForce(new Coords2D(3, 4), Color.BLACK);
        setChipForce(new Coords2D(4, 3), Color.BLACK);
    }

    public Cell getCell(Coords2D coords) {
        return currentState[coords.x][coords.y];
    }

    /**
     * Нахождение направлений в формате (0, 1) для которых будут замыкания при установке фишки.
     * @param coords Позиция, на которую устанавливается фишка.
     * @param color Цвет фишки.
     * @return Массив направлений.
     */
    public ArrayList<Coords2D> getClosingDirections(Coords2D coords, Color color) {
        ArrayList<Coords2D> result = new ArrayList<Coords2D>();

        for (Coords2D dir : DIRECTIONS) {
            Coords2D processingCoords = Coords2D.sum(coords, dir);
            if (isInField(processingCoords)) {
                Cell processingCell = getCell(processingCoords);
                if (!processingCell.isEmpty() && processingCell.getChipColor().isOppositeColor(color)) {
                    int factor = 2;
                    while (true) {
                        processingCoords = Coords2D.sum(coords, Coords2D.scale(dir, factor));
                        if (!isInField(processingCoords)) {
                            break;
                        }

                        processingCell = getCell(processingCoords);
                        if (processingCell.isEmpty()) {
                            break;
                        } else if (processingCell.getChipColor() == color) {
                            result.add(dir);
                            break;
                        }

                        factor++;
                    }
                }
            }
        }

        return result;
    }

    public boolean canPlaceChip(Coords2D coords, Color color) {
        if (!getCell(coords).isEmpty()) {
            return false;
        }

        return !getClosingDirections(coords, color).isEmpty();
    }

    public void placeChip(Coords2D coords, Color color) {
        if (!canPlaceChip(coords, color)) {
            return;
        }

        stateHistory.push(getCopyOfCurrentState());

        setChipForce(coords, color);

        var directionsToSwitch = getClosingDirections(coords, color);
        for (Coords2D dir : directionsToSwitch) {
            Coords2D processingCoords;
            Cell processingCell;
            int factor = 1;
            while (true) {
                processingCoords = Coords2D.sum(coords, Coords2D.scale(dir, factor));
                if (!isInField(processingCoords)) {
                    break;
                }

                processingCell = getCell(processingCoords);
                if (processingCell.isEmpty()) {
                    break;
                } else if (processingCell.getChipColor().isEqualColor(color)) {
                    break;
                } else {
                    processingCell.switchChip();
                }

                factor++;
            }
        }
    }

    public boolean canUndoMove(int amount) {
        return stateHistory.size() >= amount;
    }

    public void undoMove(int amount) {
        if (canUndoMove(amount)) {
            for (int i = 0; i < amount - 1; i++) {
                stateHistory.pop();
            }
            currentState = stateHistory.pop();
        }
    }

    public boolean isFilled() {
        for (Cell[] line : currentState) {
            for (Cell cell : line) {
                if (cell.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public FieldStats getFieldStats() {
        FieldStats result = new FieldStats();

        for (Cell[] line : currentState) {
            for (Cell cell : line) {
                if (!cell.isEmpty()) {
                    if (cell.getChipColor() == Color.WHITE) {
                        result.whiteChips++;
                    } else {
                        result.blackChips++;
                    }
                }
            }
        }

        return result;
    }

    public ArrayList<Coords2D> getAvailableCoordsToPlaceChip(Color color) {
        ArrayList<Coords2D> result = new ArrayList<Coords2D>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coords2D coords = new Coords2D(i, j);
                if (canPlaceChip(coords, color)) {
                    result.add(coords);
                }
            }
        }

        return result;
    }

    public boolean canMakeMove(Color color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canPlaceChip(new Coords2D(i, j), color)) {
                    return true;
                }
            }
        }

        return false;
    }

    /* ===== static ===== */

    private static final Coords2D[] DIRECTIONS;

    static {
        DIRECTIONS = new Coords2D[8];
        DIRECTIONS[0] = new Coords2D(0, 1);
        DIRECTIONS[1] = new Coords2D(0, -1);
        DIRECTIONS[2] = new Coords2D(1, 0);
        DIRECTIONS[3] = new Coords2D(1, 1);
        DIRECTIONS[4] = new Coords2D(1, -1);
        DIRECTIONS[5] = new Coords2D(-1, 0);
        DIRECTIONS[6] = new Coords2D(-1, 1);
        DIRECTIONS[7] = new Coords2D(-1, -1);
    }

    public static String coordsToString(Coords2D coords) {
        StringBuilder result = new StringBuilder();
        result.append((char) ('a' + coords.y));
        result.append((char) ('1' + coords.x));
        return result.toString();
    }

    public static Coords2D stringToCoords(String str) {
        if (str.length() != 2) {
            return null;
        }

        Coords2D result = new Coords2D(str.charAt(1) - '1', str.charAt(0) - 'a');
        if (result.x < 0 || result.x >= 8 || result.y < 0 || result.y >= 8) {
            return null;
        }

        return result;
    }

    public static boolean isCorner(Coords2D coords) {
        return (coords.x == 0 || coords.x == 7) && (coords.y == 0 || coords.y == 7);
    }

    public static boolean isBorder(Coords2D coords) {
        return coords.x == 0 || coords.x == 7 || coords.y == 0 || coords.y == 7;
    }

    public static boolean isInField(Coords2D coords) {
        return coords.x >= 0 && coords.x < 8 && coords.y >= 0 && coords.y < 8;
    }

    /* ===== private ===== */

    /**
     * Создает глубокую копию двумерного массива клеток.
     * @return Глубокая копия двумерного массива клеток.
     */
    private Cell[][] getCopyOfCurrentState() {
        Cell[][] copy = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = currentState[i][j].getDeepCopy();
            }
        }

        return copy;
    }

    private void setChipForce(Coords2D coords, Color color) {
        getCell(coords).setChip(color);
    }
}