package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.Background;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static com.star.app.screen.MenuScreen.isMusicPlay;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.ScreenManager.ScreenType.MENU;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Экран с полезной информацией
 */
public class HelpScreen extends AbstractScreen {
    /**
     * Задний фон
     */
    private Background background;
    /**
     * Шрифт размера 24
     */
    private BitmapFont font24;
    /**
     * Шрифт размера 32
     */
    private BitmapFont font32;
    /**
     * Шрифт размера 48
     */
    private BitmapFont font48;
    /**
     * Для работы со строками
     */
    private StringBuilder stringBuilder;
    /**
     * Музыка
     */
    private Music music;

    /**
     * Создание объекта
     */
    public HelpScreen(SpriteBatch batch) {
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
        this.font24 = getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font32 = getInstance().getAssetManager().get("fonts/font32.ttf");
        this.font48 = getInstance().getAssetManager().get("fonts/font48.ttf");
        this.music = getInstance().getAssetManager().get("audio/gameover.mp3");
        this.music.setLooping(true);
        if (isMusicPlay) {
            this.music.play();
        }
    }

    /**
     * Мето для вычисления изменения состояния объектов
     */
    public void update(float dt) {
        background.update(dt);
        if (Gdx.input.justTouched()) {
            ScreenManager.getInstance().changeScreen(MENU);
        }
    }

    /**
     * Метод для ресования.
     * Рисуются:
     * Задний фон
     * Информация о кавишах управления и описание игры
     */
    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        createKeysUsedText();
        createGameMissionDescriptionText();
        font24.draw(batch, "Tap screen to return main menu...", 0, 40, SCREEN_WIDTH, center, false);
        batch.end();
    }

    /**
     * Метод для создания и вывода на экран информации о клавишах управления
     */
    private void createKeysUsedText() {
        font48.draw(batch, "KEYS USED:", 0, 700, SCREEN_WIDTH, center, false);
        stringBuilder.clear();
        stringBuilder.append("W or UP - forward").append("\n");
        stringBuilder.append("S or DOWN - backward").append("\n");
        stringBuilder.append("A or LEFT- counterclockwise rotation").append("\n");
        stringBuilder.append("D or RIGHT- clockwise rotation").append("\n");
        stringBuilder.append("SPACE - fire").append("\n");
        stringBuilder.append("U - open shop").append("\n");
        font32.draw(batch, stringBuilder, 40, 650, SCREEN_WIDTH, left, false);
    }

    /**
     * Метод для создания и вывода на экран описания игры
     */
    private void createGameMissionDescriptionText() {
        font48.draw(batch, "The mission of the Game:", 0, 400, SCREEN_WIDTH, center, false);
        stringBuilder.clear();
        stringBuilder.append("You must destroy asteroids and bots. When they are destroyed, you can get powerUps").append("\n");
        stringBuilder.append(" (amos, medkit, and coins). You can use the coins to improve your skills in the shop.").append("\n");
        stringBuilder.append(" You can purchase more powerful weapons, increase your current and maximum HP, ").append("\n");
        stringBuilder.append("and a magnetic field that attracts powerUps. Additionally, there is a flying saucer ").append("\n");
        stringBuilder.append("at the top of the screen that drops powerUps when you approach it. You can also ").append("\n");
        stringBuilder.append("improve the availability of powerUps in the shop.");
        font32.draw(batch, stringBuilder, 40, 350, SCREEN_WIDTH, left, false);
    }

    /**
     * Метод для очистки экрана
     */
    @Override
    public void dispose() {
        background.dispose();
    }
}
