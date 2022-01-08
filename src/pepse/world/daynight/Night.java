package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * a class responsible for the night transition and display
 */
public class Night {
    public static final int LAYER = Layer.FOREGROUND;
    public static final String TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * creates a gameobject which will represent nights in the game
     * @param gameObjects the game object collection that we'll add the sun into
     * @param layer layer of the halo object
     * @param windowDimensions the dimensions of the window
     * @param cycleLength the length of one day/night cycle
     * @return the constructed halo object
     * @return
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag(TAG);
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength/2, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        return night;
    }
}
