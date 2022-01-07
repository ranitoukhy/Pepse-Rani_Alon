package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.function.Function;

/**
 * a class that represents a block.
 */
public class Block extends GameObject {
    public static final int SIZE = 30;
    public static final String TAG = "block";
    public static final Function<Float, Integer> ROUND = val -> (int)Math.floor(val / Block.SIZE) * Block.SIZE;

    /**
     * constructor of the block.
     * @param topLeftCorner initial position
     * @param renderable the display of the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.setTag(TAG);
    }
}
