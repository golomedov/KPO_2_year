public class Chip {
    private Color color;

    public Chip(Color color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void switchColor() {
        color = color.getOppositeColor();
    }

    public Chip getDeepCopy() {
        return new Chip(color);
    }
}
