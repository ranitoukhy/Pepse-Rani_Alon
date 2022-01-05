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
     * A simple platformer demo with a circle as an avatar and a few platforms.
     * Move with left and right keys, jump with space, down+space to drop down a platform.
     * @author Dan Nirel
     */


    public class Avatar extends GameObject {
        private static final float VELOCITY_X = 400;
//        private static final float VELOCITY_Y = -650;
private static final float VELOCITY_Y = -300;
        private static final float GRAVITY = 600;
        private static final Color AVATAR_COLOR = Color.DARK_GRAY;
        private static ImageRenderable IDLE_RENDER;
        private static ImageReader imageReader;

        private float lastX;

        private int energy;
        private static final String IDLE = "src/pepse/world/sprite-idle.png";
        private static final String WALK = "src/pepse/world/sprite-walk.png";
        private static final String WALK2 = "src/pepse/world/sprite-walk2.png";
        private static final String FLY = "src/pepse/world/sprite-fly.png";
        private static  AnimationRenderable WALK_ANIMATION;
        private static Renderable FLY_RENDERABLE;

        private UserInputListener inputListener;

        public Avatar(Vector2 pos, UserInputListener inputListener) {
            super(pos, Vector2.ONES.mult(50), new OvalRenderable(AVATAR_COLOR));
            physics().preventIntersectionsFromDirection(Vector2.ZERO);
            transform().setAccelerationY(GRAVITY);
            this.inputListener = inputListener;
            energy = 0;
            this.lastX = getCenter().x();
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            //lastX = getCenter().x();
            renderer().setRenderable(IDLE_RENDER);
            float xVel = 0;
            if (!inputListener.isKeyPressed(KeyEvent.VK_SPACE) && !inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energy < 100){
                energy++;
            }
            if (getVelocity().y() == 0 && energy < 100)
                renderer().setRenderable(IDLE_RENDER);
               // energy ++;

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

            if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
                physics().preventIntersectionsFromDirection(null);
                new ScheduledTask(this, .5f, false,
                        ()->physics().preventIntersectionsFromDirection(Vector2.ZERO));
                return;
            }
            if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
                transform().setVelocityY(VELOCITY_Y);
            if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energy > 0){
                renderer().setRenderable(FLY_RENDERABLE);
                if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
                    renderer().setIsFlippedHorizontally(false);
                }
                else if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
                    renderer().setIsFlippedHorizontally(true);
                }
                transform().setVelocityY(VELOCITY_Y);
                transform().setAccelerationY(200);
                energy --;
            }
            if (getVelocity().y() > 200){transform().setVelocityY(200);}
        }
        public static Avatar create(GameObjectCollection gameObjects,
                                    int layer, Vector2 topLeftCorner,
                                    UserInputListener inputListener,
                                    ImageReader imageReader){
            Avatar.imageReader = imageReader;
            Avatar avatar = new Avatar(topLeftCorner, inputListener);
            gameObjects.addGameObject(avatar, layer);
//            imageReader.readImage("/Users/sigalnaim/IdeaProjects/Pepse/src/pepse/world/mvSatyr" +
//                    ".prev.png",false);

            IDLE_RENDER = imageReader.readImage(IDLE, true);
            avatar.renderer().setRenderable(IDLE_RENDER);
            Renderable renderable_walk = imageReader.readImage(WALK, true);
            Renderable renderable_walk2 = imageReader.readImage(WALK2, true);
            Renderable [] renderables = new Renderable[]{
                    renderable_walk,renderable_walk2};
            FLY_RENDERABLE = imageReader.readImage(FLY,true);
            WALK_ANIMATION =  new AnimationRenderable(renderables, 0.5);



            return avatar;

        }

        public float getDelta()
        {
            float delta = getCenter().x() - lastX;
            lastX = getCenter().x();
            return delta;
        }
    }

