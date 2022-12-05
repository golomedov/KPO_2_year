public enum Color {
    WHITE,
    BLACK;

    public boolean isOppositeColor(Color other) {
        return this != other;
    }

    public boolean isEqualColor(Color other) {
        return this == other;
    }

    public Color getOppositeColor() {
        if (this == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }
}
