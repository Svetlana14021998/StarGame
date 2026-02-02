package com.star.app.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.screen.utils.Assets;

import static com.star.app.game.entities.BotHelper.MIN_TIMER;
import static com.star.app.game.entities.Hero.MAX_SEARCH_RADIUS;
import static com.star.app.game.entities.Hero.Skill.*;

/**
 * Магазин
 * Расширяет класс {@link Group}, то есть является группой (игровая панель)
 */
public class Shop extends Group {
    /**
     * герой
     */
    private Hero hero;
    /**
     * Шрифт размера 24
     */
    private BitmapFont font24;
    /**
     * Изображение
     */
    private Texture texture;

    /**
     * Создание магазина
     * 1.Создаем шрифт
     * 2.Добавление заднего фона
     * 3.Добавляется Skin
     * 4.Добавляются кнопки покупки товаров
     *
     * @param hero герой
     */
    public Shop(final Hero hero) {
        this.hero = hero;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.texture = new Texture("images/sky.png");

        Image image = new Image(texture);
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        createCloseBtn(textButtonStyle);
        createCriticalBtn(textButtonStyle);
        createHelperTimerBtn(textButtonStyle);
        createHpMaxBtn(textButtonStyle);
        createHpBtn(textButtonStyle);
        createWeaponBtn(textButtonStyle);
        createMagnetBtn(textButtonStyle);

        this.setPosition(720, 20);
        this.setVisible(false);
        skin.dispose();
    }

    /**
     * Создание кнопки для закрытия окна магазина
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createCloseBtn(TextButtonStyle style) {
        final TextButton btnClose = new TextButton("X", style);
        final Shop thisShop = this;

        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
                hero.setPause(false);
            }
        });

        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(450, 550);
        this.addActor(btnClose);
    }

    /**
     * Создание кнопки для увеличения вероятности критического удара
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createCriticalBtn(TextButtonStyle style) {
        final TextButton btnCritical = new TextButton("  Critical (+5% for 50 coins)  ", style);
        btnCritical.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(CRITICAL.getCost())) {
                    if (hero.upgrade(CRITICAL)) {
                        hero.decreaseMoney(CRITICAL.getCost());
                    }
                }
                if (hero.getCritical() == 100) {
                    btnCritical.setVisible(false);
                }
            }
        });

        btnCritical.setPosition(10, 245);
        this.addActor(btnCritical);
    }

    /**
     * Создание кнопки для уменьшения времени сброса бонуса помощником
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createHelperTimerBtn(TextButtonStyle style) {
        final TextButton btnTimer = new TextButton("  Time Help ( -3sec for 30 coins)  ", style);
        btnTimer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(TIMER.getCost())) {
                    if (hero.getBotTimer() >= 4) {
                        if (hero.upgrade(TIMER)) {
                            hero.decreaseMoney(TIMER.getCost());
                        }
                    }
                    if (hero.getBotTimer() == MIN_TIMER) {
                        btnTimer.setVisible(false);
                    }
                }
            }
        });

        btnTimer.setPosition(10, 415);
        this.addActor(btnTimer);
    }

    /**
     * Создание кнопки для увеличения максимального уровня жизни
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createHpMaxBtn(TextButtonStyle style) {
        final TextButton btnHpMax = new TextButton("  HP MAX (10 point for 20 coins)  ", style);

        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP_MAX.getCost())) {
                    if (hero.upgrade(Hero.Skill.HP_MAX)) {
                        hero.decreaseMoney(Hero.Skill.HP_MAX.getCost());
                    }
                }
            }
        });
        btnHpMax.setPosition(10, 500);
        this.addActor(btnHpMax);
    }

    /**
     * Создание кнопки для увеличения текущего уровня жизни
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createHpBtn(TextButtonStyle style) {
        final TextButton btnHp = new TextButton("  HP (10 points for 20 coins)  ", style);
        btnHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(HP.getCost())) {
                    if (hero.upgrade(HP)) {
                        hero.decreaseMoney(HP.getCost());
                    }
                }
            }
        });
        btnHp.setPosition(10, 330);
        this.addActor(btnHp);
    }

    /**
     * Создание кнопки для прокачки оружия
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createWeaponBtn(TextButtonStyle style) {
        final TextButton btnWp = new TextButton("  Weapon (next type for 100 coins)  ", style);
        btnWp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(WEAPON.getCost())) {
                    if (hero.upgrade(WEAPON)) {
                        hero.decreaseMoney(WEAPON.getCost());
                    }
                }
                if (hero.getWeaponNum() == hero.getWeapons().length - 1) {
                    btnWp.setVisible(false);
                }
            }
        });
        btnWp.setPosition(10, 75);
        this.addActor(btnWp);
    }

    /**
     * Создание кнопки для расширения зоны притяжения бонусов
     * Добавление обработчика события при нажатии кнопки
     *
     * @param style стиль кнопки
     */
    private void createMagnetBtn(TextButtonStyle style) {
        final TextButton btnMagnet = new TextButton("  Magnet (radius + 10 for 50 coins)  ", style);
        btnMagnet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(MAGNET.getCost())) {
                    if (hero.upgrade(MAGNET)) {
                        hero.decreaseMoney(MAGNET.getCost());
                    }
                }
                if (hero.getSearchArea().radius == MAX_SEARCH_RADIUS) {
                    btnMagnet.setVisible(false);
                }
            }
        });
        btnMagnet.setPosition(10, 160);
        this.addActor(btnMagnet);
    }
}

