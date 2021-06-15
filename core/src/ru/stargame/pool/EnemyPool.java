package ru.stargame.pool;

import com.badlogic.gdx.audio.Sound;

import ru.stargame.base.SpritesPool;
import ru.stargame.math.Rect;
import ru.stargame.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private final Rect worldBound;
    private final BulletPool bulletPool;
    private final Sound bulletSound;

    public EnemyPool(Rect worldBound, BulletPool bulletPool, Sound bulletSound) {
        this.worldBound = worldBound;
        this.bulletPool = bulletPool;
        this.bulletSound = bulletSound;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(worldBound, bulletPool,bulletSound);
    }
}
