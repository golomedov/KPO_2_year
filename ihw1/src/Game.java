public class Game {
    private GameField field;
    private Color currentPlayerColor;
    private GameMode gameMode;
    private boolean isHardMode;

    private Player playerWhite;
    private Player playerBlack;

    public void chooseGameMode() {
        System.out.print("Выберите режим игры.\n'pvp' - игрок против игрока\n'pve' - игрок против робота.\n");
        String command = ConsoleScanner.scanCommand(new String[]{"pvp", "pve"});
        if (command.equals("pvp")) {
            gameMode = GameMode.PVP;
        } else if (command.equals("pve")) {
            gameMode = GameMode.PVE;
        }

        if (gameMode == GameMode.PVE) {
            System.out.println("Выберите сложность.");
            command = ConsoleScanner.scanCommand(new String[]{"easy", "hard"});
            if (command.equals("easy")) {
                isHardMode = false;
            } else if (command.equals("hard")) {
                isHardMode = true;
            }
        }
    }

    public void mainLoop() {
        if (gameMode == GameMode.PVP) {
            playerBlack = new PlayerHuman(Color.BLACK, "Игрок 1");
            playerWhite = new PlayerHuman(Color.WHITE, "Игрок 2");
        } else if (gameMode == GameMode.PVE) {
            playerBlack = new PlayerHuman(Color.BLACK, "Игрок");
            playerWhite = new PlayerRobot(Color.WHITE, isHardMode);
        }

        field = new GameField();

        boolean continueGame = true;
        int bestResult = 0;
        while (continueGame) {
            int result = runGameSession();
            bestResult = Integer.max(bestResult, result);

            System.out.print("Желаете сыграть еще раз?\n");
            String command = ConsoleScanner.scanCommand(new String[]{"yes", "no"});

            if (command.equals("yes")) {
                continueGame = true;
            } else if (command.equals("no")) {
                continueGame = false;
            }
        }

        System.out.printf("Лучший результат за сессию: %d б.\n", bestResult);
    }

    /**
     * Запуск одной игровой партии.
     * @return При PVP - максимальное количество очков. При PVE - счет игрока.
     */
    private int runGameSession() {
        currentPlayerColor = Color.BLACK;
        field.setupStartState();

        while (true) {
            if (getCurrentPlayer().isRobot()) {
                FieldRenderer.drawFieldSimple(field);
            } else {
                FieldRenderer.drawFieldWithPossibleMoves(field, currentPlayerColor);
                FieldRenderer.printPossibleMoves(field, currentPlayerColor);
            }

            if (field.isFilled()) {
                System.out.println("Поле заполнено. Игра завершена.");
                break;
            }
            if (!playerWhite.canMakeMove(field) && !playerBlack.canMakeMove(field)) {
                System.out.println("Ни один игрок не может сделать ход. Игра завершена.");
                break;
            }

            System.out.printf("Ходит %s.\n", getCurrentPlayer().getName());

            if (!getCurrentPlayer().canMakeMove(field)) {
                System.out.printf("%s не имеет доступных ходов. Ход переходит дальше.\n", getCurrentPlayer().getName());
                switchPlayer();
            } else {
                boolean lastMoveCancelled = getCurrentPlayer().makeNextMove(field);
                if (!lastMoveCancelled) {
                    switchPlayer();
                }
            }
        }

        var stats = field.getFieldStats();
        System.out.println("Результаты игры:");
        System.out.printf("%s: %d б. %s: %d б.\n", playerWhite.getName(), stats.whiteChips, playerBlack.getName(), stats.blackChips);
        if (stats.whiteChips > stats.blackChips) {
            System.out.printf("Победил %s.\n", playerWhite.getName());
        } else if (stats.whiteChips < stats.blackChips) {
            System.out.printf("Победил %s.\n", playerBlack.getName());
        } else {
            System.out.println("Ничья.");
        }

        if (gameMode == GameMode.PVE) {
            return stats.blackChips;
        } else {
            return Integer.max(stats.blackChips, stats.whiteChips);
        }
    }

    private Player getCurrentPlayer() {
        if (currentPlayerColor == Color.WHITE) {
            return playerWhite;
        } else {
            return playerBlack;
        }
    }

    private void switchPlayer() {
        currentPlayerColor = currentPlayerColor.getOppositeColor();
    }
}
