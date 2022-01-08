package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PerlinNoise;
import pepse.util.ColorSupplier;
import pepse.util.EndlessWorldUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Terrain class - a class responsible for building the ground of the game (therefore it
 * implements the Createable interface)
 */
public class Terrain implements Creatable{
    public static final int PERLIN_MULT = 15;
    public static int TOP_LAYER = Layer.STATIC_OBJECTS;
    public static int BOTTOM_LAYER = Layer.STATIC_OBJECTS+1;
    public static final String TOP_TAG = "top terrain";

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private Random random;
    private final PerlinNoise perlinNoise;

    /**
     * Constructor of the Terrain - initialises values necessary for creating the ground
     * @param gameObjects: the game object collection that we'll add the trees to.
     * @param groundLayer the layer of the ground of the game
     * @param windowDimensions 2D vector that represents the window dimensions
     * @param seed a parameter that will be sent to the Random instance
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = (2/3.F)*windowDimensions.y();
        perlinNoise = new PerlinNoise();
        perlinNoise.setSeed(seed);

        TOP_LAYER = groundLayer;
        BOTTOM_LAYER = TOP_LAYER+1;
    }

    /**
     * a function that returns the ground height at a given x coordinate
     */
    public float groundHeightAt(float x) {
        return groundHeightAtX0 + Block.SIZE * ((int)(perlinNoise.noise(x/Block.SIZE) * PERLIN_MULT));
    }

    @Override
    public GameObjectCollection getGameObjects() {
        return this.gameObjects;
    }

    /**
     * a function that creates a column of blocks given a current x coordinate
     */
    @Override
    public void create(int x) {
        float preHeight = groundHeightAt(x);
        int YCoordinate = Block.ROUND.apply(preHeight);

        for (int i = 0; i < TERRAIN_DEPTH; i++ )
        {
            Renderable blockRenderable =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

            Block block = new Block(new Vector2(x,YCoordinate),
                    blockRenderable);
            //set tag only for the top block layer in order to avoid unnecessary handling of
            // low - level brick collisions with the leaves
            if(i == 0) {
                block.setTag(TOP_TAG);
                EndlessWorldUtil.addObject(x, block,gameObjects, TOP_LAYER);
            } else {
                EndlessWorldUtil.addObject(x, block,gameObjects, BOTTOM_LAYER);
            }

            YCoordinate += Block.SIZE;
        }
    }

    @Override
    public String getTag() {
        return TOP_TAG;
    }
}
