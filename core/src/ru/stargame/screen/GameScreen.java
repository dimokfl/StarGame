package ru.stargame.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.stargame.base.BaseScreen;
import ru.stargame.math.Rect;
import ru.stargame.sprite.Background;
import ru.stargame.sprite.Star;

public class GameScreen extends BaseScreen {

    private static final  int STAR_COUNT = 64;

    private Texture bg;
    private TextureAtlas atlas;

    private Background background;
    private Star[] stars;

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
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars){
            star.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
    }

    private void update(float delta){
        for (Star star : stars){
            star.update(delta);
        }
    }

    private void draw() {
        ScreenUtils.clear(0.3f, 0.5f, 0.4f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars){
            star.draw(batch);
        }
        batch.end();
    }
}
