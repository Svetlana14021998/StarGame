package com.star.app.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

/**
 * Класс для описания бонусов
 */
public class PowerUp implements Poolable {
    /**
     * Перечисление типов бонусов:
     * 1.Аптечка
     * 2.Монеты
     * 3.Патроны
     */
    public enum Type {
        MEDKIT(0), MONEY(1), AMOS(2);

        public int index;

        Type(int index) {
            this.index = index;
        }
    }

    /**
     * Позиция
     */
    private Vector2 position;
    /**
     * Скорость
     */
    private Vector2 velocity;
    /**
     * Текущее время жизни объекта
     */
    private float time;
    /**
     * Максимальное время жизни объекта
     */
    private static final float MAX_TIME = 7.0f;
    /**
     * Признак активности элемента
     */
    private boolean active;
    /**
     * Тип бонуса
     */
    private Type type;
    /**
     * Сколько бонусов начисляется
     */
    private int power;

    public Type getType() {
        return type;
    }

    public float getTime() {
        return time;
    }

    public int getPower() {
        return power;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void deactivate() {
        active = false;
    }

    /**
     * Создание объекта
     * Создается в заданных координатах с заданной скоростью
     * признак активный - нет
     */
    public PowerUp() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    /**
     * При активации:
     * устанавлиываются тип, позиция, задается скорость
     * признак активный - да
     *
     * @param type  тип бонуса
     * @param x     координата по оси х
     * @param y     координата по оси у
     * @param power сколько бонусов дает
     */
    public void activate(Type type, float x, float y, int power) {
        this.type = type;
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
        this.velocity.nor().scl(50.0f);
        this.active = true;
        this.power = power;
        this.time = 0.0f;
    }

    /**
     * Метод для вычисления изменения состояния
     * объект движется
     * если время его жизни превысило максимальное время жизни, то он деактивируется
     */
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= MAX_TIME) {
            deactivate();
        }
    }
}
