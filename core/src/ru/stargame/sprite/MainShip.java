package ru.stargame.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.stargame.base.Sprite;
import ru.stargame.math.Rect;
import ru.stargame.pool.BulletPool;

public class MainShip extends Sprite {

    private static final float HEIGHT = 0.15f;
    private static final float PADDING = 0.05f;
    private static final int INVALID_POINTER = -1;
    private static final float RELOAD_INTERVAL = 0.2f;

    private final Vector2 v0 = new Vector2(0.5f, 0);
    private final Vector2 v = new Vector2();

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private  Rect worldBounds;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletV;
    private Vector2 bulletPos;
    private Sound bulletSound;

    private float reloadTimer;



    public MainShip(TextureAtlas atlas, BulletPool bulletPool, Sound bulletSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletSound = bulletSound;
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletPos = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= RELOAD_INTERVAL) {
            reloadTimer = 0;
            shoot();
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x){
            if(leftPointer != INVALID_POINTER){
                return false;
            }
            leftPointer = pointer;
            movieLeft();
        } else {
            if(rightPointer != INVALID_POINTER){
                return false;
            }
            rightPointer = pointer;
            movieRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer){
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER){
                movieRight();
            } else stop();
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                movieLeft();
            } else stop();
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                movieLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                movieRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if(pressedRight){
                    movieRight();
                } else stop();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if(pressedLeft){
                    movieLeft();
                } else stop();
                break;
        }
        return false;
    }

    private void movieRight(){
        v.set(v0);
    }

    private void movieLeft(){
        v.set(v0).rotateDeg(180);
    }

    private void stop(){
        v.setZero();
    }

    private void shoot(){
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, 1, 0.01f);
        bulletSound.play();
    }

}
