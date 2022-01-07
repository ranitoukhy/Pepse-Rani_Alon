package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

import java.util.ArrayList;
import java.util.HashMap;

public class EndlessWorldUtil {

    private static HashMap<Integer, ArrayList<GameObject>> objectsAtX = new HashMap<>();
    private static HashMap<String, Integer> layersByTags = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> tagsByX = new HashMap<>();

    public static void addObject(int x, GameObject obj, GameObjectCollection gameObjects) {
        addObject(x, obj, gameObjects, Layer.STATIC_OBJECTS);
    }

    public static void addObject(int x, GameObject obj, GameObjectCollection gameObjects, int layer) {
        if (!objectsAtX.containsKey(x)){
            objectsAtX.put(x, new ArrayList<>());
        }

        objectsAtX.get(x).add(obj);
        layersByTags.put(obj.getTag(), layer);
        gameObjects.addGameObject(obj, layer);
    }

    public static void addTag(int x, String tag) {
        if (!tagsByX.containsKey(x)){
            tagsByX.put(x, new ArrayList<>());
        }

        tagsByX.get(x).add(tag);
    }

    public static void removeCol(int x, GameObjectCollection gameObjects) {
        if (!objectsAtX.containsKey(x)){
            return;
        }
        for (GameObject obj : objectsAtX.get(x)) {
            int layer = layersByTags.getOrDefault(obj.getTag(), Layer.STATIC_OBJECTS);
            gameObjects.removeGameObject(obj, layer);
        }
        objectsAtX.remove(x);
        tagsByX.remove(x);
    }

    public static boolean containsKey(int x){
        return objectsAtX.containsKey(x);
    }
    public static boolean tagInX(String tag, int x) {
           if(!tagsByX.containsKey(x)) return false;
           return tagsByX.get(x).contains(tag);
    }

}
