package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.EndlessWorldUtil;
import pepse.world.Block;
import pepse.world.Creatable;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * a class representing a treetop built of leaves
 */
public class TreeTop implements Creatable {
    private static final int MIN_RADIUS=2;
    private static final int MAX_RADIUS=4;
    public static final int RESET_TIME = 20;
    public static final int MOVE_HORIZONTAL_TIME = 5;
    public static final int START_FALL_TIME = 100;

    private final GameObjectCollection gameObjects;
    private final int treeX;
    private final int treeHeight;
    private final int radius;
    private final int leafLayer;

    /**
     * constructor
     * @param gameObjects global game objects collection
     * @param treeX the X of the tree this tree top belongs to
     * @param treeHeight the height of the tree this tree top belongs to
     * @param seed seed for random
     * @param leafLayer the layer for the leaves
     */
    public TreeTop(GameObjectCollection gameObjects, int treeX, int treeHeight, int seed, int leafLayer) {
        this.gameObjects = gameObjects;
        this.treeX = treeX;
        this.treeHeight = treeHeight;
        this.radius = (int) (new Random(Objects.hash(treeX,seed)).nextFloat()*(MAX_RADIUS-MIN_RADIUS) + MIN_RADIUS);
        this.leafLayer = leafLayer;

        createInRange(treeX - radius*Block.SIZE, treeX+1+radius*Block.SIZE);
    }


    @Override
    public GameObjectCollection getGameObjects() {
        return this.gameObjects;
    }

    @Override
    public void create(int x) {
        // create a square of leaves
        for (int y = treeHeight-radius*Block.SIZE; y <= treeHeight+radius*Block.SIZE; y+=Block.SIZE)
        {
            EndlessWorldUtil.addObject(x, new Leaf(new Vector2(x,y)), gameObjects, leafLayer);
        }
    }

    @Override
    public String getTag() {
        return Leaf.TAG+treeX;
    }


    /**
     * an inner class representing a single leaf
     */
    class Leaf extends Block {

        private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
        public static final String TAG = "Leaf";
        private static final float FADEOUT_TIME = 10;
        private static final float maxAngle = 30f;
        private static final float minAngle = -30f;

        private static final float minScale = 0.7f;
        private static final float maxScale = 1f;
        private static final float VELOCITY_Y = 50;
        private static final float VELOCITY_X = 20;

        private float currAngle = 0;
        private float currScale = 1;

        private boolean scaleUp;
        private boolean angleUp;

        private Transition<Float> horizontalTransition;
        private final Vector2 topLeftCorner;
        private boolean onGround = false;

        /**
         * constructor
         * @param topLeftCorner position of leaf
         */
        public Leaf(Vector2 topLeftCorner) {
            super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR)));
            this.topLeftCorner = topLeftCorner;
            setTag(TAG);
            physics().setMass(0);
            startLifeCycle();
        }

        /*
         * updates the scale of the leaf for each cycle of the task scheduler
         */
        private void updateScale()
        {
            currScale += (scaleUp) ? 0.05f : -0.05f;
            if(currScale > maxScale) scaleUp = false;
            if(currScale < minScale) scaleUp = true;

            setDimensions(new Vector2(Block.SIZE, Block.SIZE).mult(currScale));
        }

        /*
         * updates the angle of the leaf for each cycle of the task scheduler
         */
        private void updateAngle()
        {
            currAngle += (angleUp) ? 2 : -2;
            if(currAngle > maxAngle) angleUp = false;
            if(currAngle < minAngle) angleUp = true;
            renderer().setRenderableAngle(currAngle);
        }

        /*
         * starts the falling of a leaf
         */
        private void fall()
        {
            // start fading out and remove leaf after
            renderer().fadeOut(FADEOUT_TIME, this::die);

            // make leaf fall down
            transform().setVelocityY(VELOCITY_Y);

            // make leaf move left and right
            horizontalTransition = new Transition<>(
                    this,
                    vel -> transform().setVelocityX(vel),
                    VELOCITY_X,
                    -VELOCITY_X,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    MOVE_HORIZONTAL_TIME,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                    null
            );
        }

        /*
        reset the leaf
         */
        private void die()
        {
            // reset leaf after random time
            new ScheduledTask(
                    this,
                    new Random().nextInt(RESET_TIME),
                    false,
                    this::startLifeCycle
            );
        }

        /*
        starts a new lifecycle for the leaf
         */
        private void startLifeCycle() {
            // reposition to original position
            setTopLeftCorner(topLeftCorner);

            // set alpha = 255
            renderer().fadeIn(0);

            // start movement cycle
            new ScheduledTask(
                    this,
                    new Random().nextFloat(),
                    true, () -> {updateScale(); updateAngle();});

            // start fall in delay
            new ScheduledTask(
                    this,
                    new Random().nextInt(START_FALL_TIME),
                    false,
                    this::fall
            );
        }

        @Override
        public boolean shouldCollideWith(GameObject other) {
            // only collide with top layer blocks of terrain
            return other.getTag().equals(Terrain.TOP_TAG);
        }

        @Override
        public void onCollisionEnter(GameObject other, Collision collision) {
            super.onCollisionEnter(other, collision);
            // when hitting the ground stop transition
            removeComponent(horizontalTransition);
            onGround = true;
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            // if on ground, stop horizontal movement
            if(onGround && transform().getVelocity().x() != 0)
                transform().setVelocityX(0);
        }
    }
}