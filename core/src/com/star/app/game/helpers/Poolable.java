package com.star.app.game.helpers;

/**
 * Интерфейс, который будет реализовываться пулл-объектами
 */
public interface Poolable {
    /**
     * Проверка активности элемента
     */
    boolean isActive();
}
