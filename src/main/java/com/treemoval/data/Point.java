package com.treemoval.data;
//----------------------------------------------------------------------------------------------------------------------
// ::Point
//
/**
 * The Point class holds x, y, z coordinates for a point in 3d space
 *
 * @author Noah
 */
public class Point {

    public double x;
    public double y;
    public double z;

    /**
     * Instantiates a new point with passed coordinates.
     *
     * @param pX the x coordinate for the new point
     * @param pY the y coordinate for the new point
     * @param pZ the z coordinate for the new point
     */
    public Point (double pX, double pY, double pZ) {

        this.x = pX;
        this.y = pY;
        this.z = pZ;

    }

    @Override
    public String toString() {
        return String.format(this.x + " " + this.y + " " + this.z);
    }

}
