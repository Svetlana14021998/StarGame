package com.star.app.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

/**
 * Экран для загрузки
 */
public class LoadingScreen extends AbstractScreen {
    /**
     * изображение экрана
     */
    private Texture texture;

    /**
     * На экране загрузки будет отображаться синяя полоса, показывающая прогресс загрузки ресурсов
     */
    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        Pixmap pixmap = new Pixmap(1280, 20, Pixmap.Format.RGB888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void show() {
    }

    /**
     * Метод для рисования.
     * 1.Если ресурсы еще не загрузились, то рисуется полоса, показывающая прогресс загрузки ресурсов.
     * 2.иначе переходим в экран меню
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        if (Assets.getInstance().getAssetManager().update()) {
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget();
        }
        batch.begin();
        batch.draw(texture, 0, 0, 1280 *
            Assets.getInstance().getAssetManager().getProgress(), 20);
        batch.end();
    }

    /**
     * Уничтожение ресурсов при переходе с данного экрана
     */
    @Override
    public void dispose() {
        texture.dispose();
    }
}