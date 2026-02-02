package com.star.app.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;

import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс для описания заднего фона
 */
public class Background {

    /**
     * Внутренний класс, описывающий Звезду
     */
    private class Star {
        /**
         * Позиция звезды (x,y)
         */
        private Vector2 position;
        /**
         * Скорость звезды (x,y)
         */
        private Vector2 velocity;
        /**
         * Масштаб
         */
        private float scale;

        /**
         * Создание звезды (создается в произвольном месте с произвольной скоростью и произвольным масштабом)
         */

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, SCREEN_WIDTH + 200),
                MathUtils.random(-200, SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        /**
         * Метод для вычислений.
         * Определяет изменение позиции звезды:
         * -если есть GameController (в случае игрового экрана), то звезда движется в сторону героя
         * -иначе звезда движется со своей скоростью
         * Если звезда вылетела за границу экрана, то она появится с другой стороны
         */
        public void update(float dt) {
            if (gc != null) {
                position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
                position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;
            } else {
                position.mulAdd(velocity, dt);
            }

            if (position.x < -200) {
                position.x = SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }

    /**
     * Количество звезд на заднем фоне
     */
    private final int STAR_COUNT = 1000;
    /**
     * Размер звезды
     */
    private static final float STAR_SIZE = 16;
    /**
     * Половина размера звезды
     */
    private static final float STAR_HALF_SIZE = 8;
    /**
     * Ссылка на {@link GameController} - основной класс, отвечающий за игровую логику
     */
    private GameController gc;
    /**
     * изображение заднего фона
     */
    private Texture textureCosmos;
    /**
     * изображение звезды
     */
    private TextureRegion textureStar;
    /**
     * Массив звезд
     */
    private Star[] stars;

    /**
     * Создание объекта
     */
    public Background(GameController gc) {
        this.textureCosmos = new Texture("images/bg.png");
        this.textureStar = getInstance().getAtlas().findRegion("star16");
        this.gc = gc;
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    /**
     * Отрисовка заднего фона и звезд
     * Для создания эффекта мерцания звезд с некоторой вероятностью прорисовываем в ту же точку еще одну звезду с масштабом в 2 раза больше
     */
    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);

        for (int i = 0; i < stars.length; i++) {
            batch.draw(textureStar, stars[i].position.x - STAR_HALF_SIZE, stars[i].position.y - STAR_HALF_SIZE, STAR_HALF_SIZE, STAR_HALF_SIZE, STAR_SIZE,
                STAR_SIZE, stars[i].scale, stars[i].scale, 0);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, stars[i].position.x - STAR_HALF_SIZE, stars[i].position.y - STAR_HALF_SIZE, STAR_HALF_SIZE, STAR_HALF_SIZE, STAR_SIZE,
                    STAR_SIZE, stars[i].scale * 2, stars[i].scale * 2, 0);
            }
        }
    }

    /**
     * Метод, который будет вычислять изменение позиции каждой звезды на заднем фоне
     */
    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }

    /**
     * Очистка ресурсов видеопамяти
     */
    public void dispose() {
        textureCosmos.dispose();
    }
}
