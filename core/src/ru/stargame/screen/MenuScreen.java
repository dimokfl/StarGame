package ru.stargame.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.stargame.base.BaseScreen;
import ru.stargame.math.Rect;
import ru.stargame.sprite.Background;
import ru.stargame.sprite.Logo;

public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Texture badlogic;

    private Background background;
    private Logo logo;


    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        badlogic = new Texture("badlogic.jpg");
        logo = new Logo(badlogic);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        logo.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        logo.update(delta);
        ScreenUtils.clear(0.3f, 0.5f, 0.4f, 1);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        badlogic.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        logo.touchDown(touch, pointer, button);
        return false;
    }
}
