package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.InfoText;
import com.star.app.game.helpers.ObjectPool;

import static com.badlogic.gdx.graphics.Color.WHITE;

/**
 * Контроллер для появляющегося текста
 */
public class InfoController extends ObjectPool<InfoText> {
    /**
     * Создание нового объекта
     *
     * @return {@link InfoText}
     */
    @Override
    protected InfoText newObject() {
        return new InfoText();
    }

    /**
     * Активация элемента
     * Появляется элемент в координатах (х,у), отображается указанный текст заданного цвета
     *
     * @param x     координата х
     * @param y     координата у
     * @param text  текст для отображения
     * @param color цвет текста
     */
    public void setup(float x, float y, String text, Color color) {
        InfoText infoText = getActiveElement();
        infoText.setup(x, y, text, color);
    }

    /**
     * Активация элемента
     * Появляется элемент в координатах (х,у), отображается указанный текст заданного цвета
     *
     * @param x     координата х
     * @param y     координата у
     * @param text  текст для отображения
     * @param color цвет текста
     */
    public void setup(float x, float y, StringBuilder text, Color color) {
        InfoText infoText = getActiveElement();
        infoText.setup(x, y, text.toString(), color);
    }

    /**
     * Метод для рисования:
     * 1.Проходит по всем активным элементам
     * 2.Устанавливаем цвет
     * 3.Выводит текст заданным шрифтом указанного цвета
     * После прохождения по всему массиву активных элементов цвет шрифта меняется на белый
     *
     * @param batch батч, на котором происходит отрисовка
     * @param font  шрифт для вывода текста
     */
    public void render(SpriteBatch batch, BitmapFont font) {
        for (int i = 0; i < activeList.size(); i++) {
            InfoText infoText = activeList.get(i);
            font.setColor(infoText.getColor());
            font.draw(batch, infoText.getText(), infoText.getPosition().x, infoText.getPosition().y);
        }
        font.setColor(WHITE);
    }

    /**
     * Метод для вычисления измнения состояния объектов
     * Вызывает метод {@link com.star.app.game.entities.InfoText#update} для всех активных элементов
     * После чего выполнятеся проверка, не нужно ли перевести какаие-то объекты из списка активных в список свободных {@link #checkPool()}
     */
    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
