package com.star.app.game.interfacies;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Интерфейс для отталкивания объекта
 */
public interface Pushable {
    /**
     * @return позиция объекта
     */
    Vector2 getPosition();

    /**
     * @return зона поражения объекта
     */
    Circle getHitArea();
}
