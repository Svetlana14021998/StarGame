package com.star.app.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.controllers.GameController;
import com.star.app.screen.utils.Assets;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.*;
import static com.star.app.game.entities.BotHelper.MIN_TIMER;
import static com.star.app.game.enums.OwnerType.PLAYER;
import static com.star.app.game.enums.WeaponType.LASER;
import static com.star.app.screen.ScreenManager.ScreenType.MENU;
import static com.star.app.screen.ScreenManager.getInstance;

/**
 * Класс, описывающий героя (наш корабль)
 */
public class Hero extends Ship {
    /**
     * Скиллы героя:
     * 1.HP_MAX - максимальное количество жизни
     * 2.HP - текущее количество жизни
     * 3.WEAPON - оружие
     * 4.MAGNET - магнитное поле для притягивания PowerUps
     * 5.TIMER - время, через которое можно получить PowerUps от помощника
     * 6.CRITICAL - вероятность нанесения критического урона (урон х3)
     */
    public enum Skill {
        HP_MAX(20), HP(20), WEAPON(100), MAGNET(50), TIMER(30), CRITICAL(50);
        /**
         * Стоимость покупки скилла
         */
        int cost;

        Skill(int cost) {
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }
    }

    /**
     * Максимальный радиус, до которого можно увеличить магнитное поле
     */
    public static final float MAX_SEARCH_RADIUS = 700.0f;
    /**
     * Скорость поворота корабля
     */
    private static final float ROTATION_SPEED = 180.0f;
    /**
     * Счет
     */
    private int score;
    /**
     * Счет для "красивого" отображения с помощью эффекта перебора цифр
     */
    private int scoreView;
    /**
     * Деньги
     */
    private int money;
    /**
     * Объект, с помощью которого будет подготавливаться текст для отображения на экране
     */
    private StringBuilder stringBuilder;
    /**
     * Магазин для покупки улучшений
     */
    private Shop shop;
    /**
     * Помощник, который будет сбрасывать PowerUps
     */
    private BotHelper botHelper;
    /**
     * Вероятность критического удара (урон х3)
     */
    private int critical;
    /**
     * Зона, при нахождении в которой помощник сбрасывает бонус
     */
    private Circle searchArea;

    /**
     * @return вероятность критического удара
     */
    public int getCritical() {
        return critical;
    }

    /**
     * @return время, через которое помощник сбрасывает бонус
     */
    public float getBotTimer() {
        return botHelper.getHelpTimer();
    }

    /**
     * Метод, уменьшающий время до следующей выдачи PowerUps от помощника на 3 секунды
     */
    public void setTimer() {
        botHelper.setHelpTimer(3);
    }

    /**
     * Установка паузы в игре
     */
    public void setPause(boolean pause) {
        gc.setPause(pause);
    }

    /**
     * @return магазин
     */

    public Shop getShop() {
        return shop;
    }

    /**
     * @return деньги
     */

    public int getMoney() {
        return money;
    }

    /**
     * @return счет
     */

    public int getScore() {
        return score;
    }

    /**
     * @return зона, при нахождении в которой помощник сбрасывает бонус
     */

    public Circle getSearchArea() {
        return searchArea;
    }

    /**
     * Метод для увеличения количества набранных очков
     *
     * @param amount добавляемые очки
     */
    public void addScore(int amount) {
        this.score += amount;
    }

    /**
     * Метод, проверяющий достаточно ли у игрока денег для покупки скилла в магазине
     *
     * @param amount количество денег, необходимое для приобретения улучшения
     */
    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    /**
     * Метод для списания денег при покупке
     *
     * @param amount количество списываемых денег
     */
    public void decreaseMoney(int amount) {
        money -= amount;
    }

    /**
     * Создание героя
     * Создается объект в позиции (х,у) с заданным максимальным уровнем жизни
     * Получаем из атласа текстур изображение для героя
     * Сохраняем размеры изображения по осям х и у
     * Указываем мощность двигателя
     * Начальные монеты героя
     * Создаем оружия
     * Вероятность крит удара - 5%
     * Тип корабля - игрок
     * Добавляем зону поражения и зону поиска
     *
     * @param gc        ссылка на {@link GameController}
     * @param botHelper ссылка на {@link BotHelper}
     */
    public Hero(GameController gc, BotHelper botHelper) {
        super(gc, 100, 640, 360);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.sizeX = texture.getRegionWidth();
        this.sizeY = texture.getRegionHeight();
        this.enginePower = 500.0f;
        this.money = 300;
        this.shop = new Shop(this);
        this.stringBuilder = new StringBuilder();
        this.weaponType = LASER;
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
        this.botHelper = botHelper;
        this.critical = 5;
        this.ownerType = PLAYER;
        this.hitArea = new Circle(position, 0.9f * (sizeX + sizeY) / 4.0f);
        this.searchArea = new Circle(position, 100);
    }

