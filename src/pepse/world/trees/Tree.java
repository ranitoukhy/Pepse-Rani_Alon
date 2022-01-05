package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.EndlessWorldUtil;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private Vector2 windowDimensions;
    private Function<Float, Float> getHeight;
    private final Color BASE_TREE_COLOR = new Color(100, 50, 20);
    private final Random random;
    private final int seed;

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
        random = new Random(Objects.hash(60,seed));
        this.seed = seed;
    }

    public void createInRange(int firstLocationX , int lastLocationX){
        firstLocationX = Block.ROUND.apply((float)firstLocationX);
        lastLocationX = Block.ROUND.apply((float)lastLocationX);

        for (int locX = firstLocationX;locX <= lastLocationX - Block.SIZE; locX += Block.SIZE){
            if (new Random(Objects.hash(locX, seed)).nextFloat() < 0.1){
                createTree(locX);
            }
        }
    }

    public void removeInRange(int firstLocationX , int lastLocationX){
        firstLocationX = (int)Math.floor((float)firstLocationX / Block.SIZE) * Block.SIZE;
        lastLocationX = (int)Math.floor((float)lastLocationX / Block.SIZE) * Block.SIZE;
        for (int locX = firstLocationX;locX <= lastLocationX - Block.SIZE; locX += Block.SIZE){
             EndlessWorldUtil.removeCol(locX,gameObjects);
        }
    }

    private void createTree(int x) {
        float groundHeight = getHeight.apply((float) x) - Block.SIZE;
        groundHeight = Block.ROUND.apply(groundHeight);
        float maxHeight = groundHeight - Block.SIZE * 6;

            for (float y = groundHeight; y > groundHeight - Block.SIZE * 6; y -= Block.SIZE) {
                Vector2 blockLocation = new Vector2(x, y);
                Renderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_COLOR));
                Block block = new Block(blockLocation, blockRenderable);
                EndlessWorldUtil.addObject(x, block, gameObjects, groundLayer + 1);
            }

            TreeTop.create(gameObjects, new Vector2(5, 5), new Vector2(x, maxHeight), groundLayer + 2);
        }
}
