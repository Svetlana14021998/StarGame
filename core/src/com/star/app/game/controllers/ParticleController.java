package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.entities.Particle;
import com.star.app.game.entities.PowerUp;
import com.star.app.game.enums.OwnerType;
import com.star.app.game.enums.WeaponType;
import com.star.app.game.helpers.ObjectPool;

import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;
import static com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс-контроллер для создания различных эффектов с помощью частиц
 */
public class ParticleController extends ObjectPool<Particle> {
    /**
     * Внутренный класс для создания эффектов
     */
    public class EffectBuilder {
        public void botIsDeadEffect(float x, float y) {//прикольный разноцветный взрыв
            for (int i = 0; i < 16; i++) {
                float angle = 6.9f / 16.0f * i;
                setup(x, y, (float) Math.cos(angle) * 100, (float) Math.sin(angle) * 100,
                    1.5f, 3.0f, 1.5f,
                    0, 1, 0, 1,
                    0, 1, 0.3f, 0.0f);
                angle = 10.5f / 16.0f * i;
                setup(x, y, (float) Math.cos(angle) * 100, (float) Math.sin(angle) * 100,
                    1.5f, 3.0f, 2.8f,
                    1, 1, 0, 1,
                    1, 0.5f, 0, 0.5f);
                angle = 1.0f * i;
                setup(x, y, (float) Math.cos(angle) * 100, (float) Math.sin(angle) * 100,
                    1.5f, 3.0f, 2.8f,
                    1, 0, 0, 1,
                    1, 0, 1, 0.5f);
            }
        }

        /**
         * Эффект взрыва при подборе бонуса
         *
         * @param x    координата х, в которой подобрали бонус
         * @param y    координата у, в которой подобрали бонус
         * @param type тип бонуса
         */
        public void takePowerUpEffect(float x, float y, PowerUp.Type type) {
            switch (type) {
                case MEDKIT:
                    for (int i = 0; i < 16; i++) {
                        float angle = 6.28f / 16.0f * i;
                        setup(x, y, (float) Math.cos(angle) * 100, (float) Math.sin(angle) * 100,
                            0.8f, 3.0f, 2.8f,
                            0, 1, 0, 1,
                            0, 1, 0.3f, 0.5f);
                    }
                    break;
                case MONEY:
                    for (int i = 0; i < 16; i++) {
                        float angle = 6.28f / 16.0f * i;
                        setup(x, y, (float) Math.cos(angle) * 100, (float) Math.sin(angle) * 100,
                            0.8f, 3.0f, 2.8f,
                            1, 1, 0, 1,
                            1, 0.5f, 0, 0.5f);
                    }
                    break;
                case AMOS:
                    for (int i = 0; i < 16; i++) {
                        float angle = 6.28f / 16.0f * i;
                        setup(x, y, (float) Math.cos(angle) * 100, (float) Math.sin(angle) * 100,
                            0.8f, 3.0f, 2.8f,
                            1, 0, 0, 1,
                            1, 0, 1, 0.5f);
                    }
                    break;
            }
        }

        /**
         * Эффект попадания пули в астероид
         *
         * @param bulletPos позиция пули
         * @param bulletVel скорость пули
         */
        public void bulletCollideWithAsteroidEffect(Vector2 bulletPos, Vector2 bulletVel) {
            setup(
                bulletPos.x + MathUtils.random(-4, 4),
                bulletPos.y + MathUtils.random(-4, 4),
                bulletVel.x * -0.3f + MathUtils.random(-30, 30),
                bulletVel.y * -0.3f + MathUtils.random(-30, 30),
                0.2f,
                2.3f, 1.7f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 0.0f
            );
        }

        /**
         * Эффект следа от пути
         *
         * @param type      в зависимости от типа оружия, будет разный цвет
         * @param bulletPos позиция пути
         * @param bulletVel скорость пули
         */
        public void createBulletTraceEffect(WeaponType type, Vector2 bulletPos, Vector2 bulletVel) {
            switch (type) {
                case LASER:
                    setup(
                        bulletPos.x + MathUtils.random(-4, 4), bulletPos.y + MathUtils.random(-4, 4),
                        bulletVel.x * -0.3f + MathUtils.random(-20, 20), bulletVel.y * -0.3f + MathUtils.random(-20, 20),
                        0.05f,
                        1.5f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f
                    );
                    break;

                case GREEN_LASER:
                    setup(
                        bulletPos.x + MathUtils.random(-4, 4), bulletPos.y + MathUtils.random(-4, 4),
                        bulletVel.x * -0.3f + MathUtils.random(-20, 20), bulletVel.y * -0.3f + MathUtils.random(-20, 20),
                        0.05f,
                        1.2f, 2.2f,
                        0.2f, 1.0f, 0.2f, 1.0f,
                        0.3f, 1.0f, 0.3f, 1.0f
                    );
                    break;

                case SUPER_LASER:
                    setup(
                        bulletPos.x + MathUtils.random(-4, 4), bulletPos.y + MathUtils.random(-4, 4),
                        bulletVel.x * -0.3f + MathUtils.random(-20, 20), bulletVel.y * -0.3f + MathUtils.random(-20, 20),
                        0.05f,
                        1.7f, 2.5f,
                        0.3f, 1.0f, 0.2f, 1.0f,
                        1.0f, 1.0f, 0.3f, 1.0f
                    );
                    break;
            }
        }

