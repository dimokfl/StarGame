package ru.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.stargame.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private static final float LEN = 2f;

    private Texture img;
    private Texture background;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 dist;
    private Vector2 tmp;

    @Override
    public void show() {
        super.show();
        img = new Texture("badlogic.jpg");
        background = new Texture("background.jpg");
        pos = new Vector2();
        v = new Vector2();
        dist = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        batch.draw(background, 0, 0, 1024, 512);
        batch.draw(img, pos.x, pos.y);
        batch.end();
        tmp.set(dist);
        if (tmp.sub(pos).len() <= v.len()){
            pos.set(dist);
            v.setZero();
        } else {
            pos.add(v);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
        background.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dist.set(screenX, Gdx.graphics.getHeight() - screenY);
        v.set(dist.cpy().sub(pos)).setLength(LEN);
        return false;
    }
}
