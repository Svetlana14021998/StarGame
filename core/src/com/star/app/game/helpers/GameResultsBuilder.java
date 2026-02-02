package com.star.app.game.helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.Hero;

import java.util.*;

import static com.badlogic.gdx.Gdx.app;

/**
 * Класс работы с результатами игр
 * Функицолнал:
 * 1.Сохранение
 * 2.Обработка
 * 3.Получение мз памяти
 * 4.Удаление
 * 5.Формирование таблицы результатов
 */
public class GameResultsBuilder {
    /**
     * Ссылка на текущего героя
     */
    private Hero hero;
    /**
     * имя которое пользователь должен ввести в случае победы
     */
    private String name;
    /**
     * Для формирования текста
     */
    private StringBuilder stringBuilder;
    /**
     * Сохраненные в памяти результаты игр
     */
    public static Preferences preferences = app.getPreferences("MyPreferences");
    /**
     * Максимальное количество результатов, которые необходимо хранить
     */
    private final int MAX_RESULTS = 10;

    /**
     * Метод для присвоения переменной hero значения
     *
     * @param hero
     */
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    /**
     * Слушатель, который отработает после того, как пользователь введет свое имя
     */
    public Input.TextInputListener listener = new Input.TextInputListener() {
        /**
         * Данный метод отработает после того, как пользователь введет имя и нажмет "ОК".
         * Результат игры пользователя (счет) будет сохранен под введенным именем
         * @param text  введенное пользователем имя (будет обрезано до 10 символов)
         */
        @Override
        public void input(String text) {
            name = text;
            if (name.length() > 10) {
                name = name.substring(0, 10);
            }
            saveResult();
        }

        /**
         * Данный метод отработает, если пользователь нажмет "Canceled" при вводе имени.
         * В этом случае результат игры сохранен не будет
         */
        @Override
        public void canceled() {

        }
    };

    /**
     * Метод, позволяющий получить Слушателя, который будет отрабатывать при нажатии пользователем на
     * кнопки "OK" или "Canceled" при вводе имени
     */
    public Input.TextInputListener getListener() {
        return listener;
    }

    public GameResultsBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Данный метод сохраняет результат пользователя
     */
    public void saveResult() {
        preferences.putInteger(name, hero.getScore());
        preferences.flush();
    }

    /**
     * Метод, преобразующий результаты из формата Preferences в формат Ключ (Строка) - значение (Число)
     *
     * @return спикок формата ключ (Строка) - значение (Число)
     */
    public static Map<String, Integer> convertPreferencesToMapResult() {
        Map<String, Integer> result = new LinkedHashMap<>();
        Map<String, ?> preferencesMap = preferences.get();
        Set<String> keySet = preferencesMap.keySet();
        for (String key : keySet) {
            String value = (String) preferencesMap.get(key);
            try {
                int currentValue = Integer.parseInt(value);
                result.put(key, currentValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Метод, сортирующий результаты пользователей в порядке уменьшения набранных очков
     *
     * @return спикок формата ключ (Строка) - значение (Число)
     */
    public static Map<String, Integer> getSortResults() {
        Map<String, Integer> inputMap = convertPreferencesToMapResult();
        Map<String, Integer> map = new TreeMap<>(inputMap);
        Set<String> keys = map.keySet();

        List<Integer> points = new ArrayList<>(map.values());
        points.sort(Collections.reverseOrder());

        Map<String, Integer> sortedMap = new LinkedHashMap<>();

        for (Integer integer : points) {
            for (String key : keys) {
                if (map.get(key) == integer) {
                    if (!sortedMap.containsKey(key)) {
                        sortedMap.put(key, integer);
                    }
                }
            }
        }
        deleteResults(sortedMap);
        return sortedMap;
    }

    /**
     * Метод для удаления лишних результатов (хранится в памяти и выводится 10 лучших результатов)
     *
     * @param results сохраненные результаты пользователей
     */
    public static void deleteResults(Map<String, Integer> results) {
        if (results.keySet().size() > 10) {
            Object[] objects = results.keySet().toArray();
            for (int i = 10; i < objects.length; i++) {
                preferences.remove((String) objects[i]);
            }
        }
        preferences.flush();
    }

    /**
     * Метод для очистки таблицы результатов
     */
    public static void clearResult() {
        preferences.clear();
        preferences.flush();
    }

    /**
     * Метод для создания таблицы результатов
     * Методы {@link #buildEmptyResultTable},{@link #buildFullResultTable},{@link #buildResultTable}
     */
    public StringBuilder createResultTable() {
        stringBuilder.clear();
        String space = " ";
        stringBuilder.append(space.repeat(5)).append("NAME:").append(space.repeat(28)).append("SCORE:").append("\n");
        Map<String, Integer> results = getSortResults();
        int resultSize = results.keySet().size();
        int size = results.keySet().size() < MAX_RESULTS ? resultSize : MAX_RESULTS;

        switch (size) {
            case 0:
                buildEmptyResultTable();
                break;
            case MAX_RESULTS:
                buildFullResultTable(results);
                break;
            default:
                buildResultTable(results, size);
                break;
        }
        return stringBuilder;
    }

    /**
     * Метод для создания таблицы результатов, если нет записей
     */
    private void buildEmptyResultTable() {
        for (int i = 0; i < MAX_RESULTS; i++) {
            stringBuilder.append(i + 1 + ".").append("\n");
        }
    }

    /**
     * Метод для создания таблицы результатов, если есть 10 записей
     */
    private void buildFullResultTable(Map<String, Integer> results) {
        Object[] keys = results.keySet().toArray();
        for (int i = 0; i < MAX_RESULTS; i++) {
            stringBuilder.append(i + 1 + ". ");
            if (i != MAX_RESULTS - 1) {
                stringBuilder.append(" ");
            }
            buildString((String) keys[i], results.get(keys[i]));
        }
    }

    /**
     * Метод для создания таблицы результатов, если есть меньше 10 записей
     */
    private void buildResultTable(Map<String, Integer> results, int count) {
        Object[] keys = results.keySet().toArray();
        for (int i = 0; i < count; i++) {

            stringBuilder.append(i + 1 + ". ");
            if (i != MAX_RESULTS - 1) {
                stringBuilder.append(" ");
            }
            buildString((String) keys[i], results.get(keys[i]));
        }
        for (int i = count; i < MAX_RESULTS; i++) {
            stringBuilder.append(i + 1 + ".").append("\n");
        }
    }

    /**
     * Формирует строку результата
     *
     * @param name  имя героя
     * @param score счет героя
     */
    private void buildString(String name, Integer score) {
        String s = "_";
        stringBuilder.append(name);
        if (name.length() < 10) {
            stringBuilder.append(s.repeat(10 - name.length()));
        }
        stringBuilder.append(s.repeat(10)).append(score).append("\n");
    }
}
