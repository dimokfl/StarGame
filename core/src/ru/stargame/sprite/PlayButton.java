package ru.stargame.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.stargame.base.ScaledButton;
import ru.stargame.math.Rect;
import ru.stargame.screen.GameScreen;

public class PlayButton extends ScaledButton {

    private static final float HEIGHT = 0.27f;
    private static final float PADDING = 0.03f;

    private final Game game;


    public PlayButton(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
        setLeft(worldBounds.getLeft() + PADDING);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen());
    }
}
