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

    private Group redTrees;
    private GroundMesh groundMesh;

    public ForestGroup(Forest forest) {

        groundMesh = new GroundMesh(forest);
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

        AmbientLight ambientTreeLight = new AmbientLight();
        ambientTreeLight.setColor(Color.rgb(90, 90, 90, 1));
        ambientTreeLight.getScope().add(allTrees);
        getChildren().add(ambientTreeLight);

        Group greenTrees = new Group();
        allTrees.getChildren().addAll(greenTrees);
        redTrees = new Group();
        allTrees.getChildren().add(redTrees);


        for(Tree tree : forest.trees) {

            TreeGroup treeGroup = new TreeGroup(tree.getX(), tree.getY(), tree.getZ());

            if(tree.getTag() == Tags.CUT) {
                treeGroup.makeRed();
                redTrees.getChildren().add(treeGroup);
            } else {
                greenTrees.getChildren().add(treeGroup);
            }

        }

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

    //--------------------------------------------------------------------------------------------------
    // ForestGroup::hideGround
    //
    public void hideGround() {
        groundMesh.setVisible(false);
    }

    //--------------------------------------------------------------------------------------------------
    // ForestGroup::hideGround
    //
    public void showGround() {
        groundMesh.setVisible(true);
    }

}
