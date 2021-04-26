package com.treemoval.visualizer;

//----------------------------------------------------------------------------------------------------------------------
// ::ForestScene
//

import com.treemoval.data.Point;
import javafx.geometry.Bounds;
import javafx.scene.*;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * The ForestScene class displays a forest group
 *
 * todo should the ForestGroup be added as a child node to root, rather than setting the root to the ForestGroup?
 *      this way, the camera and axes can exist outside the ForestGroup, and won't have to be recreated when changing
 *      forests (root member should be final)
 */
public class ForestScene extends SubScene {

    final Group root = new Group();
    private ForestGroup forestGroup = null;

    boolean highContrastMode = false;
    AmbientLight contrastLight = new AmbientLight();

    private final Xform axisGroup = new Xform();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 35.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 45.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 200000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 5.0;
    private static final double MOUSE_SPEED = 0.5;
    private static final double ROTATION_SPEED = 0.3;
    private static final double TRACK_SPEED = 0.6;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    //--------------------------------------------------------------------------------------------------
    // ForestScene::ForestScene()
    //
    /**
     * instantiates the ForestScene with a placeholder root. The init() function should be called to
     * complete setup.
     */
    public ForestScene () {
        super(new Group(), 1024, 768, true, SceneAntialiasing.BALANCED);
        setFill(Color.LIGHTBLUE);
    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::init
    //
    /**
     * initializes the ForestScene with a forest, camera, axes, and mouse and keyboard event handlers
     */
    public void init() {

        setRoot(root);

        contrastLight.setLightOn(false);
        root.getChildren().add(contrastLight);

        buildCamera();
        buildAxes();
        handleMouse();
        handleKeyboard();

    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::buildCamera
    //
    /**
     * Adds and positions the camera.
     *
     * todo should this be in a different class?
     *      perhaps a new WorldGroup class, or even in the ForestGroup class?
     */
    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        PointLight light = new PointLight();
        light.setColor(Color.WHITE);
        cameraXform3.getChildren().add(light);
        light.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);

        setCamera(camera);

    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::resetCamera
    //
    /**
     * sets the camera position based on the forestGroup
     * if there is no forestGroup (i.e. if it is null), thecamera will be pointed at origin.
     *
     */
    public void resetCamera() {
        forestGroup.getLayoutX();
        Bounds bounds = forestGroup.getBoundsInLocal();
        Point center = new Point(
                (bounds.getMaxX() - bounds.getMinX()) / 2.0,
                0,
                (bounds.getMaxZ() - bounds.getMinZ()) / 2.0
        );
        System.out.println("Center: ");
        System.out.println("x: " + center.x + " y: " + center.y + " z: " + center.z);

        cameraXform.t.setX(center.x);
        cameraXform.t.setZ(center.z);

        cameraXform3.setTranslateZ(- bounds.getDepth());
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::setForestGroup
    //
    /**
     * changes the forest displayed in the subscene
     *
     * @param forestGroup the ForestGroup to be displayed within the scene
     */
    public void setForestGroup(ForestGroup forestGroup, boolean resetCamera) {

        root.getChildren().remove(this.forestGroup);
        this.forestGroup = forestGroup;
        root.getChildren().add(this.forestGroup);

        if(resetCamera) {
            resetCamera();
        }

    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::setContrastDisplayMode
    //
    public void setContrastDisplayMode() {

        setFill(Color.BLACK);
        contrastLight.setLightOn(true);
        if(forestGroup != null) {
            forestGroup.hideGround();
        }

    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::setNormalDisplayMode
    //
    public void setNormalDisplayMode() {

        if (highContrastMode) {

            setFill(Color.LIGHTBLUE);
            contrastLight.setLightOn(false);
            if (forestGroup != null) {
                forestGroup.showGround();
            }

            camera.setFieldOfView(30.0);
            cameraXform3.setTranslateZ(0.0);

            highContrastMode = false;

        }
    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::buildAxes
    //
    /**
     * Build a euclidean axes representation using three box shapes in the axisGroup and add it to the
     * world group.
     *
     * todo should this be in a different class?
     *      perhaps a new WorldGroup class, or even in the ForestGroup class?
     */
    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        //axisGroup.setVisible(false);
        root.getChildren().addAll(axisGroup);
    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::handleMouse
    //
    /**
     * Adjust the camera on mouse events.
     *
     */
    private void handleMouse() {

        setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 1.0;

            // smart modifier to automatically increase control speed when zoomed out
            double cameraZ = cameraXform3.getTranslateZ();
            double smartModifier = 1.0;
            if (cameraZ < -300) { smartModifier = (cameraZ * -1) / 300; }

            if (me.isControlDown()) {
                modifier = CONTROL_MULTIPLIER;
            }
            if (me.isShiftDown()) {
                modifier = SHIFT_MULTIPLIER;
            }
            if (me.isPrimaryButtonDown()) {

                double ry = cameraXform.ry.getAngle() - mouseDeltaX * MOUSE_SPEED * ROTATION_SPEED;
                double rx = cameraXform.rx.getAngle() + mouseDeltaY * MOUSE_SPEED * ROTATION_SPEED;

                // limit rotation from going below the horizon
                if(rx < 3) { rx = 3; }
                // limit camera rotation to prevent going upside down
                if(rx >= 90) { rx = 90; }

                if(rx > 85) {
                    if (!highContrastMode) {
                        setContrastDisplayMode();
                        camera.setFieldOfView(1);
                        System.out.println("cameraZ debug:  " + cameraZ);
                        cameraXform3.setTranslateZ(cameraZ * 30.0 - 13000.0);
                        highContrastMode = true;

                    }

                    if((Math.abs(ry) % 360) < 5 || (Math.abs(ry) % 360) > 355) {
                        ry = 0;
                    }

                } else {
                    if (highContrastMode) {
                        setNormalDisplayMode();
                        camera.setFieldOfView(30.0);
                        System.out.println("cameraZ debug:  " + cameraZ);
                        cameraXform3.setTranslateZ((cameraZ + 13000.0) / 30.0);
                        highContrastMode = false;
                    }
                }

                cameraXform.ry.setAngle(ry);
                cameraXform.rx.setAngle(rx);

            } else if (me.isSecondaryButtonDown()) {

                if (highContrastMode) {return;}
                double newZ = cameraZ + mouseDeltaX * MOUSE_SPEED * modifier * smartModifier;
                // limit zoom in to prevent zooming through the horizon
                if (newZ > 350) { newZ = 350; }
                // limit zoom out to prevent far clip
                if (newZ < -5000) { newZ = -5000; }
                cameraXform3.setTranslateZ(newZ);

            } else if (me.isMiddleButtonDown()) {

                double angle = Math.toRadians(cameraXform.ry.getAngle());
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);

                double x = cameraXform.t.getX();
                x = x + (mouseDeltaX * cos + mouseDeltaY * sin) * MOUSE_SPEED * modifier * smartModifier * TRACK_SPEED;
                cameraXform.t.setX(x);

                double z = cameraXform.t.getZ();
                z = z + (mouseDeltaX * sin * -1 + mouseDeltaY * cos) * MOUSE_SPEED * modifier * smartModifier * TRACK_SPEED;
                cameraXform.t.setZ(z);
            }
        });
    }

    //--------------------------------------------------------------------------------------------------
    // ForestScene::handleKeyboard
    //
    /**
     * Modify the scene on keyboard events.
     */
    private void handleKeyboard() {
        setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case C:
                    resetCamera();
                    break;
                case X:
                    axisGroup.setVisible(!axisGroup.isVisible());
                    break;
                case V:
                    //groundGroup.setVisible(!groundGroup.isVisible());
                    break;
            }
        });
    }


}
