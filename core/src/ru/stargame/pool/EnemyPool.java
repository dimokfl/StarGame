package ru.stargame.pool;

import com.badlogic.gdx.audio.Sound;

import ru.stargame.base.SpritesPool;
import ru.stargame.math.Rect;
import ru.stargame.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private final Rect worldBound;
    private final ExplosionPool explosionPool;
    private final BulletPool bulletPool;
    private final Sound bulletSound;

    public EnemyPool(Rect worldBound, ExplosionPool explosionPool, BulletPool bulletPool, Sound bulletSound) {
        this.explosionPool = explosionPool;
        this.worldBound = worldBound;
        this.bulletPool = bulletPool;
        this.bulletSound = bulletSound;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(worldBound, explosionPool, bulletPool,bulletSound);
    }
}
