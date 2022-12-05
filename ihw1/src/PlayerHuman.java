import java.util.Scanner;

public class PlayerHuman extends Player {
    public PlayerHuman(Color color, String name) {
        super(color, name);
    }

    @Override
    public boolean makeNextMove(GameField field) {
        Coords2D coords = null;
        boolean undoMove = false;

        String command = ConsoleScanner.scanNextGameMove(field, color);
        if (command.equals("undo")) {
            undoMove = true;
        } else {
            coords = GameField.stringToCoords(command);
        }

        if (undoMove) {
            field.undoMove(2);
        } else {
            field.placeChip(coords, color);
        }

        return undoMove;
    }

    @Override
    public boolean isRobot() {
        return false;
    }
}
