package ru.stargame.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {
    protected final List<T> activeObject = new ArrayList<>();
    protected final List<T> freeObject = new ArrayList<>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObject.isEmpty()){
            object = newObject();
        } else {
            object = freeObject.remove(freeObject.size() - 1);
        }
        activeObject.add(object);
        return object;
    }

    public void updateActiveSprites(float delta) {
        for (Sprite sprite : activeObject) {
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void drawActiveSprites (SpriteBatch batch) {
        for (Sprite sprite : activeObject) {
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    public void freeAllDestroyed () {
        for (int i = 0; i < activeObject.size(); i++) {
            T sprite = activeObject.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public void freeAllActiveObjects() {
        freeObject.addAll(activeObject);
        activeObject.clear();
    }

    public void dispose() {
        activeObject.clear();
        freeObject.clear();
    }

    public List<T> getActiveObject() {
        return activeObject;
    }

    private void free (T object) {
        if (activeObject.remove(object)) {
            freeObject.add(object);
        }
    }


}