        public void shipFireTrailEffect(OwnerType ownerType, Vector2 position, Vector2 velocity, float angle) {
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;

            switch (ownerType) {
                case PLAYER:
                    for (int i = 0; i < 2; i++) {
                        setup(
                            bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                            0.5f,
                            1.2f, 0.2f,
                            1.0f, 0.3f, 0.0f, 1.0f,
                            1.0f, 1.0f, 1.0f, 1.0f);
                    }
                    break;
                case BOT:
                    for (int i = 0; i < 2; i++) {
                        setup(
                            bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                            velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                            0.5f,
                            1.2f, 0.2f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 0.0f);
                    }
                    break;
            }
        }
    }

    /**
     * изображение частицы
     */
    private TextureRegion oneParticle;
    /**
     * Объект класса {@link EffectBuilder}
     */
    private EffectBuilder effectBuilder;
    /**
     * размер изображения
     */
    private static final float PARTICLE_SIZE = 16;
    /**
     * Половина размера изображения
     */
    private static final float PARTICLE_HALF_SIZE = 8;

    /**
     * @return объект класса {@link EffectBuilder}
     */
    public EffectBuilder getEffectBuilder() {
        return effectBuilder;
    }

    /**
     * При создании экземпляра класса:
     * 1.Получаем изображение частицы из атласа текстур.
     * 2.Создаем объект класса {@link EffectBuilder}
     */
    public ParticleController() {
        this.oneParticle = getInstance().getAtlas().findRegion("star16");
        this.effectBuilder = new EffectBuilder();
    }

    /**
     * @return создаем новую частицу
     */
    @Override
    protected Particle newObject() {
        return new Particle();
    }

    /**
     * Метод для отрисовки
     * BlendFunction показывает, как надо смешивать цвета заднего фона и накладываемого на него рисунка
     * 1.BlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA) - верхний рисунок полностью перекрывает нижний, нет смешивания цветов
     * 2.Для всех активных частиц вычисляем ее промежуточное состояние (цвет, размер, прозрачность)
     * 3. batch.setColor - устанавливает цвет для батча и дальнейшее рисование будет выполняться перемножением установленного цвета и цвета изображения
     * 4.Рисуем точки
     * 5.batch.setColor(1.0f, 1.0f, 1.0f, 1.0f) - устанавливает белый цвет
     * 6.BlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE) - режим, смешивающий цвета
     * 7.Проходим по активным частицам, вычисляем их промежуточное состояние и рисуем
     * За счет использования второго цикла прорисовки получается более яркое изображение (эффект усиления цвета)
     * После второго цикла прорисовки необходимо переключить режим на GL_ONE_MINUS_SRC_ALPHA и установить цвет батча - белый
     */
    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < activeList.size(); i++) {
            Particle o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t),
                lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(oneParticle, o.getPosition().x - PARTICLE_HALF_SIZE, o.getPosition().y - PARTICLE_HALF_SIZE,
                PARTICLE_HALF_SIZE, PARTICLE_HALF_SIZE, PARTICLE_SIZE, PARTICLE_SIZE, scale, scale, 0);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeList.size(); i++) {
            Particle o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            if (MathUtils.random(0, 300) < 3) {
                scale *= 5;
            }
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t),
                lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(oneParticle, o.getPosition().x - PARTICLE_HALF_SIZE, o.getPosition().y - PARTICLE_HALF_SIZE,
                PARTICLE_HALF_SIZE, PARTICLE_HALF_SIZE, PARTICLE_SIZE, PARTICLE_SIZE, scale, scale, 0);
        }
        batch.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Метод для создания частицы.
     * 1.Получаем объект из листа активных элементов.
     * 2.Активируем его с помощью метода {@link com.star.app.game.entities.Particle#init}
     *
     * @param x       координата по оси х
     * @param y       координата по оси у
     * @param vx      скорость по оси х
     * @param vy      скорость по оси у
     * @param timeMax максимальное время жизни
     * @param size1   начальный размер
     * @param size2   конечный размер
     * @param r1      начальная компонента красного цвета
     * @param g1      начальная компонента зеленого цвета
     * @param b1      начальная компонента синего цвета
     * @param a1      начальная прозрачность
     * @param r2      конечная компонента красного цвета
     * @param g2      конечная компонента зеленого цвета
     * @param b2      конечная компонента синего цвета
     * @param a2      конечная прозрачность
     */
    public void setup(float x, float y, float vx, float vy,
        float timeMax, float size1, float size2,
        float r1, float g1, float b1, float a1,
        float r2, float g2, float b2, float a2) {
        Particle item = getActiveElement();
        item.init(x, y, vx, vy, timeMax, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
    }

    /**
     * Метод для вычисления изменения состояния.
     * 1.Проходим по списку активных элементов и вызываем у каждого из них метод update {@link com.star.app.game.entities.Particle#update}
     * 2.Проверяем, не нужно ли перевести частицы из списка активных в список свободных с помощью метода {@link #checkPool()}
     */
    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    /**
     * Метод для вычисления промежуточного состояния
     *
     * @param value1 начальное значение
     * @param value2 конечное значение
     * @param point  промежуточная точка (текущий момент времени жизни)
     * @return текущее состояние параметра частицы
     */
    public float lerp(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }
}
