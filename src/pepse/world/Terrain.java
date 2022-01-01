package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PerlinNoise;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

public class Terrain {
    public static final String TOP_TAG = "top terrain";
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private Random random;
    private PerlinNoise perlinNoise;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = (2/3.F)*windowDimensions.y();
        perlinNoise = new PerlinNoise();
        perlinNoise.setSeed(seed);
    }
    public float groundHeightAt(float x) {
        //return groundHeightAtX0;
        return groundHeightAtX0 + Block.SIZE * ((int)(perlinNoise.noise(x/Block.SIZE) * 28));

//        return (float) ((Math.cos(x)+Math.sin(x))*15+groundHeightAtX0);
    }

    public void createInRange(int minX, int maxX) {
        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);

//        minX = (int)Math.floor((float)minX / Block.SIZE) * Block.SIZE;
//        maxX = (int)Math.floor((float)maxX / Block.SIZE) * Block.SIZE;
        for (int blockXCoordinate = minX; blockXCoordinate <= maxX; blockXCoordinate+=30) {
            float preHeight = groundHeightAt(blockXCoordinate);
            int YCoordinate = Block.ROUND.apply(preHeight);
//                    (int)Math.floor(preHeight / Block.SIZE) * Block.SIZE;

            for (int i = 0; i < TERRAIN_DEPTH; i++ )
            {
                Renderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

                Block block = new Block(new Vector2(blockXCoordinate,YCoordinate),
                        blockRenderable);
                block.setTag(TOP_TAG);
                gameObjects.addGameObject(block, groundLayer);

                YCoordinate += Block.SIZE;
            }

        }




    }
}
