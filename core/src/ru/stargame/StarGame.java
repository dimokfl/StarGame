package ru.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture img;
	private TextureRegion region;

	private int x;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		/* Для вырезания региона нужно указать картинку источник, указать начало координат
		с которых начинается выделение региона относительно начала координат самой картинки
		а не начала глобальных координат. Последние две цифры задают высоту и длину вырезвемого
		региона от начала заданных координат.*/
		region = new TextureRegion(img, 10, 10, 200, 200);
	}

	@Override
	public void render () {
		x++;
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, x, 0);
		/* Для отрисовки региона вызываем batch и указываем координаты начала отрисовки
		и конечный размер картинки (по факту назначаем масштаб)
		если последние две цифры не ставить то будет отрисован оригинальный размер. */
		batch.draw(region, 300, 300, 150, 150);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
