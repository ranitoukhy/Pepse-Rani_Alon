package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private Vector2 windowDimensions;
    private Function<Float, Float> getHeight;
    private final Color BASE_TREE_COLOR = new Color(100, 50, 20);
    private final Random random;
    // private static Random randomTreeHeight = new Random()
    public Tree(GameObjectCollection gameObjects,
                int groundLayer, Vector2 windowDimensions, Function<Float, Float> getHeight){


        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.getHeight = getHeight;
        random = new Random();

    }
    public void createInRange(int firstLocationX , int lastLocationX){
        firstLocationX = (int)Math.floor((float)firstLocationX / Block.SIZE) * Block.SIZE;
        lastLocationX = (int)Math.floor((float)lastLocationX / Block.SIZE) * Block.SIZE;
        for (int locX = firstLocationX;locX <= lastLocationX - Block.SIZE; locX += Block.SIZE){
            if (random.nextFloat() < 0.1){
                createTree(locX);
            }
        }
    }
    private void createTree(int x){

        float groundHeight = getHeight.apply((float)x) - Block.SIZE;
        System.out.println("IN TREE for x: "+x+" ground height is :"+groundHeight);
        groundHeight = (float) (Math.floor(groundHeight / Block.SIZE) * Block.SIZE);

        for(float y=groundHeight; y > groundHeight-Block.SIZE*6; y-=Block.SIZE) {
            Vector2 blockLocation = new Vector2(x, y);
            Renderable blockRenderable =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_COLOR));
            Block block = new Block(blockLocation, blockRenderable);
            gameObjects.addGameObject(block);
        }
    }
}
