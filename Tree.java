public class Tree {
    private double x;
    private double y;
    private double z;
    private int cut = -1;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Tree(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getCut() {return cut;}

    @Override
    public String toString() {
        return String.format("x: " + getX() + "y: " + getY() + "z: " + getZ());
    }
}
