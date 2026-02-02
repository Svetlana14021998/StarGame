package com.star.app.game.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.controllers.GameController;
import com.star.app.game.interfacies.Pushable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

import static com.star.app.game.enums.OwnerType.BOT;
import static com.star.app.game.enums.WeaponType.SUPER_LASER;

/**
 * Класс для описания главного противника
 */
public class BotBoss extends Ship implements Pushable {
    /**
     * Вектор для промежуточных вычисления
     */
    private Vector2 tempVector;
    /**
     * Для работы с текстом
     */
    private StringBuilder strBuilder;
    /**
     * Веятность выпадения бонуса
     */
    private int probably;

    /**
     * @return вероятность выпадения бонуса
     */
    public int getProbably() {
        return probably;
    }

    /**
     * Создание объекта
     * Создается с указанным количеством жизни в заданных координатах
     * Устанавливается зона поражения
     * Получаем из атласа текстур изображение
     * Сохраняем его размеры по осям х и у
     * Устанавливаем мощность двигателя
     * Тип корабля - бот
     * Создаем оружие
     *
     * @param gc ссылка на {@link GameController}
     */
    public BotBoss(GameController gc) {
        super(gc, 100,
            MathUtils.random(ScreenManager.SCREEN_WIDTH - 200, ScreenManager.SCREEN_WIDTH - 100),
            MathUtils.random(100, ScreenManager.SCREEN_HEIGHT - 100));
        this.hitArea = new Circle(position, 115);
        this.texture = Assets.getInstance().getAtlas().findRegion("sokol");
        this.sizeX = texture.getRegionWidth();
        this.sizeY = texture.getRegionHeight();
        this.enginePower = 100.0f;
        this.ownerType = BOT;
        this.tempVector = new Vector2();
        this.probably = 7;
        this.weaponType = SUPER_LASER;
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
        this.strBuilder = new StringBuilder();
    }

    /**
     * Метод для вычисления изменения состояния
     * Бот поворачивается и движется в сторону героя
     * Если между ботом и героем расстояние меньше 200, то бот ускоряется
     * Если между ботом и героем расстояние меньше 300, то бот начинает стрелять
     */
    public void update(float dt) {
        super.update(dt);
        tempVector.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVector.angleDeg();
        if (gc.getHero().getPosition().dst(position) > 200) {
            accelerate(dt);
        }
        if (gc.getHero().getPosition().dst(position) < 300) {
            tryToFire();
        }
    }

    /**
     * Метод для рисования
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - sizeX / 2.0f, position.y - sizeY / 2.0f, sizeX / 2.0f, sizeY / 2.0f,
            sizeX, sizeY, 1, 1, angle - 180);
    }

    /**
     * @param batch батч для рисования
     * @param font  шрифт
     */
    public void renderHP(SpriteBatch batch, BitmapFont font) {
        strBuilder.setLength(0);
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        font.draw(batch, strBuilder, 1020, 700);
    }

    /**
     * Создание оружия
     */
    private void createWeapons() {
        weapons = new Weapon[]{
            new Weapon(
                gc, this, weaponType, 0.4f, 3, 700, 30000,
                new Vector3[]{
                    new Vector3(125, 0, 0),
                    new Vector3(100, 100, 0),
                    new Vector3(100, -100, 0),
                }),
            new Weapon(
                gc, this, weaponType, 0.4f, 3, 750, 30000,
                new Vector3[]{
                    new Vector3(125, 0, 0),
                    new Vector3(100, 50, 0),
                    new Vector3(100, 100, 0),
                    new Vector3(100, -100, 0),
                    new Vector3(100, -50, 0),
                }),
        };
    }

    /**
     * Метод для улучшения оружия
     */
    public void upgradeWeapon() {
        if (weaponNum < weapons.length - 1) {
            weaponNum++;
            currentWeapon = weapons[weaponNum];
        }
    }
}
