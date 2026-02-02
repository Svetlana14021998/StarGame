package com.star.app.screen.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.star.app.screen.ScreenManager.ScreenType;

/**
 * Класс для управления ресурсами (Singleton)
 */
public class Assets {
    /**
     * Создание объекта
     */
    private static final Assets ourInstance = new Assets();

    /**
     * Возвращает созданный объект класса
     */

    public static Assets getInstance() {
        return ourInstance;
    }

    /**
     * Ссылка на AssetManager, который позволяет управлять ресурсами
     */
    private AssetManager assetManager;
    /**
     * Ссылка на атлас текстур
     */
    private TextureAtlas textureAtlas;

    /**
     * Возвращает загруженный атлас текстур
     */
    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    /**
     * Возвращает AssetManager, с помощью которого можно управлять ресурсами
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Приватный конструктор, для реализации паттерна Одиночка
     */
    private Assets() {
        assetManager = new AssetManager();
    }

    /**
     * Метод для загрузки нужных ресурсов для конкретного экрана
     */
    public void loadAssets(ScreenType type) {
        createStandardFont(24);
        switch (type) {
            case MENU:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/music.mp3", Music.class);
                createStandardFont(72);
                createStandardFont(18);
                break;
            case GAMEOVER:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/gameover.mp3", Music.class);
                assetManager.load("audio/gameoversound.mp3", Sound.class);
                createStandardFont(72);
                createStandardFont(48);
                break;
            case GAME:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/mortal.mp3", Music.class);
                assetManager.load("audio/StarWars.mp3", Music.class);
                assetManager.load("audio/shoot.mp3", Sound.class);
                assetManager.load("audio/explosion.mp3", Sound.class);
                assetManager.load("audio/money.mp3", Sound.class);
                assetManager.load("audio/nextlevel.mp3", Sound.class);
                createStandardFont(32);
                createStandardFont(72);
                break;
            case WIN:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/gameover.mp3", Music.class);
                createStandardFont(72);
                createStandardFont(48);
            case HELP:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/gameover.mp3", Music.class);
                createStandardFont(32);
                createStandardFont(48);
            case RESULT:
                assetManager.load("images/game.pack", TextureAtlas.class);
                assetManager.load("audio/gameover.mp3", Music.class);
                createStandardFont(32);
                createStandardFont(48);
        }
    }

    /**
     * Метод для создания шрифта нужного размера
     */
    private void createStandardFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "fonts/Roboto-Medium.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.shadowOffsetX = 1;
        fontParameter.fontParameters.shadowOffsetY = 1;
        fontParameter.fontParameters.shadowColor = Color.DARK_GRAY;
        assetManager.load("fonts/font" + size + ".ttf", BitmapFont.class, fontParameter);
    }

    /**
     * Получение ссылки на атлас текстур
     */
    public void makeLinks() {
        textureAtlas = assetManager.get("images/game.pack", TextureAtlas.class);
    }

    /**
     * Очистка ресурсов
     */
    public void clear() {
        assetManager.clear();
    }
}

