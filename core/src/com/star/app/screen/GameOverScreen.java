package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.Background;
import com.star.app.game.entities.Hero;

import static com.badlogic.gdx.utils.Align.center;
import static com.star.app.screen.MenuScreen.isMusicPlay;
import static com.star.app.screen.MenuScreen.isSoundPlay;
import static com.star.app.screen.ScreenManager.ScreenType.MENU;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Экран Игра закончена (проигрыш)
 */
public class GameOverScreen extends AbstractScreen {
    /**
     * Задний фон
     */
    private Background background;
    /**
     * Шрифт размера 72
     */
    private BitmapFont font72;
    /**
     * Шрифт размера 48
     */
    private BitmapFont font48;
    /**
     * Шрифт размера 24
     */
    private BitmapFont font24;
    /**
     * Герой
     */
    private Hero defeatedHero;
    /**
     * Для работы со строками
     */
    private StringBuilder stringBuilder;
    /**
     * Музыка
     */
    private Music music;
    /**
     * Звук
     */
    private Sound sound;

    /**
     * Сохранение героя
     *
     * @param defeatedHero текущий герой
     */
    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    /**
     * Создание объекта
     */
    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Метод для отображения экрана
     * 1.Создается задний фон
     * 2.Создаются шрифты
     * 3.Добавляется музыка
     */
    @Override
    public void show() {
        this.background = new Background(null);
        this.font72 = getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font48 = getInstance().getAssetManager().get("fonts/font48.ttf");
        this.font24 = getInstance().getAssetManager().get("fonts/font24.ttf");
        this.sound = getInstance().getAssetManager().get("audio/gameoversound.mp3");
        this.music = getInstance().getAssetManager().get("audio/gameover.mp3");
        this.music.setLooping(true);
        if (isMusicPlay) {
            this.music.play();
        }
        if (isSoundPlay) {
            this.sound.play();
        }
    }

    /**
     * Метод для вычисления изменения состояний объектов
     *
     * @param dt
     */
    public void update(float dt) {
        background.update(dt);
        if (Gdx.input.justTouched()) {
            ScreenManager.getInstance().changeScreen(MENU);
        }
    }

    /**
     * Метод для рисования
     * Рисуются:
     * 1.Задний фон
     * 2.Сообщение об окончании игры и информация о набранных очках и монетах
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "Game Over", 0, 600, ScreenManager.SCREEN_WIDTH, center, false);
        stringBuilder.clear();
        stringBuilder.append("Hero score: ").append(defeatedHero.getScore()).append("\n");
        stringBuilder.append("Money: ").append(defeatedHero.getMoney()).append("\n");
        font48.draw(batch, stringBuilder, 0, 400, ScreenManager.SCREEN_WIDTH, center, false);
        font24.draw(batch, "Tap screen to return main menu...", 0, 40,
            ScreenManager.SCREEN_WIDTH, center, false);
        batch.end();
    }

    /**
     * Метод для очистки ресурсов
     */
    @Override
    public void dispose() {
        background.dispose();
    }
}
