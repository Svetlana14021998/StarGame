package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.entities.Asteroid;
import com.star.app.game.helpers.ObjectPool;

/**
 * Класс-контроллер, отвечающий за астероиды
 */
public class AsteroidController extends ObjectPool<Asteroid> {
    /**
     * Ссылка на объект класса {@link GameController}, отвечающий за основную игровую логику
     */
    private GameController gc;

    /**
     * Создание астероида
     */
    @Override
    protected Asteroid newObject() {
        return new Asteroid(gc);
    }

    public AsteroidController(GameController gc) {
        this.gc = gc;
    }

    /**
     * Метод для отрисовки активных астероидов
     */
    public void render(SpriteBatch batch) {
        for (Asteroid a : activeList) {
            a.render(batch);
        }
    }

    /**
     * Метод для определения характеристик астероида при его активации
     *
     * @param x     координата по оси х
     * @param y     координата по оси у
     * @param vx    скорость по оси х
     * @param vy    скорость по оси у
     * @param scale масштаб астероида
     */
    public void setup(float x, float y, float vx, float vy, float scale) {
        getActiveElement().activate(x, y, vx, vy, scale);
    }

    /**
     * Метод для вычисления изменения состояния активных астероидов
     */
    public void update(float dt) {
        for (Asteroid asteroid : activeList) {
            asteroid.update(dt);
        }
        checkPool();
    }
}
