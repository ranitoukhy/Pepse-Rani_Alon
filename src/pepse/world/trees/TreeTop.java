package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class TreeTop {
    public static void create(GameObjectCollection gameObjects, Vector2 dimensions, Vector2 trunkTop, int layer)
    {
        int firstX = Block.ROUND.apply(trunkTop.x() - ((int)(dimensions.x()/2)*Block.SIZE));
        int firstY = Block.ROUND.apply(trunkTop.y() - ((int)(dimensions.y()/2)*Block.SIZE));

        Vector2 right = new Vector2(Block.SIZE, 0);
        for (int i = 0; i < dimensions.y(); i++)
        {
            Vector2 position = new Vector2(firstX, firstY+(Block.SIZE*i));
            for (int j = 0; j < dimensions.x(); j++)
            {
                gameObjects.addGameObject(new Leaf(position));
                position = position.add(right);
            }
        }
    }
}

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

    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR)));
        this.topLeftCorner = topLeftCorner;
        setTag(TAG);
//        new Transition(this,
//                deg -> renderer().setRenderableAngle((float)deg),
//                -30f,30f,
//                Transition.LINEAR_INTERPOLATOR_FLOAT,
//                4, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null);
//        new Transition(this,
//                scale -> setDimensions(new Vector2(Block.SIZE,Block.SIZE).mult((float)scale)),
//                1f,0.7f,
//                Transition.LINEAR_INTERPOLATOR_FLOAT,
//                4, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null);
        physics().setMass(0);
        startLifeCycle();
    }

    private void updateScale()
    {
        currScale += (scaleUp) ? 0.05f : -0.05f;
        if(currScale > maxScale) scaleUp = false;
        if(currScale < minScale) scaleUp = true;

        setDimensions(new Vector2(Block.SIZE, Block.SIZE).mult(currScale));
    }

    private void updateAngle()
    {
        currAngle += (angleUp) ? 2 : -2;
        if(currAngle > maxAngle) angleUp = false;
        if(currAngle < minAngle) angleUp = true;
        renderer().setRenderableAngle(currAngle);
    }

    private void fall()
    {
        renderer().fadeOut(FADEOUT_TIME, this::die);
        transform().setVelocityY(VELOCITY_Y);
        horizontalTransition = new Transition<>(
                this,
                vel -> transform().setVelocityX(vel),
                VELOCITY_X,
                -VELOCITY_X,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                5,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    private void die()
    {
        System.out.println("die");
        new ScheduledTask(
                this,
                new Random().nextInt(20),
                false,
                this::startLifeCycle
        );
    }

    private void startLifeCycle() {
        System.out.println("start life cycle");
        setTopLeftCorner(topLeftCorner);
        renderer().fadeIn(0);
        new ScheduledTask(this, new Random().nextFloat(), true, () -> {updateScale(); updateAngle();});
        new ScheduledTask(
                this,
                new Random().nextInt(100),
                false,
                this::fall
        );
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Terrain.TOP_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        removeComponent(horizontalTransition);
        onGround = true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(onGround && transform().getVelocity().x() != 0)
            transform().setVelocityX(0);
    }
}
