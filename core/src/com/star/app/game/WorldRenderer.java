package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.controllers.GameController;
import com.star.app.screen.utils.Assets;

import static com.badlogic.gdx.utils.Align.center;
import static com.star.app.screen.ScreenManager.*;

/**
 * Класс, отвечающий за отрисовку
 */
public class WorldRenderer {
    /**
     * Ссылка на основной класс, отвечающий за игровую логику {@link GameController}
     */
    private GameController gc;
    /**
     * Ссылка на внутреннюю часть экрана, на которой будем рисовать
     */
    private SpriteBatch batch;
    /**
     * Шрифт размера 32
     */
    private BitmapFont font32;
    /**
     * Шрифт размера 72
     */
    private BitmapFont font72;
    /**
     * Для работы с текстом
     */
    private StringBuilder stringBuilder;
    /**
     * Буфер, в котором будем рисовать, применять эффекты и потом передавать в батч
     */
    private FrameBuffer frameBuffer;
    /**
     * Изображение, полученное в бкфере
     */
    private TextureRegion frameBufferRegion;
    /**
     * Программы для создания эффектов
     */
    private ShaderProgram shaderProgram;

    /**
     * Создание объекта
     * Получаем нужные шрифты
     * Создаем фреймбуфер для рисования
     * Получаем изображение из буфера
     * Поворачиваем его по оси у ( так как у бефера оконная система координат - ось у направлена вниз)
     * Устанавливаем шейдеры
     */
    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf", BitmapFont.class);
        this.stringBuilder = new StringBuilder();

        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, SCREEN_WIDTH,
            SCREEN_HEIGHT, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        this.frameBufferRegion.flip(false, true);
        this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(),
            Gdx.files.internal("shaders/fragment.glsl").readString());
        if (!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader:" + shaderProgram.getLog());
        }
    }

    /**
     * Метод для рисования
     * Рисуем в фреймбуфере:
     * -задний фон
     * -если уровнень не последний, то рисуем астероиды и бота с его уровенем жизни, если он жив
     * -помощника
     * -если уровень последний и босс жив, то рисуем его и его уровень жизни
     * -героя
     * -пули
     * -бонусы
     * -частицы (эффекты)
     * -информационный текст
     * Рисуем на батче:
     * -включаем шейдер
     * -передаем в него координаты героя  (для создания эффекта затемнения при отдалении от героя)
     * -рисуем содержимое буфера
     * -выключаем шейдер
     * -рисуем информацию о скилах героя
     * -выводим информацию о переходе на новый уровень {@link #buildNewLevelText()}
     */
    public void render() {
        frameBuffer.begin();

        ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);

        if (gc.getLevel() != gc.getBossLevel()) {
            gc.getAsteroidController().render(batch);
            if (gc.getBot().isAlive()) {
                gc.getBot().render(batch);
                gc.getBot().renderHP(batch, font32);
            }
        }

        gc.getBotHelper().render(batch);

        if (gc.getBotBoss().isAlive() && gc.getLevel() == gc.getBossLevel()) {
            gc.getBotBoss().render(batch);
            gc.getBotBoss().renderHP(batch, font32);
        }
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getPowerUpsController().render(batch);
        gc.getParticleController().render(batch);
        gc.getInfoController().render(batch, font32);
        batch.end();
        frameBuffer.end();

        batch.begin();
        batch.setShader(shaderProgram);
        shaderProgram.setUniformf("px", gc.getHero().getPosition().x / SCREEN_WIDTH);
        shaderProgram.setUniformf("py", gc.getHero().getPosition().y / SCREEN_HEIGHT);
        batch.draw(frameBufferRegion, 0, 0);

        batch.setShader(null);

        gc.getHero().renderGUI(batch, font32);

        buildNewLevelText();
        batch.end();

        gc.getStage().draw();
    }

    /**
     * Метод для вывода информации о переходе на новый уровень
     * Если таймер уровня меньше установленного значения:
     * если уровень не последний, то текст - номер уровня
     * если уровень последний, текст - последний уровень
     * Рисуем текст
     */
    private void buildNewLevelText() {
        if (gc.getRoundTimer() <= 3.0f) {
            stringBuilder.clear();
            if (!gc.isBossLevel()) {
                stringBuilder.append("Level ").append(gc.getLevel());
            } else {
                stringBuilder.append("FINAL LEVEL!");
            }
            font72.draw(batch, stringBuilder, 0, HALF_SCREEN_HEIGHT,
                SCREEN_WIDTH, center, false);
        }
    }
}
