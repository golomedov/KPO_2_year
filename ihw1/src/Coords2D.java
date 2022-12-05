public class Coords2D {
    public int x;
    public int y;

    public Coords2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coords2D scale(Coords2D base, int factor) {
        return new Coords2D(base.x * factor, base.y * factor);
    }

    public static Coords2D sum(Coords2D lhs, Coords2D rhs) {
        return new Coords2D(lhs.x + rhs.x, lhs.y + rhs.y);
    }
}
