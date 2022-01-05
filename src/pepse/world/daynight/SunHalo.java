package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    public static final String TAG = "halo";

    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun,
                                    Color color){
        OvalRenderable oval = new OvalRenderable(color);
        GameObject halo = new GameObject(Vector2.ZERO,sun.getDimensions().mult(1.25f), oval);
        halo.setTag(TAG);
        gameObjects.addGameObject(halo, layer);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        halo.addComponent((float deltaTime) -> halo.setCenter(sun.getCenter()));
        return halo;

    }
}
