package pepse.world;

import danogl.collisions.GameObjectCollection;
import pepse.util.EndlessWorldUtil;

public interface Creatable {

    default void createInRange(int minX, int maxX) {
        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);

        for (int x = minX; x <= maxX; x+=Block.SIZE) {
//            if(!EndlessWorldUtil.containsKey(x))
            if(!EndlessWorldUtil.tagInX(getTag(), x)) {
                EndlessWorldUtil.addTag(x, getTag());
                create(x);
            }
        }
    }

    default void removeInRange(int minX, int maxX) {
        minX = Block.ROUND.apply((float) minX);
        maxX = Block.ROUND.apply((float) maxX);

        for (int x = minX; x <= maxX; x += 30) {
            EndlessWorldUtil.removeCol(x, getGameObjects());
        }
    }

    GameObjectCollection getGameObjects();
    void create(int x);
    String getTag();
}
