package com.treemoval.data;

import java.io.*;
import java.util.*;

import static com.treemoval.data.Tags.*;
import static java.lang.Math.*;

//----------------------------------------------------------------------------------------------------------------------
// ::Forest
//
/**
 * The Forest class holds and manipulates an ArrayList of Tree objects.
 *
 * Includes methods for reading and writing to file, calculating the distance between two trees, and printing the
 * coordinates of every tree in the forest.
 *
 * @author Garrett Evans
 * @version 1.1
 *
 */
public class Forest {

    public List<Tree> trees = new ArrayList<>(); // todo should probably be private
    private double safeDistance = 5; //todo need to implement way for user to modify
                                    // note from nps: this should be passed in to the thinning algorithm
                                    //      (it will be retrieved as a value from the gui)
  
    private double minX;
    private double maxX;
    private double minZ;
    private double maxZ;

    //--------------------------------------------------------------------------------------------------
    // Forest::Forest
    //
    /**
     * The default constructor leaves the ArrayList empty
     */
    public Forest() {

    }

    /**
     * This constructor instantiates with user defined number of trees and area of forest.
     *
     * @param num_trees the number of trees in the forest
     * @param bound the bounds for the x and y coordinates (0 - inclusive, bound - exclusive)
     */
    public Forest(int num_trees, int bound) {

        for(int i = 0; i < num_trees; i++){
            Random rand = new Random();
            double x = rand.nextInt(bound) + rand.nextDouble();
            double z = rand.nextInt(bound) + rand.nextDouble();
            double y = 0;

            this.trees.add(new Tree(x, y, z));
        }
        updateBounds();
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::readFromFile
    //
    /**
     * Reads tree data from a CSV file and stores it in the forest.
     *
     * todo This method should probably accept an enum value (APPEND or CLEAR)
     *      to determine whether or not to clear before adding trees from the file.
     *
     * @param path the path to the CSV file to be read
     * @throws IOException if the CSV file is not found
     */
    public void readFromFile(String path) throws IOException {

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        this.trees.clear();

        String line;

        while ((line = br.readLine()) != null) {
            // System.out.println(line);
            String[] dets = line.split(",");
            double x = Double.parseDouble(dets[0]);
            double y = Double.parseDouble(dets[1]);
            double z = Double.parseDouble(dets[2]);
            this.trees.add(new Tree(x, y, z));
            // for (String string : dets) { System.out.println(string); }
        }

        br.close();
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::exportForest
    //
    /**
     * Reads tree data from a CSV file and stores it in the forest.
     * Returns 0 on success, 1 if file exists, 2 if file extension is incorrect
     * @throws IOException if the CSV file is not found
     */
    public int exportForest(String fileName) throws IOException {

        File file = new File(fileName);
        String line;

        if(!fileName.substring(fileName.length() - 4).equals(".csv")) {
            System.out.println("The file name needs to with the .csv file extension.\n");
            return 2;
        }


        if(file.createNewFile()){
            System.out.println("File created: " + file.getName());
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(writer);

            for(Tree tree: this.trees){
                line = tree.getX() + "," + tree.getY() + "," + tree.getZ();
                bw.write(line);
                bw.newLine();
            }

            bw.close();
            System.out.println("File writing success.\n");

        } else {
            System.out.println("File already exists.\n");
            return 1;
        }

        return 0;
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::distance
    //
    /**
     * Calculates the euclidean distance between two trees.
     *
     * @param a the first tree
     * @param b the second tree
     * @return the distance between trees a and b.
     */
    public static double distance(Tree a, Tree b) {
        double x1 = a.getX();
        double y1 = a.getY();
        double z1 = a.getZ();

        double x2 = b.getX();
        double y2 = b.getY();
        double z2 = b.getZ();

        return sqrt(pow(x2-x1, 2) + pow(y2-y1, 2) + pow(z2-z1, 2));
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::getTree
    //
    /**
     * Returns the tree at a specified index in the forest.
     *
     * todo should handle out of bounds indexing without crashing
     *
     * @param x the index value of the desired tree
     * @return the Tree object, if found.
     */
    public Tree getTree(int x) {
        return this.trees.get(x);
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::listTrees
    //
    /**
     * Outputs the coordinates of every tree in the forest.
     */
    public void listTrees() {
        int i = 0;
        for(Tree tree : this.trees) {
            i++;
            System.out.println("Tree " + i + "; " + tree);
        }
        System.out.println();
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::updateBounds
    //
    /**
     * Sets the bound for use with the visualizer.
     */
    public void updateBounds() {
        for (Tree tree: trees) {
            if (tree.getX() < minX)
                minX = tree.getX();
            if (tree.getX() > maxX)
                maxX = tree.getX();

            if (tree.getZ() < minZ)
                minZ = tree.getZ();
            if (tree.getZ() > maxZ)
                maxZ = tree.getZ();
        }
    }

    // Forest::thinningAlgorithm
    //
    /**
     * The algorithm that goes through the forest and marks each tree to be cut or not. It begins by selecting the first
     * tree in the forest as the "currentTree", and marks it to not be cut. Then the distance to every tree in the
     * forest is calculated, then sorted by distance (closest to furthest). Then, the algorithm goes through each tree.
     * If the tree has not been marked yet and it is within the "safeDistance", it gets marked for removal. If the
     * tree has not been marked and it is outside the safeDistance, then it is marked to not be cut down,
     * and is then marked as the new "currentTree" since it is the closest tree at a safe distance.
     * The loop ends. If the # of marked trees is not equal to the # of total trees then the process begins again.
     *
     * todo it might be able to be more efficient but it would be a lot of work for little return
     * */
    public void thinningAlgorithm() {
        int marked = 1; //# of trees marked to be cut or not
        int treeNum = this.trees.size(); //total # of trees
        Tree currentTree = this.getTree(0);
        currentTree.setTag(SAFE);

        while (marked < treeNum) {
            for (Tree tmpTree : this.trees) {
                tmpTree.setDist(distance(currentTree, tmpTree));
            }

            this.trees.sort(new SortByDist()); //sort by closest to furthest distance

            for (Tree tmpTree : this.trees) {
                if (tmpTree.getDist() < safeDistance && tmpTree.getTag() == UNMARKED) {
                    tmpTree.setTag(CUT); //to be cut!
                    marked++;
                } else if (tmpTree.getDist() >= safeDistance && tmpTree.getTag() == UNMARKED) {
                    currentTree = tmpTree;
                    currentTree.setTag(SAFE);//don't cut
                    marked++;
                    break;
                }
            }
        }
    }

    //--------------------------------------------------------------------------------------------------
    // Forest::main
    //
    /**
     * Simple forest testing; instantiates and prints a random forest, then clears the forest
     * and reads data from a file to print to console as well.
     *
     * todo Should be tested with JUnit testing instead of using main function.
     *
     * @param args none
     */
    public static void main(String[] args) {

        Forest forest = new Forest(20, 20);
        Forest new_forest = new Forest(100, 1000);

        System.out.println("This is the first forest using the default constructor.");
        forest.listTrees();

        System.out.println("This is the second forest created by setting the number of trees and area.");
        new_forest.listTrees();

        System.out.println("The distance between the first two trees is: " +
                distance(forest.getTree(0), forest.getTree(1)) + "\n");

        forest.thinningAlgorithm();
        forest.listTrees();

        String export = "forest_export.csv";

        try {
            forest.exportForest(export);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            forest.readFromFile("forest_export.csv");
            System.out.println("This is reading from the file forest_export.csv");
            forest.listTrees();
            System.out.println("The distance between the first two trees is: " +
                    distance(forest.getTree(0), forest.getTree(1)) + "\n");

        } catch (IOException e) {

            System.out.println("Sample forest file not found!");

        }

    }

}