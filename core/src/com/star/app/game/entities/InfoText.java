package com.star.app.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

import static com.badlogic.gdx.graphics.Color.GREEN;

/**
 * Класс для описания текста, появляющегося при подборе бонусов, нанесении урона и крит. ударе
 */
public class InfoText implements Poolable {
    /**
     * Цвет текста
     */
    private Color color;
    /**
     * Текст
     */
    private StringBuilder text;
    /**
     * Активен ли элемент
     */
    private boolean active;
    /**
     * Позиция
     */
    private Vector2 position;
    /**
     * Скорость
     */
    private Vector2 velocity;
    /**
     * Текущее время жизни
     */
    private float time;
    /**
     * Максимальное время жизни
     */
    private float maxTime;

    /**
     * Возвращает признак активности элемента
     *
     * @return активен ли элемент
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Возвращает текст
     *
     * @return текст
     */
    public StringBuilder getText() {
        return text;
    }

    /**
     * Возвращает позицию элемента
     *
     * @return позиция
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Возвращает цвет
     *
     * @return цвет
     */
    public Color getColor() {
        return color;
    }

    /**
     * Создаем объект:
     * 1.Создаем новый StringBuilder для формирования текста
     * 2.Признак активности - нет
     * 3.Устанавливаем позицию и скорость
     * 4.Задаем максимальное время жизни и текущее время жизни (0)
     * 5.Устанавливаем цвет текста
     */
    public InfoText() {
        this.text = new StringBuilder();
        this.active = false;
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(10.0f, 50.0f);
        this.time = 0.0f;
        this.maxTime = 1.5f;
        this.color = GREEN;
    }

    /**
     * Метод для активации (изображение становится видимым) элемента
     * 1.Элемент устанавливается в указанную позицию (х,у)
     * 2.Признак активности - да
     * 3.Очищается стрингбилдер
     * 4.Добавляется указанный текст выбранного цвета
     * 5.Текущее время жизни = 0
     *
     * @param x     координата по оси х
     * @param y     координата по оси у
     * @param text  текст сообщения
     * @param color цвет текста
     */
    public void setup(float x, float y, String text, Color color) {
        this.position.set(x, y);
        this.active = true;
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
        this.color = color;
    }

    /**
     * Метод для активации (изображение становится видимым) элемента
     * 1.Элемент устанавливается в указанную позицию (х,у)
     * 2.Признак активности - да
     * 3.Очищается стрингбилдер
     * 4.Добавляется указанный текст выбранного цвета
     * 5.Текущее время жизни = 0
     *
     * @param x     координата по оси х
     * @param y     координата по оси у
     * @param text  текст сообщения
     * @param color цвет текста
     */
    public void setup(float x, float y, StringBuilder text, Color color) {
        this.position.set(x, y);
        this.active = true;
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
        this.color = color;
    }

    /**
     * Метод для вычисления изменения состояния объектов
     * Объект двигается с заданной скоростью
     * Время жизни объекта увеличивается
     * Когда время жизни объекта станет равным максимальному времени жизни, объект деактивируется
     */
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime) {
            deactivate();
        }
    }

    public void deactivate() {
        active = false;
    }
}
