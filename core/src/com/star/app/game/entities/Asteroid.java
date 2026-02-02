package com.star.app.game.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;
import com.star.app.game.helpers.Poolable;
import com.star.app.game.interfacies.Pushable;
import com.star.app.screen.MenuScreen;
import com.star.app.screen.utils.Assets;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;

/**
 * Класс, описывающий Астероид
 */
public class Asteroid implements Poolable, Pushable {
    /**
     * Скорость, в пределах которой будет произвольно определяться скорость астероида random(-RANDOM_ASTEROID_SPEED,RANDOM_ASTEROID_SPEED)
     */
    public final static float RANDOM_ASTEROID_SPEED = 200f;
    /**
     * Ссылка на объект класса {@link GameController}, в котором реализована основная игровая логика
     */
    private GameController gc;
    /**
     * Картинка с изображением астероида
     */
    private TextureRegion texture;
    /**
     * Позиция
     */
    private Vector2 position;
    /**
     * Скорость
     */
    private Vector2 velocity;
    /**
     * Текущий уровень жизни
     */
    private int hp;
    /**
     * Максимальный уровень жизни
     */
    private int hpMax;
    /**
     * угол показа изображения
     */
    private float angle;
    /**
     * скорость вращения
     */
    private float rotationSpeed;
    /**
     * масштаб изображения
     */
    private float scale;
    /**
     * Признак активности элемента
     */
    private boolean active;
    /**
     * область поражения астероида
     */
    private Circle hitArea;
    /**
     * звук уничтожения астероида
     */
    private Sound explosionSound;
    /**
     * Размер астероида по оси х
     */
    private float sizeX;
    /**
     * Размер астероида по оси y
     */
    private float sizeY;

    /**
     * @return масштаб астероида
     */
    public float getScale() {
        return scale;
    }

    /**
     * @return максимальный уровень жизни
     */
    public int getHpMax() {
        return hpMax;
    }

    /**
     * @return скорость
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * @return область поражения астероида
     */
    public Circle getHitArea() {
        return hitArea;
    }

    /**
     * @return позиция
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return активен или не активен астероид
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Создание астероида:
     * 1.Создается астероид в позиции (0;0) со скоростью (0;0).
     * 2.Определяется область поражения (0;0;0).
     * 3.Признак активности - нет.
     * 4.Загружается изображение астероида.
     * 5.Загружается звук уничтожения астероида.
     */
    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
        this.sizeX = texture.getRegionWidth();
        this.sizeY = texture.getRegionHeight();
        this.explosionSound = Assets.getInstance().getAssetManager().get("audio/explosion.mp3");
    }

    /**
     * Метод для отрисовки
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - sizeX / 2.0f, position.y - sizeY / 2.0f, sizeX / 2.0f, sizeY / 2.0f,
            sizeX, sizeY, scale, scale, angle);
    }

    /**
     * Метод для деактивации астероида
     */
    public void deactivate() {
        active = false;
        if (MenuScreen.isSoundPlay) {
            explosionSound.play();
        }
    }

    /**
     * Активация астероида:
     * 1.Устанавливается позиция, скорость.
     * 2.Макисмальный уровень жизни зависит от размера изображения и уровня игры
     * 3.Угол поворота и скорость вращения определяется произвольно
     * 4.Определяется область поражения астероида (меньше, чем радиус изображения астероида, т.к. изображение неровное)
     *
     * @param x     координата по оси х
     * @param y     координата по оси у
     * @param vx    скорость по оси х
     * @param vy    скорость по оси у
     * @param scale масштаб астероида от исходного изображения
     */
    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.hpMax = (int) ((7 + gc.getLevel() * 2) * scale);//зависомость живучести астероида от уровня игры
        this.hp = hpMax;
        this.angle = random(0.0f, 360.0f);
        this.rotationSpeed = random(-180.0f, 180.0f);
        this.hitArea.setPosition(position);
        this.scale = scale;
        this.active = true;
        this.hitArea.setRadius((sizeX + sizeY) * scale * 0.9f / 4.0f);
    }

    /**
     * Получение урона астероидом
     *
     * @param amount нанесенный урон
     */
    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.5) {
                createLittleAsteroids();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод для вычисления изменения состояния астероида
     * 1.изменяется позиция астероида.
     * 2.Выполняется вращение.
     * 3.Проверка, что астероид не вышел за пределы экрана {@link #checkMoveToOtherScreenSide()}
     */
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
        checkMoveToOtherScreenSide();
        hitArea.setPosition(position);
    }

    /**
     * Проверка вылета астероида за границы экрана.
     * Если астероид вылетел за границы экрана, то он появляется с другой стороны
     */
    private void checkMoveToOtherScreenSide() {
        if (position.x < -hitArea.radius) {
            position.x = SCREEN_WIDTH + hitArea.radius;
        }
        if (position.x > SCREEN_WIDTH + hitArea.radius) {
            position.x = -hitArea.radius;
        }
        if (position.y < -hitArea.radius) {
            position.y = SCREEN_HEIGHT + hitArea.radius;
        }
        if (position.y > SCREEN_HEIGHT + hitArea.radius) {
            position.y = -hitArea.radius;
        }
    }

    /**
     * Метод для создания астероидов меньшего размера при уничтожении астероида
     */
    private void createLittleAsteroids() {
        for (int i = 0; i < 3; i++) {
            gc.getAsteroidController().setup(position.x, position.y,
                random(-RANDOM_ASTEROID_SPEED, RANDOM_ASTEROID_SPEED),
                random(-RANDOM_ASTEROID_SPEED, RANDOM_ASTEROID_SPEED),
                scale - 0.2f);
        }
    }
}
