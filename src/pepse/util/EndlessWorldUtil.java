package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * a class that is responsible for containing, adding and removing game objects for each x
 * coordinate in the current frame
 */
public class EndlessWorldUtil {
    // a hashmap that maps a given x coordinate to an ArrayList of game objects that appear in it.
    private static HashMap<Integer, ArrayList<GameObject>> objectsAtX = new HashMap<>();
    // a hashmap that maps a given tag of an object to the layer that the object appears in.
    private static HashMap<String, Integer> layersByTags = new HashMap<>();
    // a hashmap that maps a given x coordinate to an ArrayList of tags that appear in it.
    private static HashMap<Integer, ArrayList<String>> tagsByX = new HashMap<>();

    /**
     * a function that adds an object to a given x coordinate
     * @param x - the coordinate we will add items to
     * @param obj - the object we want to add
     * @param gameObjects the game object collection to which we'll add the object to
     */
    public static void addObject(int x, GameObject obj, GameObjectCollection gameObjects) {
        addObject(x, obj, gameObjects, Layer.STATIC_OBJECTS);
    }
    /**
     * a function that adds an object to a given x coordinate
     * @param x - the coordinate we will add items to
     * @param obj - the object we want to add
     * @param gameObjects - the game object collection to which we'll add the object to
     * @param layer - the layer of the object that we'll add (is Layer.STATIC_OBJECTS by defualt)
     */
    public static void addObject(int x, GameObject obj, GameObjectCollection gameObjects, int layer) {
        if (!objectsAtX.containsKey(x)){//add the item only if its not already in the list
            objectsAtX.put(x, new ArrayList<>());
        }

        objectsAtX.get(x).add(obj);
        layersByTags.put(obj.getTag(), layer);
        gameObjects.addGameObject(obj, layer);
    }

    /**
     * a function that adds a given tag to an x coordinate
     * @param x the x coordinate we add the objects to
     * @param tag the tag we want to add
     */
    public static void addTag(int x, String tag) {
        if (!tagsByX.containsKey(x)){
            tagsByX.put(x, new ArrayList<>());
        }

        tagsByX.get(x).add(tag);
    }

    /**
     * a function that removes every game object that appears in a given x coordinate
     */
    public static void removeCol(int x, GameObjectCollection gameObjects) {
        if (!objectsAtX.containsKey(x)){
            return;
        }
        for (GameObject obj : objectsAtX.get(x)) {//remove every object in x coordinate
            int layer = layersByTags.getOrDefault(obj.getTag(), Layer.STATIC_OBJECTS);
            gameObjects.removeGameObject(obj, layer);
        }
        objectsAtX.remove(x);
        tagsByX.remove(x);
    }

    /**
     * @param tag- a string representing a tag
     * @param x an x coordinate
     * @return true of the tag exists in the given x coordinate, false otherwise.
     */
    public static boolean tagInX(String tag, int x) {
           if(!tagsByX.containsKey(x)) return false;
           return tagsByX.get(x).contains(tag);
    }

}
