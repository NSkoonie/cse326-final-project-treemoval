package com.treemoval.data;

import java.util.Comparator;

//----------------------------------------------------------------------------------------------------------------------
// ::Tree
//
/**
 * This class will compare the distance of two trees, and return a negative int, a positive int or a zero.
 * This class' sole purpose is to function as the comparator needed to sort a forest by the distance of
 * its trees. Can also be used elsewhere if needed.
 *
 * @author Garrett Evans
 * @version 1.0
 */
public class SortByDist implements Comparator<Tree> {

    @Override
    public int compare(Tree a, Tree b) {
        if (a.getDist() - b.getDist() < 0)
            return -1;
        else if (a.getDist() - b.getDist() > 0)
            return 1;
        else
            return 0;
    }
}
