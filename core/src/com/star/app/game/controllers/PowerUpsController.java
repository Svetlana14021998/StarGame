package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.entities.PowerUp;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Класс-контроллер для создания бонусов
 */
public class PowerUpsController extends ObjectPool<PowerUp> {
    /**
     * Массив текстур для изображения бонусов
     */
    private TextureRegion[][] textures;
    /**
     * размер изображения
     */
    private final int SIZE = 60;
    /**
     * Сколько бонусов начисляется
     */
    private final int POWER = 30;

    /**
     * Создание Бонуса
     */
    @Override
    protected PowerUp newObject() {
        return new PowerUp();
    }

    /**
     * Инициализируем массив текстур:
     * Добавлено 4 бонуса, для каждого из которых есть 6 изображения.
     * Размер каждого изображения 60х60. Формируем массив где i- вид бонуса, j - его изображения
     */
    public PowerUpsController() {
        this.textures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("powerups"))
            .split(SIZE, SIZE);
    }

    /**
     * Метод для рисования:
     * 1.Проходим по всем активным бонусам.
     * 2.Определяем индексы изображения, которое нужно нарисовать:
     * - i-ый индекс определяемся с помощью типа бонуса
     * - j-ый индекс вычисляется как остаток от деления времени жизни, разделенного на 0.1, на длину массива изображений
     * 3.Рисует полученное изображение
     */
    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            PowerUp p = activeList.get(i);
            int frameIndex = (int) (p.getTime() / 0.1f) % textures[p.getType().index].length;
            batch.draw(textures[p.getType().index][frameIndex], p.getPosition().x - SIZE / 2, p.getPosition().y - SIZE / 2);
        }
    }

    /**
     * Метод создания бонуса (с какой-то вероятностью).
     * Если произвольное число меньше заданной вероятности, то создается бонус в указанных координатах
     *
     * @param x           координата по оси х
     * @param y           координата по оси у
     * @param probability вероятность выпадения бонуса
     */
    public void setup(float x, float y, float probability) {
        if (random() <= probability) {
            getActiveElement().activate(PowerUp.Type.values()[random(0, 2)], x, y, POWER);
        }
    }

    /**
     * Метод для создания бонуса
     *
     * @param x координата по оси х
     * @param y координата по оси у
     */
    public void create(float x, float y) {
        getActiveElement().activate(PowerUp.Type.values()[random(0, 2)], x, y, POWER);
    }

    /**
     * Метод для вычисления изменения состояния объектов.
     * 1.Проходит по всем активным бонусам
     * 2.Вычисляет изменение их состояния
     * 3.Проверяет, не нужно ли перевести элемент из активного списка в свободный {@link ObjectPool#checkPool()}
     */
    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
