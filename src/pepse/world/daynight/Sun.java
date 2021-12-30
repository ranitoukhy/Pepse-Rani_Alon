package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun{

    public static final String TAG = "sun";
    public static final int SUN_SIZE = 100;
    public static final int SUN_RATIO = 2;

    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowsDimensions, float cycleLength){
        Vector2 topLeftCorner = new Vector2(windowsDimensions.x()/SUN_RATIO,
                10);
        Vector2 dimensions = new Vector2(SUN_SIZE,SUN_SIZE);
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(topLeftCorner, dimensions,ovalRenderable);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(TAG);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        Transition transition = new Transition(sun,
                deg -> sun.setCenter(getLocation((float)deg, windowsDimensions)),
                0f,360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_LOOP,null);


        return sun;

    }
    private static Vector2 getLocation(float deg, Vector2 windowsDimensions){

        Vector2 initialPosition = new Vector2(windowsDimensions.x()/2, 50);
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
