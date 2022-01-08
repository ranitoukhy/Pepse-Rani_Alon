package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * a class that represents the game manager.
 */
public class PepseGameManager extends GameManager {

    private static final int INDENTATION_X_VALUE = 30;
    private static final int DAY_CYCLE = 30;
    private static final int SEED = 30;

    private static Avatar avatar;
    private static Terrain terrain;
    private static Tree tree;
    private Vector2 windowDimensions;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * a function that initialises the game. builds all necessary game objects.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        createBackground();
        createNature();
        createAvatar(imageReader, inputListener);
    }

    /**
     * creates the avatar for the game
     */
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener) {
        Vector2 initialAvatarLocation =
                new Vector2(windowDimensions.x()/2 + Block.SIZE,
                terrain.groundHeightAt(windowDimensions.x()/2) - Block.SIZE );
        avatar = Avatar.create(gameObjects(),Avatar.LAYER, initialAvatarLocation,
                inputListener, imageReader);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects().layers().shouldLayersCollide(Avatar.LAYER, Terrain.TOP_LAYER, true);
        gameObjects().layers().shouldLayersCollide(Avatar.LAYER, Terrain.BOTTOM_LAYER, true);
        setCamera(new Camera(avatar,
                windowDimensions.mult(0.5f).subtract(initialAvatarLocation),
                windowDimensions,
                windowDimensions));
    }

    /**
     * creates the terrain and the trees for the game
     */
    private void createNature() {
        terrain = new Terrain(gameObjects(),Terrain.TOP_LAYER,
                windowDimensions,SEED);
        terrain.createInRange(0,(int)windowDimensions.x());

        tree = new Tree(gameObjects(),Terrain.TOP_LAYER,
                terrain::groundHeightAt,
                SEED);

        tree.createInRange(0, (int) windowDimensions.x());
    }

    /**
     * creates the sky, sun, and night cycle
     */
    private void createBackground() {
        GameObject sky = Sky.create(gameObjects(),windowDimensions,
                Layer.BACKGROUND);

        GameObject night = Night.create(gameObjects(), Night.LAYER,
                windowDimensions, DAY_CYCLE);
        GameObject sun = Sun.create(gameObjects(), Sun.LAYER,
                windowDimensions, DAY_CYCLE);
        GameObject halo = SunHalo.create(gameObjects(), SunHalo.LAYER, sun, SunHalo.COLOR);
    }


    /**
     * a function that updates each frame of the game. main functionality is to keep the endless
     * world mechanism.
     * @param deltaTime current time
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int delta = (int)avatar.getDelta();
        if(delta != 0) {
            int createMinX = (delta > 0)? (int)avatar.getCenter().x() +(int)windowDimensions.x()/2:
                    (int)avatar.getCenter().x()-(int)windowDimensions.x()/2+delta;
            int createMaxX = (delta < 0)?
                    (int)avatar.getCenter().x() - (int)windowDimensions.x()/2 :
                    (int)avatar.getCenter().x()+(int)windowDimensions.x()/2+delta;
            int removeMinX = (delta > 0)?
                    createMinX - (int)windowDimensions.x() :
                    createMinX + (int)windowDimensions.x();
            int removeMaxX = (delta > 0)?
                    createMaxX - (int)windowDimensions.x() :
                    createMaxX + (int)windowDimensions.x();

            createAndRemoveInRange(createMinX,createMaxX,removeMinX,removeMaxX);

        }
    }

    /**
     * creates objects in [createMinX,createMaxX] and removes from [removeMinX,removeMaxX]
     */
    private void createAndRemoveInRange(int createMinX, int createMaxX, int removeMinX, int removeMaxX){
        terrain.createInRange(createMinX - INDENTATION_X_VALUE, createMaxX + INDENTATION_X_VALUE);
        terrain.removeInRange(removeMinX + INDENTATION_X_VALUE, removeMaxX - INDENTATION_X_VALUE);

        tree.createInRange(createMinX - INDENTATION_X_VALUE, createMaxX + INDENTATION_X_VALUE);
        tree.removeInRange(removeMinX + INDENTATION_X_VALUE,removeMaxX - INDENTATION_X_VALUE);
    }
}
