package com.treemoval.visualizer;

import com.treemoval.data.Point;
import javafx.fxml.FXMLLoader;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.io.IOException;

//--------------------------------------------------------------------------------------------------
// ::TreeGroup
//
/**
 * The TreeGroup class handles importation of tree models, positioning, coloring, etc.
 * Extends the JavaFX Group class.
 */
public class TreeGroup extends Group {

    private final double TREE_Y_OFFSET = 30;

    //--------------------------------------------------------------------------------------------------
    // TreeGroup::TreeGroup
    //
    /**
     * Default constructor creates a tree at the origin.
     */
    public TreeGroup() {
        this(0, 0, 0);
    }

    //--------------------------------------------------------------------------------------------------
    // TreeGroup::TreeGroup
    //
    /**
     * instantiates a tree with random coordinates
     *
     * todo should take coordinates as input
     */
    public TreeGroup(double pX, double pY, double pZ) {

        try {
            getChildren().addAll(
                    ((Group) FXMLLoader.load(getClass().getResource("tree.fxml"))).getChildren()
            );
        } catch (IOException e) {
            System.out.println("tree.fxml resource file not found.");
        }

        setRotationAxis(Rotate.Z_AXIS);
        setRotate(180.0);
        setScaleX(10.0);
        setScaleY(10.0);
        setScaleZ(10.0);

        setTranslateX(pX);
        setTranslateY(TREE_Y_OFFSET + pY);
        setTranslateZ(pZ);

        setDepthTest(DepthTest.ENABLE);

    }

    //--------------------------------------------------------------------------------------------------
    // TreeGroup::makeRed
    //
    /**
     * sets the leaves mesh of the TreeGroup to red material as indication that the tree is marked for cutting
     */
    public void makeRed() {

        MeshView leaves = (MeshView) getChildren().get(0);
        if(!leaves.getId().equals("leaves")) { return; }

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.BLACK);

        leaves.setMaterial(redMaterial);
    }



}
