package com.star.app.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

/**
 * Частицы, с помощью которых будут реализовываться эффекты
 */
public class Particle implements Poolable {
    /**
     * Позиция частицы (х;у)
     */
    private Vector2 position;
    /**
     * Скорость частицы (х;у)
     */
    private Vector2 velocity;
    /**
     * Начальный цвет частицы по модели RGB и прозрачность (a1)
     */
    private float r1, g1, b1, a1;
    /**
     * Конечный цвет частицы по модели RGB и прозрачность (a2)
     */
    private float r2, g2, b2, a2;
    /**
     * Признак активности частицы
     */
    private boolean active;
    /**
     * Текущее "время жизни"
     */
    private float time;
    /**
     * Максимальное "время жизни"
     */
    private float timeMax;
    /**
     * Начальный и конечный размеры
     */
    private float size1, size2;

    /**
     * @return позиция частицы
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return скорость частицы
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * @return начальная компонента красного цвета
     */
    public float getR1() {
        return r1;
    }

    /**
     * @return начальная компонента зеленого цвета
     */
    public float getG1() {
        return g1;
    }

    /**
     * @return начальная компонента синего цвета
     */
    public float getB1() {
        return b1;
    }

    /**
     * @return начальная прозрачность частицы
     */
    public float getA1() {
        return a1;
    }

    /**
     * @return конечная компонента красного цвета
     */
    public float getR2() {
        return r2;
    }

    /**
     * @return конечная компонента зеленого цвета
     */
    public float getG2() {
        return g2;
    }

    /**
     * @return конечная компонента синего увета
     */
    public float getB2() {
        return b2;
    }

    /**
     * @return конечная прозрачность частицы
     */
    public float getA2() {
        return a2;
    }

    /**
     * @return текущее время жизни частицы
     */
    public float getTime() {
        return time;
    }

    /**
     * @return максимальное время жизни частицы
     */
    public float getTimeMax() {
        return timeMax;
    }

    /**
     * @return начальный размер
     */
    public float getSize1() {
        return size1;
    }

    /**
     * @return конечный размер
     */
    public float getSize2() {
        return size2;
    }

    /**
     * @return признак активности частицы
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Создание частицы:
     * 1.Позиция (0;0)
     * 2.Скорость (0;0)
     * 3.Начальный и конечный размер 1
     */
    public Particle() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        size1 = 1.0f;
        size2 = 1.0f;
    }

    /**
     * Метод вызывается, когда частицу необходимо сделать видимой
     * Частица становится видимой (active = true)
     * Текущее время жизни - 0.0f
     * Устанавливаются следующие параметры:
     *
     * @param x       координата по оси х
     * @param y       координата по оси у
     * @param vx      скорость по оси х
     * @param vy      скорость по оси у
     * @param timeMax максимальное время жизни частицы
     * @param size1   начальный размер
     * @param size2   конечный размер
     * @param r1      начальная компонента красного цвета
     * @param g1      начальная компонента зеленого цвета
     * @param b1      начальная компонента синего цвета
     * @param a1      начальная прозрачность
     * @param r2      конечная компонента красного цвета
     * @param g2      конечная компонента зеленого цвета
     * @param b2      конечная компонента синего цвета
     * @param a2      конечная прозрачность
     */
    public void init(float x, float y, float vx, float vy,
        float timeMax, float size1, float size2,
        float r1, float g1, float b1, float a1,
        float r2, float g2, float b2, float a2) {
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.r1 = r1;
        this.r2 = r2;
        this.g1 = g1;
        this.g2 = g2;
        this.b1 = b1;
        this.b2 = b2;
        this.a1 = a1;
        this.a2 = a2;
        this.time = 0.0f;
        this.timeMax = timeMax;
        this.size1 = size1;
        this.size2 = size2;
        this.active = true;
    }

    /**
     * При деактивации частицы она становится невидимой (cative = false)
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Метод для вычисления изменения состояния объекта:
     * 1.Увеличивается время жизни.
     * 2.Меняется позиция в зависимости от скорости и времени.
     * 3.Если время жизни частицы больше максимального времени жизни, то она деактивируется
     */
    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if (time > timeMax) {
            deactivate();
        }
    }
}

