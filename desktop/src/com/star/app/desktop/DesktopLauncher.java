package com.star.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.star.app.StarGame;

/**
 * Класс - точка входа в приложение (запуск игры)
 */
public class DesktopLauncher {

    /**
     * Настройка конфигураций и запуск приложения
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new StarGame(), config);
    }
}
