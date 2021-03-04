public class Tree {
    private double x;
    private double y;
    private double z;
    private int cut = -1;

    public double getX() {
        return x;
    }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getZ() { return z; }
    public void setZ(double z) { this.z = z; }

    public Tree() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Tree(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getCut() {return cut;}

    @Override
    public String toString() {
        return String.format("x: " + getX() + ", y: " + getY() + ", z: " + getZ());
    }
}
