package com.star.app.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;
import com.star.app.game.enums.OwnerType;
import com.star.app.game.enums.WeaponType;
import com.star.app.game.interfacies.Pushable;

import static com.badlogic.gdx.math.MathUtils.cosDeg;
import static com.badlogic.gdx.math.MathUtils.sinDeg;
import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;

/**
 * Класс для описания корабля
 */
public abstract class Ship implements Pushable {
    /**
     * Ссылка на {@link GameController}
     */
    protected GameController gc;
    /**
     * изображение
     */
    protected TextureRegion texture;
    /**
     * позиция
     */
    protected Vector2 position;
    /**
     * скорость
     */
    protected Vector2 velocity;
    /**
     * угол поворота изображения
     */
    protected float angle;
    /**
     * мощность двигателя
     */
    protected float enginePower;
    /**
     * таймер стрельбы
     */
    protected float fireTimer;
    /**
     * максимальный уровень жизни
     */
    protected int hpMax;
    /**
     * текущий уровень жизни
     */
    protected int hp;
    /**
     * текущее оружие
     */
    protected Weapon currentWeapon;
    /**
     * зона поражения
     */
    protected Circle hitArea;
    /**
     * Доступное оружие
     */
    protected Weapon[] weapons;
    /**
     * Номер текущего оружия
     */
    protected int weaponNum;
    /**
     * Тип корабля
     */
    protected OwnerType ownerType;
    /**
     * Тип оружия
     */
    protected WeaponType weaponType;
    protected  float sizeX;
    protected float sizeY;

    /**
     * @param hp текущий уровень жизни
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     *Воскрешение корабля (текущий уровень жизни равен максимальному)
     */
    public void resurrection() {
        hp = hpMax;
    }

    /**
     * @return максимальный уровень жизни
     */
    public int getHpMax() {
        return hpMax;
    }

    /**
     * @return тип оружия
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * @return номер текущего оружия
     */
    public int getWeaponNum() {
        return weaponNum;
    }

    /**
     * @return текущий уровень жизни
     */
    public int getHp() {
        return hp;
    }

    /**
     * @return тип игрока
     */
    public OwnerType getOwnerType() {
        return ownerType;
    }

    /**
     * @return область поражения корабля
     */
    public Circle getHitArea() {
        return hitArea;
    }

    /**
     * @return текущее оружие
     */
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     * @return все доступное оружие
     */
    public Weapon[] getWeapons() {
        return weapons;
    }

    /**
     * @return жив ли корабль
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * @return угол поворота изображения
     */
    public float getAngle() {
        return angle;
    }

    /**
     * @return скорость
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * @return позиция
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Создание корабля
     * @param gc ссылка на {@link GameController}
     * @param hpMax максимальный уровень жизни
     * @param x координата х
     * @param y координата у
     */
    public Ship(GameController gc, int hpMax, float x, float y) {
        this.gc = gc;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
    }

    /**
     * Метод для вычисления изменения состояния
     * 1.Увеличивается таймер стрельбы
     * 2.Корабль двигается с заданной скоростью
     * 3.Зона поражения перемещается с кораблем
     * 4.Торможение корабля
     * 5.Проверка столкновения с границами экрана
     */
    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        float stopKoef = 1.0f - 1.0f * dt;
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
        checkSpaceBorders();
    }

    /**
     * Метод для стрельбы:
     * 1.Если текущий таймер стрельбы больше заданного в оружии периода стрельбы, то:
     *  - сбрасываем таймер на 0
     *  - получаем количество пуль, которые выпускает корабль
     *  - выпускаем заданное количество пуль
     */
    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            int gunCount = currentWeapon.getSlots().length;
            currentWeapon.fire(gunCount);
        }
    }

    /**
     * Проверка столкновения корабля с границами экрана
     * В случае столкновения корабль отталкивается от границы
     */
    private void checkSpaceBorders() {
        if (position.x < sizeX / 2.0f) {
            position.x = sizeX / 2.0f;
            velocity.x *= -1;
        }
        if (position.x > SCREEN_WIDTH - sizeX / 2.0f) {
            position.x = SCREEN_WIDTH - sizeX / 2.0f;
            velocity.x *= -1;
        }
        if (position.y < sizeY / 2.0f) {
            position.y = sizeY;
            velocity.y *= -1;
        }
        if (position.y > SCREEN_HEIGHT - sizeY) {
            position.y = SCREEN_HEIGHT - sizeY;
            velocity.y *= -1;
        }
    }

    /**
     * Метод для рисования
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - sizeX / 2.0f, position.y - sizeY / 2.0f, sizeX / 2.0f, sizeY / 2.0f, sizeX, sizeY, 1, 1,
            angle);
    }

    /**
     * Метод получения урона
     * @param amount на сколько уменьшается жизнь
     */
    public void takeDamage(int amount) {
        hp -= amount;
    }

    /**
     * Метод для движения корабля вперед
     */
    public void accelerate(float dt) {
        velocity.x += cosDeg(angle) * enginePower * dt;
        velocity.y += sinDeg(angle) * enginePower * dt;
    }

    /**
     *
     * Метод для движения корабля назад
     */
    public void brake(float dt) {
        velocity.x -= cosDeg(angle) * enginePower * dt / 2;
        velocity.y -= sinDeg(angle) * enginePower * dt / 2;
    }

    /**
     * Вращение корабля
     * @param rotationSpeed скорость вращения
     */
    public void rotate(float rotationSpeed, float dt) {
        angle += rotationSpeed * dt;
    }
}
