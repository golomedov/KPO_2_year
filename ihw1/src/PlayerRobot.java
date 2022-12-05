public class PlayerRobot extends Player {
    private boolean isHardLevel;

    public PlayerRobot(Color color, boolean isHardLevel) {
        super(color, "Робот");
        this.isHardLevel = isHardLevel;
    }

    @Override
    public boolean makeNextMove(GameField field) {
        Coords2D bestCoords;
        if (isHardLevel) {
            bestCoords = findBestCoordsHard(field, color);
        } else {
            bestCoords = findBestCoordsSimple(field, color);
        }

        field.placeChip(bestCoords, color);
        return false;
    }

    @Override
    public boolean isRobot() {
        return true;
    }

    private static Coords2D findBestCoordsHard(GameField field, Color playerColor) {
        var availableCoords = field.getAvailableCoordsToPlaceChip(playerColor);

        float bestEvaluation = -1000000;
        int indexOfBest = -1;
        for (int i = 0; i < availableCoords.size(); i++) {
            float evaluation = evaluateMoveHard(field, availableCoords.get(i), playerColor);
            if (evaluation > bestEvaluation) {
                bestEvaluation = evaluation;
                indexOfBest = i;
            }
        }

        return availableCoords.get(indexOfBest);
    }

    private static float evaluateMoveHard(GameField field, Coords2D coords, Color playerColor) {
        float selfEvaluation = evaluateMoveSimple(field, coords, playerColor);
        float enemyBestEvaluation = 0;

        // very smart hack
        field.placeChip(coords, playerColor);
        if (field.canMakeMove(playerColor.getOppositeColor())) {
            enemyBestEvaluation = evaluateMoveSimple(field, findBestCoordsSimple(field, playerColor.getOppositeColor()), playerColor.getOppositeColor());
        }
        field.undoMove(1);
        // ---------------

        return selfEvaluation - enemyBestEvaluation;
    }

    private static Coords2D findBestCoordsSimple(GameField field, Color playerColor) {
        var availableCoords = field.getAvailableCoordsToPlaceChip(playerColor);

        float bestEvaluation = -1;
        int indexOfBest = -1;
        for (int i = 0; i < availableCoords.size(); i++) {
            float evaluation = evaluateMoveSimple(field, availableCoords.get(i), playerColor);
            if (evaluation > bestEvaluation) {
                bestEvaluation = evaluation;
                indexOfBest = i;
            }
        }

        return availableCoords.get(indexOfBest);
    }

    private static float evaluateMoveSimple(GameField field, Coords2D coords, Color playerColor) {
        float result = 0;

        if (GameField.isCorner(coords)) {
            result += 0.8;
        } else if (GameField.isBorder(coords)) {
            result += 0.4;
        }

        var directionsToSwitch = field.getClosingDirections(coords, playerColor);
        for (Coords2D dir : directionsToSwitch) {
            Coords2D processingCoords;
            Cell processingCell;
            int factor = 1;
            while (true) {
                processingCoords = Coords2D.sum(coords, Coords2D.scale(dir, factor));
                if (!GameField.isInField(processingCoords)) {
                    break;
                }

                processingCell = field.getCell(processingCoords);
                if (processingCell.isEmpty()) {
                    break;
                } else if (processingCell.getChipColor().isEqualColor(playerColor)) {
                    break;
                } else {
                    if (GameField.isBorder(processingCoords)) {
                        result += 2;
                    } else {
                        result += 1;
                    }
                }
                factor++;
            }
        }

        return result;
    }
}
