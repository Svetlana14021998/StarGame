package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.entities.Bullet;
import com.star.app.game.entities.Ship;
import com.star.app.game.helpers.ObjectPool;

import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс - контроллер, отвечающий за пули
 */
public class BulletController extends ObjectPool<Bullet> {
    /**
     * Ссылка на основной класс, отвечающий за игровую логику {@link GameController}
     */
    private final GameController gc;
    /**
     * изображение пули
     */
    private final TextureRegion bulletTexture;
    /**
     * Половина размера изображения пули
     */
    private static final float BULLET_HALF_SIZE = 16;

    /**
     * Создание новой пули
     */
    @Override
    protected Bullet newObject() {
        return new Bullet(gc);
    }

    /**
     * При инициализации класса загружаем получаем пули из атласа текстур
     */
    public BulletController(GameController gc) {
        this.gc = gc;
        this.bulletTexture = getInstance().getAtlas().findRegion("bullet");
    }

    /**
     * Метод для рисования активных пуль
     */
    public void render(SpriteBatch batch) {
        for (Bullet b : activeList) {
            batch.draw(bulletTexture, b.getPosition().x - BULLET_HALF_SIZE, b.getPosition().y - BULLET_HALF_SIZE);
        }
    }

    /**
     * Метод, который устанавливает характеристики пули при ее активации
     *
     * @param owner кто выпуслит пулю (герой или вражеский корабль)
     * @param x     координата по оси х
     * @param y     координата по оси у
     * @param vx    скорость по по оси х
     * @param vy    скорость по по оси у
     */
    public void setup(Ship owner, float x, float y, float vx, float vy) {
        getActiveElement().activate(owner, x, y, vx, vy);
    }

    /**
     * Метод, вычисляющий изменение состояния всех активных пуль
     */
    public void update(float dt) {
        for (Bullet bullet : activeList) {
            bullet.update(dt);
        }
        checkPool();
    }
}