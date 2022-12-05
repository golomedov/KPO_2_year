public abstract class Player {
    protected Color color;
    protected String name;

    public Player(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Выполняет следующий ход игрока на игровом поле, либо делает отмену хода.
     * @param field Игровое поле.
     * @return true - если произошел отмена хода. false - если была установлена фишка.
     */
    public abstract boolean makeNextMove(GameField field);

    public abstract boolean isRobot();

    public boolean canMakeMove(GameField field) {
        return field.canMakeMove(color);
    }
}