    /**
     * Метод для отрисовки информации о характеристиках героя:
     * 1.Набранные очки
     * 2.Уровень жизни/макисмальный уровень жизни
     * 3.Количество денег
     * 4.Количество пуль
     * 5.Радиус магнитного поля
     * 6.Время, необходимое для получения PowerUps от помощника
     * 7.Вероятность критического удара
     */
    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(scoreView).append("\n");
        stringBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        stringBuilder.append("MONEY: ").append(money).append("\n");
        stringBuilder.append("BULLETS: ").append(currentWeapon.getCurBullets()).append(" / ")
            .append(currentWeapon.getMaxBullets()).append("\n");
        stringBuilder.append("MAGNETIC: ").append((int) searchArea.radius).append("\n");
        stringBuilder.append("TIMER: ").append((int) botHelper.getHelpTimer()).append("\n");
        stringBuilder.append("CRITICAL: ").append(critical).append("%\n");
        font.draw(batch, stringBuilder, 20, 700);
    }

    /**
     * Метод для улучшения характеристики после подбора PowerUp
     *
     * @param p - полученный PowerUp
     */
    public void consume(PowerUp p) {
        switch (p.getType()) {
            case MEDKIT:
                int oldHP = hp;
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
                stringBuilder.clear();
                stringBuilder.append("HP +").append(hp - oldHP);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y,
                    stringBuilder, Color.GREEN);
                break;
            case MONEY:
                stringBuilder.clear();
                stringBuilder.append("MONEY +").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y,
                    stringBuilder, Color.YELLOW);
                money += p.getPower();
                break;
            case AMOS:
                int count = currentWeapon.addBullets(p.getPower());
                stringBuilder.clear();
                stringBuilder.append("AMMOS +")
                    .append(count);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y,
                    stringBuilder, Color.ORANGE);
                break;
        }
    }

    /**
     * Метод для улучшения скилла после покупки в магазине
     *
     * @param skill - улучшенный скилл
     */
    public boolean upgrade(Skill skill) {
        switch (skill) {
            case TIMER:
                if (getBotTimer() > MIN_TIMER) {
                    setTimer();
                }
                return true;
            case HP_MAX:
                hpMax += 10;
                return true;
            case HP:
                if (hp < hpMax) {
                    hp += 10;
                    if (hp > hpMax) {
                        hp = hpMax;
                    }
                    return true;
                }
            case WEAPON:
                if (weaponNum < weapons.length - 1) {
                    weaponNum++;
                    currentWeapon = weapons[weaponNum];
                    return true;
                }
            case MAGNET:
                if (searchArea.radius < MAX_SEARCH_RADIUS) {
                    searchArea.radius += 10;
                    if (searchArea.radius > MAX_SEARCH_RADIUS) {
                        searchArea.radius = MAX_SEARCH_RADIUS;
                    }
                    return true;
                }

            case CRITICAL:
                if (critical < 100) {
                    critical += 5;
                    return true;
                }
        }
        return false;
    }

    /**
     * Метод для вычисления изменений.
     * 1.Вычисление количества очков {@link #updateScore}.
     * 2.Движение корабля {@link #ruleShipMovement}.
     * 3.Если нажата клавиша ESCAPE, то выходим из игры в экран меню.
     * 4.Если нажата клавиша U, то открывается магазин.
     * 5.Если скорость корабля достигает значения 50.0f, то добавляется эффект {@link  com.star.app.game.controllers.ParticleController.EffectBuilder#shipFireTrailEffect}
     */
    public void update(float dt) {
        super.update(dt);
        searchArea.setPosition(position);
        updateScore(dt);

        ruleShipMovement(dt);
        if (input.isKeyJustPressed(ESCAPE)) {
            getInstance().changeScreen(MENU);
        }
        if (input.isKeyJustPressed(U)) {
            shop.setVisible(true);
            setPause(true);
        }

        if (velocity.len() > 50.0f) {
            gc.getParticleController().getEffectBuilder().shipFireTrailEffect(ownerType, position, velocity, angle);
        }
    }

    /**
     * Метод для управления движением кораблем
     */
    private void ruleShipMovement(float dt) {
        if (input.isKeyPressed(SPACE)) {
            tryToFire();
        }
        if (input.isKeyPressed(A) || input.isKeyPressed(LEFT)) {
            rotate(ROTATION_SPEED, dt);
        }
        if (input.isKeyPressed(D) || input.isKeyPressed(RIGHT)) {
            rotate(-ROTATION_SPEED, dt);
        }
        if (input.isKeyPressed(W) || input.isKeyPressed(UP)) {
            accelerate(dt);
        }
        if (input.isKeyPressed(S) || input.isKeyPressed(DOWN)) {
            brake(dt);
        }
    }

    /**
     * Метод для отображения изменения счета
     */
    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    /**
     * Метод для инициализации массива оружий, доступных герою
     */
    private void createWeapons() {
        weapons = new Weapon[]{
            new Weapon(
                gc, this, weaponType, 0.2f, 1, 600, 300,
                new Vector3[]{
                    new Vector3(28, 90, 0),
                    new Vector3(28, -90, 0)
                }),
            new Weapon(
                gc, this, weaponType, 0.2f, 1, 600, 300,
                new Vector3[]{
                    new Vector3(28, 0, 0),
                    new Vector3(28, 90, 20),
                    new Vector3(28, -90, -20)
                }),
            new Weapon(
                gc, this, weaponType, 0.2f, 1, 600, 500,
                new Vector3[]{
                    new Vector3(28, 0, 0),
                    new Vector3(28, 90, 10),
                    new Vector3(28, 90, 20),
                    new Vector3(28, -90, -10),
                    new Vector3(28, -90, -20)
                }),
            new Weapon(
                gc, this, weaponType, 0.1f, 2, 600, 1000,
                new Vector3[]{
                    new Vector3(28, 0, 0),
                    new Vector3(28, 90, 16),
                    new Vector3(28, -90, -16)
                }),
        };
    }
}
