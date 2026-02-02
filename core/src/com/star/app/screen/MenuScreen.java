package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.entities.Background;

import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.ScreenManager.ScreenType.*;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Экран меню
 */
public class MenuScreen extends AbstractScreen {
    /**
     * Включена ли музыка
     */
    public static boolean isMusicPlay = true;
    /**
     * Включены ли звуки
     */
    public static boolean isSoundPlay = true;
    /**
     * Задний фон
     */
    private Background background;
    /**
     * Шрифт размера 72
     */
    private BitmapFont font72;
    /**
     * Шрифт размера 24
     */
    private BitmapFont font24;
    /**
     * Шрифт размера 18
     */
    private BitmapFont font18;
    /**
     * Компонент, отвечающий за размещение и управление игровыми объектами (кнопки, переключатели и т.д.)
     */
    private Stage stage;
    /**
     * Музыка
     */
    private Music music;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    /**
     * Метод для отображения экрана.
     * 1.Создаем задний фон.
     * 2.Создаем Stage.
     * 3.Создаем шрифты.
     * 4.Stage перехватывает происходящие события (нажатия на кнопки).
     * 5.Создаем Skin (нужен для рисования кнопок).
     * 6.Создаем стили рисования кнопок.
     * 7.Создаем кнопки с обработчиками событий.
     * 8.Очищаем Skin.
     * 9.Добавялем музыку
     */
    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font18 = getInstance().getAssetManager().get("fonts/font18.ttf");
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(getInstance().getAtlas());

        TextButtonStyle textButtonStyle = createTextBtnStyle(skin, "simpleButton", font24, "simpleSkin");
        TextButtonStyle littleBtnStyle = createTextBtnStyle(skin, "smButton", font18, "littleSkin");

        addNewGameBtn(textButtonStyle);
        addExitBtn(textButtonStyle);
        addHelpBtn(textButtonStyle);
        addHResultBtn(textButtonStyle);
        addSwitchMusicBtn(littleBtnStyle);
        addSwitchSoundBtn(littleBtnStyle);

        skin.dispose();

        this.music = getInstance().getAssetManager().get("audio/music.mp3");
        this.music.setLooping(true);
        if (isMusicPlay) {
            this.music.play();
        }
    }

    /**
     * Метод для вычисления изменения состояния
     */
    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    /**
     * Метод для рисования.
     * 1.Рисуем задний фон.
     * 2.Выводим название игры.
     */
    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "Star Game", 0, 600, SCREEN_WIDTH, 1, false);
        batch.end();
        stage.draw();
    }

    /**
     * Метод для очистки ресурсов
     */
    @Override
    public void dispose() {
        background.dispose();
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Создание стиля кнопки
     *
     * @param skin    объект класса, отвечающего за стили
     * @param imgName имя изображения, которое будет использоваться для рисования кнопки
     * @param font    шрифт
     * @param name    имя, по которому из skin можно будет получить данный стиль
     * @return стиль для кнопки
     */
    private TextButtonStyle createTextBtnStyle(Skin skin, String imgName, BitmapFont font, String name) {
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable(imgName);
        textButtonStyle.font = font;
        skin.add(name, textButtonStyle);
        return textButtonStyle;
    }

    /**
     * Создание кнопки "Новая игра"
     * 1.Добавляется кнопка с нужным стилем в нужную позицию
     * 2.Добавляется обработчик события при нажатии на кнопку
     *
     * @param style стиль для кнопки
     */
    private void addNewGameBtn(TextButtonStyle style) {
        Button btnNewGame = new TextButton("New Game", style);
        btnNewGame.setPosition(480, 410);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(GAME);
            }
        });
        stage.addActor(btnNewGame);
    }

    /**
     * Создание кнопки "Выход"
     * 1.Добавляется кнопка с нужным стилем в нужную позицию
     * 2.Добавляется обработчик события при нажатии на кнопку
     *
     * @param style стиль для кнопки
     */
    private void addExitBtn(TextButtonStyle style) {
        Button btnExitGame = new TextButton("Exit Game", style);
        btnExitGame.setPosition(480, 110);
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        stage.addActor(btnExitGame);
    }

    /**
     * Создание кнопки "Полезная информация"
     * 1.Добавляется кнопка с нужным стилем в нужную позицию
     * 2.Добавляется обработчик события при нажатии на кнопку
     *
     * @param style стиль для кнопки
     */
    private void addHelpBtn(TextButtonStyle style) {
        Button btnHelp = new TextButton("Help", style);
        btnHelp.setPosition(480, 310);
        btnHelp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(HELP);
            }
        });
        stage.addActor(btnHelp);
    }

    /**
     * Создание кнопки "Результаты игроков"
     * 1.Добавляется кнопка с нужным стилем в нужную позицию
     * 2.Добавляется обработчик события при нажатии на кнопку
     *
     * @param style стиль для кнопки
     */
    private void addHResultBtn(TextButtonStyle style) {
        Button btnResult = new TextButton("Player's results", style);
        btnResult.setPosition(480, 210);
        btnResult.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(RESULT);
            }
        });
        stage.addActor(btnResult);
    }

    /**
     * Создание кнопки Включение/выключения музыки
     * 1.Добавляется кнопка с нужным стилем в нужную позицию
     * 2.Добавляется обработчик события при нажатии на кнопку
     *
     * @param style стиль для кнопки
     */
    private void addSwitchMusicBtn(TextButtonStyle style) {
        final Button btnMusicStop = new TextButton("Turn Off Music", style);

        btnMusicStop.setPosition(80, 110);
        btnMusicStop.setTransform(true);
        btnMusicStop.setScale(1.5f);

        final Button btnMusicPlay = new TextButton("Turn On Music", style);

        btnMusicPlay.setPosition(80, 110);
        btnMusicPlay.setTransform(true);
        btnMusicPlay.setScale(1.5f);
        btnMusicPlay.setVisible(false);

        btnMusicStop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isMusicPlay = !isMusicPlay;
                music.stop();
                btnMusicStop.setVisible(false);
                btnMusicPlay.setVisible(true);
            }
        });

        btnMusicPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isMusicPlay = !isMusicPlay;
                music.play();
                btnMusicStop.setVisible(true);
                btnMusicPlay.setVisible(false);
            }
        });

        stage.addActor(btnMusicStop);
        stage.addActor(btnMusicPlay);
    }

    /**
     * Создание кнопки Включение/выключения звуков
     * 1.Добавляется кнопка с нужным стилем в нужную позицию
     * 2.Добавляется обработчик события при нажатии на кнопку
     *
     * @param style стиль для кнопки
     */

    private void addSwitchSoundBtn(TextButtonStyle style) {
        final Button btnSoundStop = new TextButton("Turn Off Sound", style);

        btnSoundStop.setPosition(80, 180);
        btnSoundStop.setTransform(true);
        btnSoundStop.setScale(1.5f);

        final Button btnSoundPlay = new TextButton("Turn On Sound", style);

        btnSoundPlay.setPosition(80, 180);
        btnSoundPlay.setTransform(true);
        btnSoundPlay.setScale(1.5f);
        btnSoundPlay.setVisible(false);

        btnSoundStop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isSoundPlay = !isSoundPlay;
                btnSoundStop.setVisible(false);
                btnSoundPlay.setVisible(true);
            }
        });
        btnSoundPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isSoundPlay = !isSoundPlay;
                btnSoundStop.setVisible(true);
                btnSoundPlay.setVisible(false);
            }
        });

        stage.addActor(btnSoundStop);
        stage.addActor(btnSoundPlay);
    }
}
