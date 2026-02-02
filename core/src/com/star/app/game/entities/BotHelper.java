package com.star.app.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс для описания помощника, который сбрасывает бонусы
 */
public class BotHelper {
    /**
     * Минимальное значение, до которого может быть уменьшен таймер
     */
    public static final float MIN_TIMER = 3.0f;
    /**
     * Размер изображения помощника по х
     */
    private static final float HELPER_SIZE_X = 176;
    /**
     * Половина размера изображения помощника по х
     */
    private static final float HELPER_HALF_SIZE_X = 88;
    /**
     * Размер изображения помощника по у
     */
    private static final float HELPER_SIZE_Y = 104;
    /**
     * Половина размера изображения помощника по у
     */
    private static final float HELPER_HALF_SIZE_Y = 52;
    /**
     * Изображение
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
     * Угол поворота изображения
     */
    private float angle;
    /**
     * Таймер для времени сброса помощи
     */
    private float helpTimer;
    /**
     * Область при попадении героя в которую может сбрасываться помощь
     */
    private Circle helpArea;

    /**
     * Создание объекта
     * Получаем изображение из атласа текстур
     * Устанавливаются позиция, скорость и угол поворота изображения
     * Устанавливается таймер
     * Добавляется зона помощи
     */
    public BotHelper() {
        this.texture = getInstance().getAtlas().findRegion("nio");
        this.position = new Vector2(88, SCREEN_HEIGHT - 52);
        this.velocity = new Vector2(MathUtils.random(50, 100), 0);
        this.angle = 0.0f;
        this.helpTimer = 21.0f;
        this.helpArea = new Circle(position.x, position.y, 200);
    }

    public Circle getHelpArea() {
        return helpArea;
    }

    /**
     * Уменьшение таймера
     *
     * @param count на сколько уменьшается таймер
     */
    public void setHelpTimer(float count) {
        helpTimer -= count;
    }

    public float getHelpTimer() {
        return helpTimer;
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * Метод для рисования
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - HELPER_HALF_SIZE_X, position.y - HELPER_HALF_SIZE_Y, HELPER_HALF_SIZE_X, HELPER_HALF_SIZE_Y, HELPER_SIZE_X,
            HELPER_SIZE_Y, 1, 1, angle);
    }

    /**
     * Метод для вычисления изменения состояния объектов
     * Помощник двигается с заданной скоростью.
     * Зона помощи смешается вместе с ним
     * Если помощник ушел за пределы экрана, то он появляется с другой стороны
     */
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        helpArea.setPosition(position);
        if (position.x >= SCREEN_WIDTH + 88) {
            position.x = 88;
        }
    }
}
