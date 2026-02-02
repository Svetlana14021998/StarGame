package com.star.app.game.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.controllers.GameController;
import com.star.app.game.enums.WeaponType;

import static com.badlogic.gdx.math.MathUtils.cosDeg;
import static com.badlogic.gdx.math.MathUtils.sinDeg;
import static com.star.app.screen.MenuScreen.isSoundPlay;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс, описывающий оружие
 */
public class Weapon {
    /**
     * Ссылка на {@link GameController}
     */
    private GameController gc;
    /**
     * Ссылка на корабль {@link Ship} которому принадлежит данное оружие
     */
    private Ship ship;
    /**
     * Тип оружия {@link WeaponType}
     */
    protected WeaponType title;
    /**
     * Период, через который можно стрелять
     */
    private float firePeriod;
    /**
     * Урон, наносимый пулей
     */
    private int damage;
    /**
     * Скорость пули
     */
    private float bulletSpeed;
    /**
     * Максимальное количество пуль
     */
    private int maxBullets;
    /**
     * Текущее количество пуль
     */
    private int curBullets;
    /**
     * Структура хранящая массив из трех значений:
     * х - расстояние от центра коробля
     * у - угол от центра коробля
     * z - направление стрельбы
     */
    private Vector3[] slots;
    /**
     * Звук выстрела
     */
    private Sound shootSound;

    /**
     * @return наносимый урон
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return период стрельбы
     */
    public float getFirePeriod() {
        return firePeriod;
    }

    /**
     * @return максимальное количество пуль
     */
    public int getMaxBullets() {
        return maxBullets;
    }

    /**
     * @return текущее количество пуль
     */
    public int getCurBullets() {
        return curBullets;
    }

    /**
     * @return слоты оружия
     */
    public Vector3[] getSlots() {
        return slots;
    }

    /**
     * При создании оружия текущее количество пуль = максимальному количеству пуль
     *
     * @param gc          ссылка на {@link GameController}
     * @param ship        ссылка на корабль, которому будет принадлежать оружие
     * @param title       тип оружия
     * @param firePeriod  период стрельбы
     * @param damage      наносимый урон
     * @param bulletSpeed скорость пуль
     * @param maxBullets  максимальное количество пуль
     * @param slots       описание слотов для каждой пушки (расстояние от центра корабля, угол от центра корабля, направление стрельбы)
     */
    public Weapon(GameController gc, Ship ship, WeaponType title,
        float firePeriod, int damage, float bulletSpeed,
        int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.ship = ship;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = maxBullets;
        this.slots = slots;
        this.title = title;
        this.shootSound = getInstance().getAssetManager().get("audio/shoot.mp3");
    }

    /**
     * Метод стрельбы:
     * Если количество пуль больше 0, то:
     * 1.Уменьшаем количество пуль на количество выпущенных пуль.
     * 2.Звучит звук выстрела
     * 3.Для каждой пушки вычисляются позиция по осям х, у, вычисляется скорость по осям х,у и создается пуля с вычесленными характеристиками
     *
     * @param count - количество выпущенных патронов
     */
    public void fire(int count) {
        if (curBullets > 0) {
            curBullets -= count;
            if (isSoundPlay) {
                shootSound.play();
            }

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;

                x = ship.getPosition().x + cosDeg(ship.getAngle() + slots[i].y) * slots[i].x;
                y = ship.getPosition().y + sinDeg(ship.getAngle() + slots[i].y) * slots[i].x;

                vx = ship.getVelocity().x + bulletSpeed * cosDeg(ship.getAngle() + slots[i].z);
                vy = ship.getVelocity().y + bulletSpeed * sinDeg(ship.getAngle() + slots[i].z);

                gc.getBulletController().setup(ship, x, y, vx, vy);
            }
        }
    }

    /**
     * Метод для увеличения количества пуль при подборе PowerUp:
     * 1.Запоминаем текущее количество пуль.
     * 2.Прибавляем полученные пули.
     * 3.Если пуль стало больше максимально возможного количества, то устанавливаем текущее значение пуль равное максимальному.
     *
     * @param amount количество подобранных пуль
     * @return на сколько было увеличено количество пуль
     */
    public int addBullets(int amount) {
        int old = curBullets;
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
        return curBullets - old;
    }
}

