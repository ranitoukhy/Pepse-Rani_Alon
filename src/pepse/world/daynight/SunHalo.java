package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * a class that represents the halo that surrounds the sun
 */
public class SunHalo {

    public static final String TAG = "halo";
    public static final float MULT_FACTOR = 1.25f;

    /**
     * a function that creates a halo
     * @param gameObjects the game object collection that we'll add the sun into
     * @param layer layer of the halo object
     * @param sun the sun that was created
     * @param color color of the halo
     * @return the constructed halo object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color){
        OvalRenderable oval = new OvalRenderable(color);
        GameObject halo = new GameObject(Vector2.ZERO,sun.getDimensions().mult(MULT_FACTOR), oval);
        halo.setTag(TAG);
        gameObjects.addGameObject(halo, layer);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        halo.addComponent((float deltaTime) -> halo.setCenter(sun.getCenter()));
        return halo;

    }
}
