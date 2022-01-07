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

public class Tree implements Creatable {
    public static final int MIN_RADIUS=2;
    public static final int MAX_RADIUS=4;
    public static final int MIN_HEIGHT=6;
    public static final int MAX_HEIGHT=10;


    private GameObjectCollection gameObjects;
    public String TAG = "tree";
    private int groundLayer;
    private Vector2 windowDimensions;
    private Function<Float, Float> getHeight;
    private final Color BASE_TREE_COLOR = new Color(100, 50, 20);
//    private final Random random;
    private final int seed;
    private TreeTop top;

    public Tree(
            GameObjectCollection gameObjects,
            int groundLayer,
            Vector2 windowDimensions,
            Function<Float, Float> getHeight,
            int seed){
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.getHeight = getHeight;
//        random = new Random(Objects.hash(60,seed));
        this.seed = seed;
    }


    @Override
    public GameObjectCollection getGameObjects() {
        return this.gameObjects;
    }

    @Override
    public void create(int x) {
        if (new Random(Objects.hash(x, seed)).nextFloat() < 0.1){
            createTree(x);
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

    private void createTree(int x) {
        Random random = new Random(Objects.hash(x, seed));
        int height = (int) (random.nextFloat()*(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT);
        int treeTopRadius = (int) (random.nextFloat()*(MAX_RADIUS-MIN_RADIUS) + MIN_RADIUS);

        System.out.println("tree height = "+height);
        float groundHeight = getHeight.apply((float) x) - Block.SIZE;
        groundHeight = Block.ROUND.apply(groundHeight);
        float maxHeight = groundHeight - Block.SIZE * height;

            for (float y = groundHeight; y > maxHeight; y -= Block.SIZE) {
                Vector2 blockLocation = new Vector2(x, y);
                Renderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_COLOR));
                Block block = new Block(blockLocation, blockRenderable);
                EndlessWorldUtil.addObject(x, block, gameObjects, groundLayer + 1);
            }

//            TreeTop.create(gameObjects, new Vector2(5, 5), new Vector2(x, maxHeight), groundLayer + 2);
            top = new TreeTop(gameObjects, x, (int) maxHeight, treeTopRadius, groundLayer+2);
            gameObjects.layers().shouldLayersCollide(groundLayer, groundLayer+2, true);
        }
}
