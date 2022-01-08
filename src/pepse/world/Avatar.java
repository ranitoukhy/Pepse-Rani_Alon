package pepse.world;


import danogl.*;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.*;
import danogl.gui.*;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * a class that represents the avatar of the game.
 */
public class Avatar extends GameObject {
    public static final int LAYER = Layer.DEFAULT;

    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 600;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final int SIZE_FACTOR = 50;
    private static final int MAX_ENERGY = 100;
    private static final double ENERGY_SUM_FACTOR = 0.5;
    private static final int MAX_Y_VELOCITY = 200;
    private static final double TIME_BETWEEN_CLIPS = 0.5;
    private static ImageRenderable IDLE_RENDER;
    private float lastX;
    private float energy;
    private static final String IDLE = "src/pepse/dan-idle.png";
    private static final String WALK = "src/pepse/dan-walk.png";
    private static final String WALK2 = "src/pepse/dan-walk2.png";
    private static final String FLY = "src/pepse/dan-fly.png";
    private static  AnimationRenderable WALK_ANIMATION;
    private static Renderable FLY_RENDERABLE;

    private UserInputListener inputListener;

    /**
     * constructor of the avatar.
     * @param pos initial position of the avatar
     * @param inputListener the input listener of the game
     */
    public Avatar(Vector2 pos, UserInputListener inputListener) {
        super(pos, Vector2.ONES.mult(SIZE_FACTOR), new OvalRenderable(AVATAR_COLOR));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        energy = MAX_ENERGY;
        this.lastX = getCenter().x();
    }

    /**
     * a function that updates every frame. avatar responds to relevant pressed keyboard keys.
     * @param deltaTime current time
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderer().setRenderable(IDLE_RENDER);
        if (getVelocity().isZero()) {energy += ENERGY_SUM_FACTOR;}
        if (getVelocity().y() == 0 && energy < MAX_ENERGY)
            renderer().setRenderable(IDLE_RENDER);//change avatar appearance
        //check if were moving to the left or to the right
        checkLeftOrRight();
        //check if were jumping
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
            transform().setVelocityY(VELOCITY_Y);
        checkFlying();
        //limit the speed of landing after flying
        if (getVelocity().y() > MAX_Y_VELOCITY){transform().setVelocityY(MAX_Y_VELOCITY);}
    }

    /**
     * a function that creates an avatar.
     * @param gameObjects the game object collection that we'll add the avatar to
     * @param layer the layer thatthe avatar will be in
     * @param topLeftCorner position of the avatar
     * @param inputListener inout listener of the game
     * @param imageReader image reader of the game
     * @return the constructed avatar
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        Avatar avatar = new Avatar(topLeftCorner, inputListener);
        gameObjects.addGameObject(avatar, layer);
        IDLE_RENDER = imageReader.readImage(IDLE, true);
        avatar.renderer().setRenderable(IDLE_RENDER);
        //create all renderable types
        Renderable renderable_walk = imageReader.readImage(WALK, true);
        Renderable renderable_walk2 = imageReader.readImage(WALK2, true);
        Renderable [] renderables = new Renderable[]{
                renderable_walk,renderable_walk2};
        FLY_RENDERABLE = imageReader.readImage(FLY,true);
        WALK_ANIMATION =  new AnimationRenderable(renderables, TIME_BETWEEN_CLIPS);
        return avatar;

    }

    /**
     * a function that returns the distance of x coordinates of the avatar from last frame to
     * current one
     */
    public float getDelta()
    {
        float delta = getCenter().x() - lastX;
        lastX = getCenter().x();
        return delta;
    }

    /**
     * a function that handles the case where the avatar goes to the left or to the right
     */
    private void checkLeftOrRight(){
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setRenderable(WALK_ANIMATION);
            renderer().setIsFlippedHorizontally(true);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            renderer().setRenderable(WALK_ANIMATION);
            renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);
    }

    /**
     * a function that handles the case where the avatar is flying
     */
    private void checkFlying(){
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energy > 0){
            renderer().setRenderable(FLY_RENDERABLE);
            if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
                renderer().setIsFlippedHorizontally(false);
            }
            else if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
                renderer().setIsFlippedHorizontally(true);
            }
            transform().setVelocityY(VELOCITY_Y);
            transform().setAccelerationY(MAX_Y_VELOCITY);
            energy -= ENERGY_SUM_FACTOR;
        }
    }
}

