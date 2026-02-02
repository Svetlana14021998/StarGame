package com.star.app.game.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс, описывающий реализацию шаблона проектирования Object Pool
 *
 * @param <T> тип объекта, реализуещего интерфейс {@link Poolable}
 */
public abstract class ObjectPool<T extends Poolable> {
    /**
     * Лист активных элементов заданного типа T, который имплементирует интерфейс {@link Poolable}
     */
    protected List<T> activeList;
    /**
     * Лист свободных (неактивных) элементов заданного типа T, который имплементирует интерфейс {@link Poolable}
     */
    protected List<T> freeList;

    /**
     * Возвращает лист активных элементов
     */
    public List<T> getActiveList() {
        return activeList;
    }

    /**
     * Метод для создания объектов заданного типа
     *
     * @return T  объект типа Т
     */
    protected abstract T newObject();

    /**
     * Метод, который перекидывает объект из списка активных элементов в список свободных элементов
     */
    public void free(int index) {
        freeList.add(activeList.remove(index));
    }

    /**
     * При пулла объектов создаются пустые списки свободных и активных элементов
     */
    public ObjectPool() {
        this.activeList = new ArrayList<T>();
        this.freeList = new ArrayList<T>();
    }

    /**
     * Метод для получения активного объекта:
     * 1.Обращаемся к списку свободных элементов.
     * 2.Если список пуст, создаем новый объект.
     * 3.Получаем последний из списка элемент.
     * 4.Добавляем его в список активных элементов и удаляем из списка свободных.
     *
     * @return активный элемент типа  T
     */
    public T getActiveElement() {
        if (freeList.size() == 0) {
            freeList.add(newObject());
        }
        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);
        return temp;
    }

    /**
     * Метод для проверки активных элементов:
     * 1.Проходим по списку активных элементов с конца
     * 2.Если есть объект, который отработал свое (признак active == false),то добавляем его с список свободных эл-в удаляем его из списка активных эл-в {@link #free(int)}
     */
    public void checkPool() {
        for (int i = activeList.size() - 1; i >= 0; i--) {
            if (!activeList.get(i).isActive()) {
                free(i);
            }
        }
    }
}
