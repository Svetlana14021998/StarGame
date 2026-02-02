package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.WorldRenderer;
import com.star.app.game.controllers.GameController;

import static com.star.app.screen.ScreenManager.ScreenType.GAME;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс, описывающий основной игровой экран
 */
public class GameScreen extends AbstractScreen {
    /**
     * Ссылка на объект класса {@link GameController}, который отвечает за игровую логику
     */
    private GameController gameController;
    /**
     * Ссылка на объект класса {@link WorldRenderer}, который отвечает за рисование
     */
    private WorldRenderer worldRenderer;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    /**
     * Метод отрабатывает, когда окно становится активным
     * Загружает необходимые ресурсы, инициализирует {@link GameController} и {@link WorldRenderer}
     */
    @Override
    public void show() {
        getInstance().loadAssets(GAME);
        this.gameController = new GameController(batch);
        this.worldRenderer = new WorldRenderer(gameController, batch);
    }

    /**
     * Метод, отвечающий за отрисовку
     */
    @Override
    public void render(float delta) {
        gameController.update(delta);
        worldRenderer.render();
    }

    /**
     * Метод для очисти ресурсов
     */
    @Override
    public void dispose() {
        gameController.dispose();
    }
}