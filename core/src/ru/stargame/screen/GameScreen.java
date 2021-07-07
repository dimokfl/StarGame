package ru.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

import ru.stargame.base.BaseScreen;
import ru.stargame.math.Rect;
import ru.stargame.pool.BulletPool;
import ru.stargame.pool.EnemyPool;
import ru.stargame.pool.ExplosionPool;
import ru.stargame.sprite.Background;
import ru.stargame.sprite.Bullet;
import ru.stargame.sprite.EnemyShip;
import ru.stargame.sprite.GameOver;
import ru.stargame.sprite.MainShip;
import ru.stargame.sprite.NewGameButton;
import ru.stargame.sprite.Star;
import ru.stargame.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final  int STAR_COUNT = 64;

    private enum State {PLAYNG, GAME_OVER}

    private Texture bg;
    private TextureAtlas atlas;

    private Background background;
    private Star[] stars;
    private GameOver gameOver;
    private NewGameButton newGameButton;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private MainShip mainShip;

    private Sound explosionSound;
    private Sound laserSound;
    private Sound bulletSound;
    private Music music;

    private EnemyEmitter enemyEmitter;
    private State state;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        gameOver = new GameOver(atlas);
        newGameButton = new NewGameButton(atlas, this);
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        enemyPool = new EnemyPool(worldBounds, explosionPool, bulletPool, bulletSound);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        mainShip = new MainShip(atlas, explosionPool, bulletPool, laserSound);
        enemyEmitter = new EnemyEmitter(worldBounds, enemyPool, atlas);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        state =State.PLAYNG;
    }

    public void startNewGame(){
        mainShip.startNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        state = State.PLAYNG;
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars){
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGameButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        bulletSound.dispose();
        music.dispose();
        laserSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYNG) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYNG) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYNG) {
            mainShip.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER){
            newGameButton.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYNG) {
            mainShip.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER){
            newGameButton.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);

        if (state == State.PLAYNG) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta);
        } else if (state == State.GAME_OVER) {
            newGameButton.update(delta);
        }
    }

    private void checkCollisions(){
        List<EnemyShip> enemyShipList = enemyPool.getActiveObject();
        for (EnemyShip enemyShip : enemyShipList){
            if (enemyShip.isDestroyed()){
                continue;
            }
            float minDist = enemyShip.getHalfWidth() + mainShip.getHalfWidth();
            if (enemyShip.pos.dst(mainShip.pos) < minDist){
                enemyShip.destroy();
                mainShip.damage(enemyShip.getDamage() * 2);
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObject();
        for (Bullet bullet : bulletList){
            if (bullet.isDestroyed()){
                continue;
            }
            if (bullet.getOwner() == mainShip){
                for (EnemyShip enemyShip : enemyShipList){
                    if (enemyShip.isDestroyed()){
                        continue;
                    }
                    if (enemyShip.isBulletCollision(bullet)){
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroy();
                    }
                }
            } else {
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }
        if(mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw() {
        ScreenUtils.clear(0.3f, 0.5f, 0.4f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars){
            star.draw(batch);
        }
        if (state == State.PLAYNG) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else {
            gameOver.draw(batch);
            newGameButton.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }
}
