package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * a class that represents the sun
 */
public class Sun{

    public static final int LAYER = Layer.BACKGROUND + 1;
    public static final String TAG = "sun";

    private static final int SUN_SIZE = 100;
    private static final int SUN_RATIO = 2;
    private static final int INITIAL_Y_COORDINATE = 10;
    private static final float FULL_CIRCLE_DEGREE = 360f;
    private static final int INITIAL_DEGREE_LOCATION = 50;

    /**
     * a function that creates the sun of the game
     * @param gameObjects the game object collection that we'll add the sun to
     * @param layer the layer of the sun object
     * @param windowsDimensions window dimensions
     * @param cycleLength time between each day cycle
     * @return the constructed sun object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowsDimensions, float cycleLength){
        Vector2 topLeftCorner = new Vector2(windowsDimensions.x()/SUN_RATIO,
                INITIAL_Y_COORDINATE);
        Vector2 dimensions = new Vector2(SUN_SIZE,SUN_SIZE);
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(topLeftCorner, dimensions,ovalRenderable);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(TAG);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        // the transition of the sun
        Transition<Float> transition = new Transition<>(sun,
                deg -> sun.setCenter(getLocation(deg, windowsDimensions)),
                0f, FULL_CIRCLE_DEGREE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_LOOP,null);

        return sun;

    }

    /**
     * @param deg a given degree
     * @param windowsDimensions window dimensions of the game
     * @return the location that the sun needs to be if its in the given degree
     */
    private static Vector2 getLocation(float deg, Vector2 windowsDimensions){

        Vector2 initialPosition = new Vector2(windowsDimensions.x()/2, INITIAL_DEGREE_LOCATION);
        float radius = (float)Math.sqrt(Math.pow(initialPosition.x() - windowsDimensions.x()/2,
                2) +
                Math.pow(initialPosition.y() - windowsDimensions.y()/2, 2));
        float newX =
                (windowsDimensions.x()/2 + (windowsDimensions.x()/windowsDimensions.y())*radius*(float)Math.sin(Math.toRadians(deg)));
        float newY =
                windowsDimensions.y()/2 - radius*(float)Math.cos(Math.toRadians(deg));

        return new Vector2(newX, newY);
    }


}
