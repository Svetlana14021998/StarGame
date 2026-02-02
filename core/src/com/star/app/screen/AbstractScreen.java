package com.star.app.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.star.app.screen.ScreenManager.getInstance;

/**
 * Базовый класс для описания экранов
 */
public abstract class AbstractScreen implements Screen {
    protected SpriteBatch batch;

    public AbstractScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    /**
     * Метод срабатывает при изменении размеров экрана
     *
     * @param width ширина экрана
     * @param height высота экрана
     */
    @Override
    public void resize(int width, int height) {
        getInstance().resize(width, height);
    }

    /**
     * Метод срабатывает, если игра ставится на паузу
     */
    @Override
    public void pause() {

    }

    /**
     * Метод срабатывает, когда игра снимается с паузы
     */
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
