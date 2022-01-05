package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
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

public class Terrain {
    public static final String TOP_TAG = "top terrain";
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private Random random;
    private PerlinNoise perlinNoise;

    private HashMap<Integer, ArrayList<GameObject>> objectsAtDelta;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = (2/3.F)*windowDimensions.y();
        perlinNoise = new PerlinNoise();
        perlinNoise.setSeed(seed);

        objectsAtDelta = new HashMap<>();
    }
    public float groundHeightAt(float x) {
        //return groundHeightAtX0;
        return groundHeightAtX0 + Block.SIZE * ((int)(perlinNoise.noise(x/Block.SIZE) * 28));

//        return (float) ((Math.cos(x)+Math.sin(x))*15+groundHeightAtX0);
    }

    public void createInRange(int minX, int maxX) {

        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);

        for (int blockXCoordinate = minX; blockXCoordinate <= maxX; blockXCoordinate+=30) {
            if(EndlessWorldUtil.containsKey(blockXCoordinate))
                continue;

            float preHeight = groundHeightAt(blockXCoordinate);
            int YCoordinate = Block.ROUND.apply(preHeight);

            for (int i = 0; i < TERRAIN_DEPTH; i++ )
            {
                Renderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

                Block block = new Block(new Vector2(blockXCoordinate,YCoordinate),
                        blockRenderable);

                block.setTag(TOP_TAG);
                EndlessWorldUtil.addObject(blockXCoordinate, block,gameObjects, groundLayer);

//                gameObjects.addGameObject(block);
//                gameObjects.addGameObject(block, groundLayer);

                YCoordinate += Block.SIZE;
            }



        }



    }

    public void removeInRange(int minX, int maxX) {
        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);

        for (int blockXCoordinate = minX; blockXCoordinate <= maxX; blockXCoordinate += 30) {
            if (!objectsAtDelta.containsKey(blockXCoordinate))
                continue;
//            for (GameObject obj : objectsAtDelta.get(blockXCoordinate))
//                EndlessWorldUtil.removeCol(blockXCoordinate, gameObjects);
            EndlessWorldUtil.removeCol(blockXCoordinate, gameObjects);
            //objectsAtDelta.remove(blockXCoordinate);
        }
    }
}
