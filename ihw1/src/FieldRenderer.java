public class FieldRenderer {
    private static final String POSSIBLE_MOVE_SYMBOL    = ".";
    private static final String WHITE_CHIP_SYMBOL       = "●";
    private static final String BLACK_CHIP_SYMBOL       = "○";
    private static final String EMPTY_CELL_SYMBOL       = " ";

    public static void printPossibleMoves(GameField field, Color colorForPossibleMoves) {
        var availableCoords = field.getAvailableCoordsToPlaceChip(colorForPossibleMoves);

        if (!availableCoords.isEmpty()) {
            System.out.print("Доступные ходы:");
            for (int i = 0; i < availableCoords.size(); i++) {
                if (i == availableCoords.size() - 1) {
                    System.out.printf(" %s.\n", GameField.coordsToString(availableCoords.get(i)));
                } else {
                    System.out.printf(" %s,", GameField.coordsToString(availableCoords.get(i)));
                }
            }
        }
    }

    public static void drawFieldSimple(GameField field) {
        draw(field, false, null);
    }

    public static void drawFieldWithPossibleMoves(GameField field, Color colorForPossibleMoves) {
        draw(field, true, colorForPossibleMoves);
    }

    private static void draw(GameField field, boolean showPossibleMoves, Color colorForPossibleMoves) {
        for (int i = 0; i < 8; i++) {
            System.out.println("  |-----|-----|-----|-----|-----|-----|-----|-----|");
            System.out.printf("%d |", i + 1);
            for (int j = 0; j < 8; j++) {
                Coords2D coords = new Coords2D(i, j);
                if (showPossibleMoves && field.canPlaceChip(coords, colorForPossibleMoves)) {
                    System.out.printf("  %s  |", POSSIBLE_MOVE_SYMBOL);
                } else {
                    System.out.printf("  %s  |", getCellSymbol(field, coords));
                }
            }
            System.out.println();
        }
        System.out.println("  |-----|-----|-----|-----|-----|-----|-----|-----|");
        System.out.println("     a     b     c     d     e     f     g     h   ");
    }

    private static String getCellSymbol(GameField field, Coords2D coords) {
        var cell = field.getCell(coords);
        if (cell.isEmpty()) {
            return EMPTY_CELL_SYMBOL;
        } else if (cell.getChipColor() == Color.WHITE) {
            return WHITE_CHIP_SYMBOL;
        } else if (cell.getChipColor() == Color.BLACK) {
            return BLACK_CHIP_SYMBOL;
        } else {
            // assert
            assert (false);
            return null;
        }
    }
}
