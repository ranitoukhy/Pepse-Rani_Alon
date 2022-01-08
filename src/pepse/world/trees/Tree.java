package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.EndlessWorldUtil;
import pepse.world.Block;
import pepse.world.Creatable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Tree class - a class that is responsible for creating trees (therefore it implements the
 * Createable interface).
 */
public class Tree implements Creatable {
    public static final int MIN_HEIGHT = 6;
    public static final int MAX_HEIGHT = 10;
    public static int TREE_LAYER_OFFSET = 2;
    public static int LEAF_LAYER_OFFSET = 3;

    private final GameObjectCollection gameObjects;
    public String TAG = "tree";
    private final int groundLayer;
    private final Function<Float, Float> getHeight;
    private final Color BASE_TREE_COLOR = new Color(100, 50, 20);
    private final int seed;

    /**
     * Constructor- initialise the parameters necessary for creating trees.
     *
     * @param gameObjects:     the game object collection that we'll add the trees to.
     * @param groundLayer      the layer of the ground of the game
     * @param getHeight        a functional interface instance that returns the ground height of a
     *                         given x coordinate
     * @param seed             a parameter that will be sent to the Random instance
     */
    public Tree(
            GameObjectCollection gameObjects,
            int groundLayer,
            Function<Float, Float> getHeight,
            int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.getHeight = getHeight;
        this.seed = seed;
    }


    @Override
    public GameObjectCollection getGameObjects() {
        return this.gameObjects;
    }

    @Override
    public void create(int x) {
        //create a tree in a chance of 1:10
        if (new Random(Objects.hash(x, seed)).nextFloat() < 0.1) {
            createTree(x);
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

    /**
     * a function that creates a single tree
     *
     * @param x - the coordinate of where we will build the tree
     */
    private void createTree(int x) {
        Random random = new Random(Objects.hash(x, seed));
        int height = (int) (random.nextFloat() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT);

        float groundHeight = getHeight.apply((float) x) - Block.SIZE;
        groundHeight = Block.ROUND.apply(groundHeight);
        float maxHeight = groundHeight - Block.SIZE * height;

        for (float y = groundHeight; y > maxHeight; y -= Block.SIZE) {
            //create a column of blocks
            Vector2 blockLocation = new Vector2(x, y);
            Renderable blockRenderable =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_COLOR));
            Block block = new Block(blockLocation, blockRenderable);
            EndlessWorldUtil.addObject(x, block, gameObjects, groundLayer + TREE_LAYER_OFFSET);
        }

        TreeTop top = new TreeTop(gameObjects, x, (int) maxHeight, seed, groundLayer + LEAF_LAYER_OFFSET);
        gameObjects.layers().shouldLayersCollide(groundLayer, groundLayer + LEAF_LAYER_OFFSET, true);
    }
}
