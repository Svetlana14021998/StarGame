package com.star.app.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;
import com.star.app.game.helpers.Poolable;

import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;

/**
 * Класс описывающий пулю.
 * реализует интерфейс {@link Poolable}
 */
public class Bullet implements Poolable {
    /**
     * Позволяет определить, что пуля вышла за границы экрана
     */
    private static final float OUTSIDE_SCREEN_DISPLACEMENT = 20;
    /**
     * Ссылка на {@link GameController} - основной класс, отвечающий за игровую логику
     */
    private GameController gc;
    /**
     * Позиция пули
     */
    private Vector2 position;
    /**
     * Скорость пули
     */
    private Vector2 velocity;
    /**
     * Признак активности
     */
    private boolean active;
    /**
     * Владелец пули (кому принадлежит пуля: герою или вражескому кораблю)
     * Влияет на эффект следа от пули
     */
    private Ship owner;

    /**
     * Возвращает владельца пули
     *
     * @return корабль, который вупистил пулю
     */
    public Ship getOwner() {
        return owner;
    }

    /**
     * Скорость
     *
     * @return скорость (x,y), с которой движется пуля
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Позиция
     *
     * @return позиция (x,y), в которой находится пуля
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * признак активности элемента
     *
     * @return активный или неактивный элемент
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * При создании пули:
     * 1.Координаты (0;0).
     * 2.Скорость (0;0).
     * 3.Признак активный - нет
     */
    public Bullet(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    /**
     * Метод для деактивации пули:
     * Проставляется признак активный - нет
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Метод для активации пули:
     * 1.Пуля устанавливается в заданную позицию (x;y).
     * 2.Скорость (vx;vy).
     * 3.Признак активный - да
     * 4.Устанавливается владелец пули
     */
    public void activate(Ship owner, float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        this.owner = owner;
    }

    /**
     * Метод для вычисления изменения состояния пули:
     * 1.Координаты меняются в зависимости от скорости и времени
     * 2.Устанавливается эффект {@link  com.star.app.game.controllers.ParticleController.EffectBuilder#createBulletTraceEffect}
     * 3.Выполняется проверка, что пуля не вылетела за границы игрового экрана {@link #checkSpaceBorder()}
     */
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        gc.getParticleController().getEffectBuilder()
            .createBulletTraceEffect(owner.getWeaponType(), position, velocity);
        checkSpaceBorder();
    }

    /**
     * Метод, проверяющий, что пуля находится в пределах игрового экрана.
     * Если пуля вылетела за пределы экрана, то она деактивируется
     */
    private void checkSpaceBorder() {
        if (position.x < -OUTSIDE_SCREEN_DISPLACEMENT || position.x > SCREEN_WIDTH + OUTSIDE_SCREEN_DISPLACEMENT ||
            position.y < -OUTSIDE_SCREEN_DISPLACEMENT || position.y > SCREEN_HEIGHT + OUTSIDE_SCREEN_DISPLACEMENT) {
            deactivate();
        }
    }
}
