import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

public class Forest {

    List<Tree> trees = new ArrayList<>();
    
    public Forest() {
        
    }

    public void readFromFile() throws IOException {
        File file = new File("forest.txt");

        BufferedReader br = null;
        br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] dets = line.split(",");
            int x = Integer.parseInt(dets[0]);
            int y = Integer.parseInt(dets[1]);
            int z = Integer.parseInt(dets[2]);
            this.trees.add(new Tree(x, y, z));
            for (String string : dets)
                System.out.println(string);

        }
        
        br.close();
    }

    public static double distance(Tree A, Tree B) {
        double x1 = A.getX();
        double y1 = A.getY();
        double z1 = A.getZ();

        double x2 = B.getX();
        double y2 = B.getY();
        double z2 = B.getZ();

        double dist = sqrt(pow(x2-x1, 2) + pow(y2-y1, 2) + pow(z2-z1, 2));
        return dist;
    }

    public static void main(String[] args) throws IOException {
        Forest forest = new Forest();
        
        for (Tree tree : forest.trees) {
            System.out.println("Tree");
        }
        System.out.println("The data for the first tree is: " + forest.getTree(0));
        System.out.println("The data for the third tree is: " + forest.getTree(2));
        System.out.println("The distance between these two trees is: " + distance(forest.getTree(0), forest.getTree(2)));

    }

    public Tree getTree(int x) {
        return this.trees.get(x);
    }

}