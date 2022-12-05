public class Cell {
    private Chip chip = null;

    public void setChip(Color color) {
        if (chip == null) {
            chip = new Chip(color);
        } else {
            chip.setColor(color);
        }
    }

    public void setEmpty() {
        chip = null;
    }

    public boolean isEmpty() {
        return chip == null;
    }

    public Color getChipColor() {
        if (chip != null) {
            return chip.getColor();
        }

        return null;
    }

    public void switchChip() {
        if (chip != null) {
            chip.switchColor();
        }
    }

    public Cell getDeepCopy() {
        Cell copy = new Cell();

        if (chip != null) {
            copy.chip = chip.getDeepCopy();
        }

        return copy;
    }
}
