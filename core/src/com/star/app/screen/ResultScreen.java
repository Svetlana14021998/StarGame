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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.Background;
import com.star.app.game.helpers.GameResultsBuilder;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static com.star.app.screen.MenuScreen.isMusicPlay;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.ScreenManager.ScreenType.MENU;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Экран вывода результатов игроков
 */
public class ResultScreen extends AbstractScreen {
    /**
     * Ссылка на класс, отвечающий за работу с результатами игр
     */
    private GameResultsBuilder gameResultsBuilder;
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
     * Компонент, отвечающий за размещение и управление игровыми объектами (кнопки, переключатели и т.д.)
     */
    private Stage stage;

    /**
     * Создание объекта класса
     */
    public ResultScreen(SpriteBatch batch) {
        super(batch);
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Метод для отображения экрана
     * 1.Создается объект класса, отвечающий за результаты игр
     * 2.Создается заний фон
     * 3.Создаем Stage
     * 4.Создаем шрифты
     * 5.Добавляем музыку
     * 6.Создаем и добавляем кнопки
     */
    @Override
    public void show() {
        this.gameResultsBuilder = new GameResultsBuilder();
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font24 = getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font32 = getInstance().getAssetManager().get("fonts/font32.ttf");
        this.font48 = getInstance().getAssetManager().get("fonts/font48.ttf");
        this.music = getInstance().getAssetManager().get("audio/gameover.mp3");
        this.music.setLooping(true);
        if (isMusicPlay) {
            this.music.play();
        }

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        addReturnToMenuBtn(textButtonStyle);
        addClearResultsBtn(textButtonStyle);

        skin.dispose();
    }

    /**
     * Метод для вычисления изменения состояний объектов
     */
    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    /**
     * Метод для рисования. Рисуются:
     * 1.Задний фон
     * 2.Сообщение "результаты игроков"
     * 3.Таблица результатов
     */
    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font48.draw(batch, "Players results:", 0, 700, SCREEN_WIDTH, center, false);
        stringBuilder = gameResultsBuilder.createResultTable();
        font32.draw(batch, stringBuilder, 40, 600, SCREEN_WIDTH, left, false);

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
     * Метод для добавления кнопки "В меню"
     * 1.Создается кнопка
     * 2.Добавялется обработчик события при нажатии кнопки
     *
     * @param style стиль для рисования кнопки
     */
    private void addReturnToMenuBtn(TextButton.TextButtonStyle style) {
        Button btnReturnToMenu = new TextButton("Return To Menu", style);
        btnReturnToMenu.setPosition(520, 70);
        btnReturnToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(MENU);
            }
        });
        stage.addActor(btnReturnToMenu);
    }

    /**
     * Метод для добавления кнопки "Очистить таблицу результатов"
     * 1.Создается кнопка
     * 2.Добавялется обработчик события при нажатии кнопки
     *
     * @param style стиль для рисования кнопки
     */
    private void addClearResultsBtn(TextButton.TextButtonStyle style) {
        Button clearResult = new TextButton("Clear Results", style);
        clearResult.setPosition(120, 70);
        clearResult.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameResultsBuilder.clearResult();
            }
        });
        stage.addActor(clearResult);
    }
}
