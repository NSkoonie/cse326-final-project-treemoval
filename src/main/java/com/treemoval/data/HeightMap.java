package com.treemoval.data;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HeightMap {

    int[][] map;
    private static final int DIMENSION = 9;

    public HeightMap (int xBound, int zBound) {
        this(xBound, zBound, ThreadLocalRandom.current().nextInt(100, 400));
    }

    public HeightMap (int lengthBound, int widthBound, int heightBound) {

        map = DSquare.generateHeightMapWithBound(
                new Random(), 0.05, heightBound, (int) Math.sqrt(lengthBound), (int) Math.sqrt(widthBound), DIMENSION);
    }

}
