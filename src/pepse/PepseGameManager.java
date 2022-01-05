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

public class PepseGameManager extends GameManager {
    private static Avatar avatar;
    private static Terrain terrain;
    private static Tree tree;
    private  WindowController windowController;
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        GameObject sky = Sky.create(gameObjects(),windowController.getWindowDimensions(),
                Layer.BACKGROUND);
        terrain = new Terrain(gameObjects(),Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(),30);
        terrain.createInRange(0,(int)windowController.getWindowDimensions().x());

        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), 30);
        GameObject sun = Sun.create(gameObjects(),Layer.BACKGROUND + 1,
                windowController.getWindowDimensions(), 30);
        Color haloColor = new Color(255, 255, 0, 50);
        GameObject halo = SunHalo.create(gameObjects(), Layer.BACKGROUND + 2, sun, haloColor);
        tree = new Tree(gameObjects(),Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(),
                terrain::groundHeightAt,
                30);

        tree.createInRange(0, (int) windowController.getWindowDimensions().x());
        Vector2 initialAvatarLocation =
                new Vector2(windowController.getWindowDimensions().x()/2 + Block.SIZE,
                terrain.groundHeightAt(windowController.getWindowDimensions().x()/2) - Block.SIZE );
        avatar = Avatar.create(gameObjects(),Layer.DEFAULT, initialAvatarLocation,
                inputListener,imageReader);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, true);
        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        //new Platformer().run();



    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int delta = (int)avatar.getDelta();
//        int deltaInBlocks = (int)Math.floor(delta / Block.SIZE);
        if(delta != 0) {
            int minX = (delta > 0)? (int)avatar.getCenter().x() +(int)windowController.getWindowDimensions().x()/2:
                    (int)avatar.getCenter().x()-(int)windowController.getWindowDimensions().x()/2+delta;
            int maxX = (delta < 0)?
                    (int)avatar.getCenter().x() - (int)windowController.getWindowDimensions().x()/2 :
                    (int)avatar.getCenter().x()+(int)windowController.getWindowDimensions().x()/2+delta;
            int minRemoveX = (delta > 0)?
                    minX - (int)windowController.getWindowDimensions().x() :
                    minX + (int)windowController.getWindowDimensions().x();
            int maxRemoveX = (delta > 0)?
                    maxX - (int)windowController.getWindowDimensions().x() :
                    maxX + (int)windowController.getWindowDimensions().x();

            terrain.createInRange(minX - 30, maxX + 30);
            terrain.removeInRange(minRemoveX + 30, maxRemoveX - 30);

            tree.createInRange(minX - 30, maxX + 30);
            tree.removeInRange(minRemoveX + 30,maxRemoveX - 30);

        }

//        if (avatar.getCenter().x() > 0.5*windowController.getWindowDimensions().x()) {
//            terrain.createInRange((int) (windowController.getWindowDimensions().x()) ,
//                    (int) (windowController.getWindowDimensions().x()/2 + avatar.getCenter().x()));
//        }
//        getCamera().

    }
}
