package com.star.app.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.star.app.StarGame;
import com.star.app.game.entities.Hero;
import com.star.app.screen.utils.Assets;

import static com.badlogic.gdx.Gdx.graphics;
import static com.star.app.screen.ScreenManager.ScreenType.*;

/**
 * Класс, отвечающий за управление игровыми экранами
 */
public class ScreenManager {
    /**
     * Типы экранов
     */
    public enum ScreenType {
        GAME, MENU, GAMEOVER, WIN, HELP, RESULT
    }

    /**
     * Ширина экрана
     */
    public static final int SCREEN_WIDTH = graphics.getWidth();
    /**
     * Высота экрана
     */
    public static final int SCREEN_HEIGHT = graphics.getHeight();
    /**
     * Половина высоты экрана
     */
    public static final int HALF_SCREEN_HEIGHT = SCREEN_HEIGHT / 2;
    /**
     * Ссылка на основной игровой класс {@link StarGame}
     */
    private StarGame game;
    /**
     * Ссылка на батч {@link SpriteBatch}
     */
    private SpriteBatch batch;
    /**
     * Ссылка на экран загрузки {@link LoadingScreen}
     */
    private LoadingScreen loadingScreen;
    /**
     * Ссылка на основной игровой экран {@link GameScreen}
     */
    private GameScreen gameScreen;
    /**
     * Ссылка на экран меню {@link MenuScreen}
     */
    private MenuScreen menuScreen;
    /**
     * Ссылка на экран проигрыша {@link GameOverScreen}
     */
    private GameOverScreen gameOverScreen;
    /**
     * Ссылка на экран победы {@link WinScreen}
     */
    private WinScreen winScreen;
    /**
     * Ссылка на экран с полезной информацией {@link HelpScreen}
     */
    private HelpScreen helpScreen;
    /**
     * Ссылка на экран таблицы результатов {@link ResultScreen}
     */
    private ResultScreen resultScreen;
    /**
     * Целевой экран, на который переходим
     */
    private Screen targetScreen;
    /**
     * Отвечает за поведение изображения при изменении пропорций экрана
     */
    private Viewport viewport;

    /**
     * Создание экземпляра класса
     */
    private static ScreenManager ourInstance = new ScreenManager();

    /**
     * Возвращает экземпляр класса
     */

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    /**
     * Возвращает поведение экрана при изменении размера
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * приватный комнструктор для создания объекта, реализующего паттерн Одиночка
     */
    private ScreenManager() {
    }

    /**
     * Инициализирует необходимые объекты:
     * 1.Создаются все экраны
     * 2.Устанавливается, что при изменении размера экрана изображение не будет растягиваться, а по бокам будут черные полосы
     */
    public void init(StarGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
        this.winScreen = new WinScreen(batch);
        this.helpScreen = new HelpScreen(batch);
        this.resultScreen = new ResultScreen(batch);
    }

    /**
     * Метод изменения размеров экрана
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    /**
     * Метод перехода между экранами
     * 1.Очищаем ресурсы текущего экрана.
     * 2.Устанавливаем экран загрузки.
     * 3.Указываем, на какой экран переходим, и загружаем нужные ресурсы.
     *
     * @param type тип экрана, на который переходим
     * @param args используется, если при переходе с одного экрана на другой необходимо передать какой-либо объект
     */
    public void changeScreen(ScreenType type, Object... args) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }
        game.setScreen(loadingScreen);

        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAMEOVER:
                targetScreen = gameOverScreen;
                gameOverScreen.setDefeatedHero((Hero) args[0]);
                Assets.getInstance().loadAssets(GAMEOVER);
                break;
            case WIN:
                targetScreen = winScreen;
                winScreen.setDefeatedHero((Hero) args[0]);
                Assets.getInstance().loadAssets(WIN);
                break;
            case HELP:
                targetScreen = helpScreen;
                Assets.getInstance().loadAssets(HELP);
                break;
            case RESULT:
                targetScreen = resultScreen;
                Assets.getInstance().loadAssets(RESULT);
                break;
        }
    }

    /**
     * Метод перехода на нужный экран
     */
    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}
