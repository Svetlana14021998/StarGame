package com.star.app.game.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.controllers.GameController;

import static com.star.app.game.enums.OwnerType.BOT;
import static com.star.app.game.enums.WeaponType.GREEN_LASER;
import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс, описывающий вражеский корабль
 */
public class Bot extends Ship {
    /**
     * вектор для промежуточных вычислений
     */
    private Vector2 tempVector;
    /**
     * Для работы с текстом
     */
    private StringBuilder strBuilder;

    /**
     * Создаем бота с заданным уровнем жизни в указанных координатах х и у
     * Получаем изображение из атласа текстур
     * Сохраняем размер изображения по осям х и у
     * Устанавливаем мощность двигаетеля
     * Тип корабля - бот
     * Создаем оружие
     * Устанавливаем зону поражения
     * @param gc ссылка на {@link GameController}
     */
    public Bot(GameController gc) {
        super(gc, 50,
            MathUtils.random(SCREEN_WIDTH - 200, SCREEN_WIDTH - 100),
            MathUtils.random(100, SCREEN_HEIGHT - 100));
        this.texture = getInstance().getAtlas().findRegion("ship");
        this.sizeX = texture.getRegionWidth();
        this.sizeY = texture.getRegionHeight();
        this.enginePower = 200.0f;
        this.ownerType = BOT;
        this.tempVector = new Vector2();
        this.weaponType = GREEN_LASER;
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
        this.strBuilder = new StringBuilder();
        this.hitArea = new Circle(position, 0.9f*(sizeX + sizeY) / 4.0f);
    }

    /**
     * Метод для отображения уровня жизни бота
     * @param batch батч для рисоания
     * @param font шрифт
     */
    public void renderHP(SpriteBatch batch, BitmapFont font) {
        strBuilder.setLength(0);
        strBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        font.draw(batch, strBuilder, 1120, 700);
    }

    /**
     * Метод для вычисления изменения состояния
     * Бот поворачивается и движется в сторону героя
     * Если между ботом и героем расстояние меньше 200, то бот ускоряется
     * Если между ботом и героем расстояние меньше 300, то бот начинает стрелять
     * Если скорость бота больше 50, то добавляется эффект {@link  com.star.app.game.controllers.ParticleController.EffectBuilder#shipFireTrailEffect}
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
        if (velocity.len() > 50.0f) {
            gc.getParticleController().getEffectBuilder().shipFireTrailEffect(ownerType, position, velocity, angle);
        }
    }

    /**
     * Создание оружия
     */
    private void createWeapons() {
        weapons = new Weapon[]{
            new Weapon(
                gc, this, weaponType, 0.3f, 1, 600, 30000,
                new Vector3[]{
                    new Vector3(28, 90, 0),
                    new Vector3(28, -90, 0)
                }),
        };
    }
}
