package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

public class PepseGameManager extends GameManager {
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObject sky = Sky.create(gameObjects(),windowController.getWindowDimensions(),
                Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjects(),Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(),30);
        terrain.createInRange(0,(int)windowController.getWindowDimensions().x());

        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), 30);
        GameObject sun = Sun.create(gameObjects(),Layer.BACKGROUND + 1,
                windowController.getWindowDimensions(), 30);
        Color haloColor = new Color(255, 255, 0, 50);
        GameObject halo = SunHalo.create(gameObjects(), Layer.BACKGROUND + 2, sun, haloColor);
        Tree tree = new Tree(gameObjects(),Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(),
                terrain::groundHeightAt);

        tree.createInRange(0, (int) windowController.getWindowDimensions().x());
    }
}
