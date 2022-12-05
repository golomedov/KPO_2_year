import java.util.Scanner;

public class ConsoleScanner {
    private static final Scanner scanner = new Scanner(System.in);

    public static String scanCommand(String[] availableCommands) {
        String inputCommand = "";
        boolean validCommandEntered = false;

        System.out.print("Введите команду.\n");
        while (!validCommandEntered) {
            System.out.print("Доступные команды:");
            for (int i = 0; i < availableCommands.length; i++) {
                if (i == availableCommands.length - 1) {
                    System.out.printf(" '%s'.", availableCommands[i]);
                } else {
                    System.out.printf(" '%s',", availableCommands[i]);
                }
            }
            System.out.println();

            inputCommand = scanner.nextLine();

            for (var command : availableCommands) {
                if (inputCommand.equals(command)) {
                    validCommandEntered = true;
                }
            }

            if (!validCommandEntered) {
                System.out.println("Некорректный ввод. Попробуйте еще раз.");
            }
        }

        return inputCommand;
    }

    public static String scanNextGameMove(GameField field, Color color) {
        Coords2D coords;
        String command;

        while (true) {
            System.out.println("Введите координаты в формате 'f5' или 'undo' для отмены последнего хода обоих игроков.");
            command = scanner.nextLine();

            if (command.equals("undo")) {
                if (field.canUndoMove(2)) {
                    break;
                } else {
                    System.out.println("Вы пока не сделали ни одного хода, отмена невозможна. Попробуйте еще раз.");
                    continue;
                }
            }

            coords = GameField.stringToCoords(command);
            if (coords == null || !GameField.isInField(coords)) {
                System.out.println("Некорректный ввод. Попробуйте еще раз.");
            } else if (!field.canPlaceChip(coords, color)) {
                System.out.println("Невозможно установить фишку в выбранную ячейку. Попробуйте еще раз.");
            } else {
                break;
            }
        }

        return command;
    }
}
