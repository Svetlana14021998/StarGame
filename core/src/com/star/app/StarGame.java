package com.star.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.star.app.screen.ScreenManager.ScreenType.MENU;
import static com.star.app.screen.ScreenManager.getInstance;

/**
 * Основной игровой класс
 */
public class StarGame extends Game {
    /**
     * Внутренняя часть игрового экрана, на которой происходит отрисовка
     */
    private SpriteBatch batch;

    /**
     * Срабатывает в момент запуска игры.
     * инициализирует необходимые ресурсы:
     * 1.Создается batch для отрисоки
     * 2.Инициализируем данные для менеджера экранов, который будет управлять экранами и подгружать необходимые для них ресурсы
     * 3.Переходим на экран Меню
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        getInstance().init(this, batch);
        getInstance().changeScreen(MENU);
    }

    /**
     * Метод для отрисовки
     * dt - время, прошедшее с предыдущей отрисовки.
     * Введение данной переменной позволяет избавиться от привязки к мощности устройства
     */
    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    /**
     * Очистка памяти от ненужных объектов (т.к. JVM не чистит видеопамять)
     */
    @Override
    public void dispose() {
        batch.dispose();
    }
}
