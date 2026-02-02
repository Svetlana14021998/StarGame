package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.Background;
import com.star.app.game.entities.Hero;
import com.star.app.game.helpers.GameResultsBuilder;
import com.star.app.screen.utils.Assets;

import static com.star.app.screen.MenuScreen.isMusicPlay;
import static com.star.app.screen.ScreenManager.ScreenType.RESULT;

/**
 * Экран победы
 */
public class WinScreen extends AbstractScreen {
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
     * Объект для работы с результатами игры
     */
    private GameResultsBuilder gameResults;

    /**
     * Сохраняется текущий герой
     *
     * @param defeatedHero текущий герой
     */
    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    /**
     * Создание объекта
     * Создается стрингбилдер
     * Создается экземпляр класса для работы с результатами
     */
    public WinScreen(SpriteBatch batch) {
        super(batch);
        this.stringBuilder = new StringBuilder();
        this.gameResults = new GameResultsBuilder();
    }

    /**
     * Метод для отображения экрана
     * 1.Текущий герой передается в класс, работающий с результатами
     * 2.Создается заднйи фон
     * 3.Создаются шрифты
     * 4.Добавляется музыка
     * 5.Появляется окно для ввода имени игрока
     */
    @Override
    public void show() {
        gameResults.setHero(defeatedHero);

        this.background = new Background(null);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font48 = Assets.getInstance().getAssetManager().get("fonts/font48.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.music = Assets.getInstance().getAssetManager().get("audio/gameover.mp3");
        this.music.setLooping(true);
        if (isMusicPlay) {
            this.music.play();
        }

        Gdx.input.getTextInput(gameResults.getListener(), "Input your name (max 10 symbols)", "Player1", "");
    }

    /**
     * Метод для вычисления изменения состояний объектов
     */
    public void update(float dt) {
        background.update(dt);
        if (Gdx.input.justTouched()) {
            ScreenManager.getInstance().changeScreen(RESULT);
        }
    }

    /**
     * Метод для рисования. рисуются:
     * 1.Задний фон
     * 2.Сообщение о победе
     * 3.Информация о набранных очках и монетах
     */
    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "YOU WIN!", 0, 600, ScreenManager.SCREEN_WIDTH, Align.center, false);
        stringBuilder.clear();
        stringBuilder.append("Hero score: ").append(defeatedHero.getScore()).append("\n");
        stringBuilder.append("Money: ").append(defeatedHero.getMoney()).append("\n");
        font48.draw(batch, stringBuilder, 0, 400, ScreenManager.SCREEN_WIDTH, Align.center, false);
        font24.draw(batch, "Tap screen to watch all players results...", 0, 40,
            ScreenManager.SCREEN_WIDTH, Align.center, false);
        batch.end();
    }

    /**
     * Метод для очистки ресурсов
     */
    @Override
    public void dispose() {
        background.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
