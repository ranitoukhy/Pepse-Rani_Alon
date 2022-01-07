package pepse.world;

import danogl.collisions.GameObjectCollection;
import pepse.util.EndlessWorldUtil;

/**
 * Createable interface:
 * each class that implements this interface is responsible for creating and deleting objects in
 * a certain range.
 */
public interface Creatable {
    /**
     * a fucntion that creates objects in a given range
     * @param minX start of the range
     * @param maxX end of the range
     */
    default void createInRange(int minX, int maxX) {
        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);

        for (int x = minX; x <= maxX; x+=Block.SIZE) {
            //if the object doesnt exist, create it and add it to x's object list
            if(!EndlessWorldUtil.tagInX(getTag(), x)) {
                EndlessWorldUtil.addTag(x, getTag());
                create(x);
            }
        }
    }

    /**
     * a fucntion that removes objects in a given range
     * @param minX start of the range
     * @param maxX end of the range
     */
    default void removeInRange(int minX, int maxX) {
        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);
        //remove every item in a given x coordinate
        for (int x = minX; x <= maxX; x += 30) {
            EndlessWorldUtil.removeCol(x, getGameObjects());
        }
    }

    /**
     * a function that returns the GameObjectCollection instance that we add obejcts to
     */
    GameObjectCollection getGameObjects();

    /**
     * a function that creates an object in a given x coordinate
     */
    void create(int x);

    /**
     * a getter of the createable's tag.
     */
    String getTag();
}
