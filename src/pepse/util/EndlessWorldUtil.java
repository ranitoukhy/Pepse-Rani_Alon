package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class EndlessWorldUtil {

    // objectAtDelta[k] = all objects with x = avater.x + k
    private static HashMap<Integer, ArrayList<GameObject>> objectsAtDelta = new HashMap<>();

    public static void addObject(int x, GameObject obj, GameObjectCollection gameObjects) {
        if (!objectsAtDelta.containsKey(x)){
            objectsAtDelta.put(x, new ArrayList<>());
        }

        objectsAtDelta.get(x).add(obj);
        gameObjects.addGameObject(obj, Layer.STATIC_OBJECTS);
    }

    public static void removeCol(int x, GameObjectCollection gameObjects) {
        if (!objectsAtDelta.containsKey(x)){
            return;
        }

        for (GameObject obj : objectsAtDelta.get(x)) {
            gameObjects.removeGameObject(obj, Layer.STATIC_OBJECTS);
        }
        objectsAtDelta.remove(x);
    }
    public static boolean containsKey(int x){
        return objectsAtDelta.containsKey(x);
    }


}
