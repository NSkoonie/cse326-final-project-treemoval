package com.treemoval.visualizer;
//--------------------------------------------------------------------------------------------------
// ::ForestBuilder
//

import com.treemoval.data.Forest;
import com.treemoval.data.Tags;
import com.treemoval.data.Tree;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * The ForestGroup class generates the forest utilizing other mesh classes
 * (i.e. GroundMesh, TreeGroup, and Rock)
 */
public class ForestGroup extends Group {

    Group redTrees;

    public ForestGroup(Forest forest) {

        // build the ground
        int xSize = (int)( forest.getMaxX() / 10.0) + 14;
        int zSize = (int)(forest.getMaxZ() / 10.0) + 14;
        System.out.println("xSize: " + xSize + " zSize: " + zSize);
        GroundMesh groundMesh = new GroundMesh( xSize, zSize );
        getChildren().add(groundMesh);

        AmbientLight ambientGroundLight = new AmbientLight();
        ambientGroundLight.setColor(Color.rgb(200, 200, 200, 1));
        ambientGroundLight.getScope().add(groundMesh);
        getChildren().add(ambientGroundLight);

        // build the trees
        Group allTrees = new Group();
        getChildren().add(allTrees);
        allTrees.setTranslateX(70);
        allTrees.setTranslateZ(70);

        redTrees = new Group();

        AmbientLight ambientTreeLight = new AmbientLight();
        ambientTreeLight.setColor(Color.rgb(90, 90, 90, 1));
        ambientTreeLight.getScope().add(allTrees);
        getChildren().add(ambientTreeLight);

        for(Tree tree : forest.trees) {
            TreeGroup treeGroup = new TreeGroup(tree.getX(), tree.getY(), tree.getZ());
            if(tree.getTag() == Tags.CUT) {
                treeGroup.makeRed();
                redTrees.getChildren().add(treeGroup);
            }
            allTrees.getChildren().add(treeGroup);
        }

        TreeGroup tree = new TreeGroup();
        tree.setTranslateY(30);
        tree.setTranslateX(30);
        tree.setTranslateZ(30);
        tree.makeRed();
        allTrees.getChildren().addAll(tree);

        //RockGroup rocks = new RockGroup();
        //getChildren().add(rocks);

    }

    //--------------------------------------------------------------------------------------------------
    // ForestGroup::hideRedTrees
    //
    public void hideRedTrees() {
        redTrees.setVisible(false);
    }

    //--------------------------------------------------------------------------------------------------
    // ForestGroup::hideRedTrees
    //
    public void showRedTrees() {
        redTrees.setVisible(true);
    }

}
