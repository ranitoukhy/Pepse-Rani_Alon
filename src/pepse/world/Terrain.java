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
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private Random random;
    private PerlinNoise perlinNoise;
    private int minX;
    private int maxX;

    // objectAtDelta[k] = all objects with x = avater.x + k
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
        System.out.println("creating range: "+minX+" - "+maxX);
        minX = (int)Math.floor((float)minX / Block.SIZE) * Block.SIZE;
        maxX = (int)Math.floor((float)maxX / Block.SIZE) * Block.SIZE;

        if(objectsAtDelta.size() == 0) {
            this.minX = minX;
            this.maxX = maxX;
        }

        int removeBlock = 0;

        for (int blockXCoordinate = minX; blockXCoordinate <= maxX; blockXCoordinate+=30) {
            if(EndlessWorldUtil.containsKey(blockXCoordinate))
                continue;

//            if(blockXCoordinate < this.minX)
//                removeBlock += 1;
//            if(blockXCoordinate > this.maxX)
//                removeBlock -= 1;

            System.out.println("creating for "+blockXCoordinate);
            ArrayList<GameObject> objects = new ArrayList<>();
            float preHeight = groundHeightAt(blockXCoordinate);
            int YCoordinate =
                    (int)Math.floor(preHeight / Block.SIZE) * Block.SIZE;

            for (int i = 0; i < TERRAIN_DEPTH; i++ )
            {
                Renderable blockRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));

                Block block = new Block(new Vector2(blockXCoordinate,YCoordinate),
                        blockRenderable);

                objects.add(block);
                EndlessWorldUtil.addObject(blockXCoordinate, block,gameObjects);

//                gameObjects.addGameObject(block);

                YCoordinate += Block.SIZE;
            }



        }



    }

    public void removeInRange(int minX, int maxX) {
        System.out.println("removing range: " + minX + " - " + maxX);
        minX = (int) Math.floor((float) minX / Block.SIZE) * Block.SIZE;
        maxX = (int) Math.floor((float) maxX / Block.SIZE) * Block.SIZE;

        if (objectsAtDelta.size() == 0) {
            this.minX = minX;
            this.maxX = maxX;
        }

        for (int blockXCoordinate = minX; blockXCoordinate <= maxX; blockXCoordinate += 30) {
            if (!objectsAtDelta.containsKey(blockXCoordinate))
                continue;
            System.out.println("removing for "+blockXCoordinate);
//            for (GameObject obj : objectsAtDelta.get(blockXCoordinate))
//                EndlessWorldUtil.removeCol(blockXCoordinate, gameObjects);
            EndlessWorldUtil.removeCol(blockXCoordinate, gameObjects);
            //objectsAtDelta.remove(blockXCoordinate);
        }
    }
}
