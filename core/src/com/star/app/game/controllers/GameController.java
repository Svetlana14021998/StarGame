package com.star.app.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.entities.*;
import com.star.app.game.interfacies.Pushable;
import com.star.app.screen.MenuScreen;
import com.star.app.screen.ScreenManager;

import static com.badlogic.gdx.graphics.Color.CYAN;
import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.star.app.game.entities.Asteroid.RANDOM_ASTEROID_SPEED;
import static com.star.app.game.enums.OwnerType.BOT;
import static com.star.app.game.enums.OwnerType.PLAYER;
import static com.star.app.screen.MenuScreen.isMusicPlay;
import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static com.star.app.screen.ScreenManager.ScreenType.GAMEOVER;
import static com.star.app.screen.utils.Assets.getInstance;

/**
 * Класс, отвечающий за игровую логику.
 * Является связующим звеном между другими игровыми классами, т.к. имеет ссылки на все игровые сущности
 */
public class GameController {
    /**
     * Ссылка на {@link Background}
     */
    private Background background;
    /**
     * Ссылка на {@link AsteroidController}
     */
    private AsteroidController asteroidController;
    /**
     * Ссылка на {@link BotHelper}
     */
    private BotHelper botHelper;
    /**
     * Ссылка на {@link BulletController}
     */
    private BulletController bulletController;
    /**
     * Ссылка на {@link ParticleController}
     */
    private ParticleController particleController;
    /**
     * Ссылка на {@link PowerUpsController}
     */
    private PowerUpsController powerUpsController;
    /**
     * Ссылка на {@link InfoController}
     */
    private InfoController infoController;
    /**
     * Ссылка на {@link Hero}
     */
    private Hero hero;
    /**
     * Ссылка на {@link Bot}
     */
    private Bot bot;
    /**
     * Ссылка на {@link BotBoss}
     */
    private BotBoss botBoss;
    /**
     * Вспомогательный вектор, который будет использоваться для различных вычислений
     */
    private Vector2 tmpVec;
    /**
     * Компонент, отвечающий за размещение и управление игровыми объектами (кнопки, переключатели и т.д.)
     */
    private Stage stage;
    /**
     * стоит ли игра на паузе
     */
    private boolean pause;
    /**
     * текущий уровень
     */
    private int level;
    /**
     * Финальный уровень с боссом
     */
    private final int BOSS_LEVEL = 5;
    /**
     * Таймер перехода на новый уровень (обнуляется при переходе на новый уровень).
     * Пока таймер не достигнет определенного значения, показывается сообщение о новом уровне
     */
    private float roundTimer;
    /**
     * Таймер для получения бонуса у помощника.
     * Когда таймер достигает определенного значения, помощник может выдать бонус
     * Потом таймер обнуляется
     */
    private float helpTimer;
    /**
     * Музыка
     */
    private Music music;
    /**
     * Музыка для последнего уровня
     */
    private Music lastLevelMusic;
    /**
     * Звук для подбора бонуса
     */
    private Sound powerUpSound;
    /**
     * Звук перехода на следующий уровень
     */
    private Sound nextLevelSound;
    /**
     * Для работы с текстом
     */
    private StringBuilder stringBuilder;

    /**
     *
     */
    public int getBossLevel() {
        return BOSS_LEVEL;
    }

    /**
     *
     */
    public boolean isBossLevel() {
        return level == BOSS_LEVEL;
    }

    /**
     *
     */
    public BotBoss getBotBoss() {
        return botBoss;
    }

    public Bot getBot() {
        return bot;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public BotHelper getBotHelper() {
        return botHelper;
    }

    public float getRoundTimer() {
        return roundTimer;
    }

    public int getLevel() {
        return level;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    /**
     * Создание объекта:
     * 1.Инициализируются все классы и контроллеры, указанные в полях класса
     * 2.Добавляется Stage
     * 3.Gdx.input.setInputProcessor(stage); stage перехватывает все события
     * 4.Задается текущий уровень -1
     * 5.Загружаются музыка и звуки
     * 6.Включается музыка для уровня
     * 7.Добавляются астероиды
     */
    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.botHelper = new BotHelper();
        this.hero = new Hero(this, botHelper);
        this.bot = new Bot(this);
        this.botBoss = new BotBoss(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController();
        this.infoController = new InfoController();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.stringBuilder = new StringBuilder();
        this.level = 1;
        this.tmpVec = new Vector2(0.0f, 0.0f);
        this.roundTimer = 0.0f;
        this.helpTimer = 0.0f;
        this.powerUpSound = getInstance().getAssetManager().get("audio/money.mp3");
        this.nextLevelSound = getInstance().getAssetManager().get("audio/nextlevel.mp3");
        this.music = getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);

        if (isMusicPlay) {
            this.music.play();
        }
        this.lastLevelMusic = getInstance().getAssetManager().get("audio/StarWars.mp3");
        createAsteroids();
    }

    /**
     * Метод для создания астероидов
     * В зависимости от уровня создается заданное количество астероидов
     */
    public void createAsteroids() {
        for (int i = 0; i < (Math.min(level, 3)); i++) {
            asteroidController.setup(random(0, SCREEN_WIDTH),
                random(0, SCREEN_HEIGHT),
                random(-RANDOM_ASTEROID_SPEED, RANDOM_ASTEROID_SPEED),
                random(-RANDOM_ASTEROID_SPEED, RANDOM_ASTEROID_SPEED),
                1.0f);
        }
    }

    /**
     * Метод для вычисления изменения состояния объектов:
     * Если игра стоит на паузе, то ничего не происходит
     * Иначе:
     * 1.Увеличиваются все таймеры
     * 2.Вызывается аналогичный метод для всех объектов и контроллеров
     * 3.Создается помощь {@link #createHelp()}
     * 4.Если уровень не последний, то:
     * -обновляются астероиды
     * -проверяются столконвения {@link #checkCollisions()}
     * -если жив бот, то у него отрабатывает метод обновления
     * 5.Если уровень последний, то:
     * -если противник жив, у него вызывается метод обновления
     * -проверяется столкновение героя и противника
     * -проверяется попадание пуль в корабли
     * -возможность подбора бонусов
     * 6.Если герой погибает, то переходим на экран проигрыша
     * 7.Если все астероиды уничтожены, то переходим на новый уровень:
     * 7.1.Если переходим не на последний уровень, то:
     * -появляется бот
     * -создаются новые астероиды
     * 7.2.Если переходим на последний уровень, то:
     * -появляется противник
     * -включается музыка для уровня с боссом
     * 8.Если уничтожаем босса, то переходим на экран победы
     */
    public void update(float dt) {
        if (pause) {
            return;
        }
        helpTimer += dt;
        roundTimer += dt;
        background.update(dt);
        hero.update(dt);
        botHelper.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        infoController.update(dt);
        createHelp();

        if (!isBossLevel()) {
            if (bot.isAlive()) {
                bot.update(dt);
            }
            asteroidController.update(dt);
            checkCollisions();
        } else {
            if (botBoss.isAlive()) {
                botBoss.update(dt);
            }
            if (botBoss.getHp() < botBoss.getHpMax() / 2) {
                botBoss.upgradeWeapon();
            }
            checkInteractions();
            bulletHitToShip(botBoss);
            takePowerUps();
        }

        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(GAMEOVER, hero);
        }
        if (getAsteroidController().getActiveList().isEmpty()) {
            if (level < BOSS_LEVEL - 1) {
                nextLevel();
                bot.resurrection();
                createAsteroids();
            } else if (level == BOSS_LEVEL - 1) {
                nextLevel();
                music.stop();
                lastLevelMusic.setLooping(true);
                if (isMusicPlay) {
                    lastLevelMusic.play();
                }
            }
        }

        if (isBossLevel() && !botBoss.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.WIN, hero);
        }
        stage.act(dt);
    }

    /**
     * Переход на новый уровень
     */
    public void nextLevel() {
        level++;
        hero.setHp(hero.getHpMax());
        if (MenuScreen.isSoundPlay) {
            nextLevelSound.play();
        }
        roundTimer = 0.0f;
    }

    /**
     * Метод для создания бонуса помощником
     */
    public void createHelp() {
        if (botHelper.getHelpArea().overlaps(hero.getHitArea()) && helpTimer >= hero.getBotTimer()) {
            powerUpsController.create(botHelper.getPosition().x, botHelper.getPosition().y);
            helpTimer = 0.0f;
        }
    }

    /**
     * Проверка всех видов столконвений:
     * астероида и героя/бота {@link #boom}
     * попадение пули в астероид {@link #bulletHitToAsteroid}
     * попадение пули в корабль {@link #bulletHitToShip}
     * возможность подбора героем бонусов {@link #takePowerUps()}
     * столкновение героя и бота
     */
    public void checkCollisions() {
        boom(hero);
        boom(bot);
        bulletHitToAsteroid();
        bulletHitToShip(bot);
        takePowerUps();

        //столкновение героя и бота
        if (hero.getHitArea().overlaps(bot.getHitArea()) && bot.isAlive()) {
            push(hero, bot);
        }
    }

    /**
     * Метод для подбора бонусов
     * Если бонус попал в магнитное поле героя, то он притягивается к нему
     * Если бонус попал в зону поражения героя, то бонус подобран. Добавляется эффект {@link ParticleController.EffectBuilder#takePowerUpEffect}и звук
     */
    private void takePowerUps() {
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getSearchArea().contains(p.getPosition())) {
                tmpVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tmpVec, 200.0f);
            }
            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                    p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
                if (MenuScreen.isSoundPlay) {
                    powerUpSound.play();
                }
            }
        }
    }

    /**
     * Метод проверки попадения пули в астероид
     * Если пуля попала в астероид, то:
     * - добавляется эффект {@link ParticleController.EffectBuilder#bulletCollideWithAsteroidEffect}
     * - пуля деактивироуется
     * Если пуля принадлежит герою, то наносимый урон вычисляется как текущий урон оружия героя +вероятность критического удара
     * Если пуля принадлежит боту, то наносимый урон - текущий урон оружия бота
     * Астероид получает урон.
     * Если пуля принадлежит герою, то герою начилсяются очки и создаются бонусы
     */
    private void bulletHitToAsteroid() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.getEffectBuilder()
                        .bulletCollideWithAsteroidEffect(b.getPosition(), b.getVelocity());

                    b.deactivate();

                    int damage = 0;
                    if (b.getOwner().getOwnerType() == PLAYER) {
                        damage = hero.getCurrentWeapon().getDamage();
                        if (random(0, 100) <= hero.getCritical()) {
                            damage *= 3;
                            showDamage("-", damage, a.getPosition(), Color.PURPLE);
                        } else {
                            damage = bot.getCurrentWeapon().getDamage();
                        }
                    }
                    if (a.takeDamage(damage)) {
                        if (b.getOwner().getOwnerType() == PLAYER) {
                            hero.addScore(a.getHpMax() * 100);
                            for (int k = 0; k < 3; k++) {
                                powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Метод попадения пули в корабль
     * <p>
     * Если пуля принадлежит герою и бот жив и пуля попадает во вражеский корабль:
     * - если бот это бот босс, то создается бонус
     * - пуля деактивируется
     * - в зависимости от вероятности критического удара корабль получает урон
     * - отображается сообщение об уроне
     * - герой получает очки
     * - если корабль уничтожен, то добавляется эффект {@link ParticleController.EffectBuilder#botIsDeadEffect}
     * <p>
     * Если пуля принадлежит вражескому кораблю и попадает в героя:
     * -герой получает урон
     * -отображется сообщение об уроне
     * - пуля деактивируется
     *
     * @param ship вражеский корабль
     */
    private void bulletHitToShip(Ship ship) {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if (b.getOwner().getOwnerType() == PLAYER && ship.isAlive()) {
                if (ship.getHitArea().contains(b.getPosition())) {
                    if (ship instanceof BotBoss) {
                        createPowerUps();
                    }
                    b.deactivate();
                    int damage = hero.getCurrentWeapon().getDamage();
                    if (random(0, 100) < hero.getCritical()) {
                        damage *= 3;
                    }

                    ship.takeDamage(damage);
                    showDamage("HP -", damage, ship.getPosition(), CYAN);
                    hero.addScore(ship.getHpMax() * 100);

                    if (!ship.isAlive()) {
                        particleController.getEffectBuilder().botIsDeadEffect(ship.getPosition().x, ship.getPosition().y);
                    }
                }
            }

            if (b.getOwner().getOwnerType() == BOT) {
                if (hero.getHitArea().contains(b.getPosition())) {
                    hero.takeDamage(ship.getCurrentWeapon().getDamage());
                    showDamage("HP -", ship.getCurrentWeapon().getDamage(), hero.getPosition(), RED);
                    b.deactivate();
                }
            }
        }
    }

    /**
     * Создание бонуса при попадении в главного бота
     */
    private void createPowerUps() {
        if (random(0, 100) <= botBoss.getProbably()) {
            powerUpsController.create(botBoss.getPosition().x, botBoss.getPosition().y);
        }
    }

    /**
     * Метод для отображения урона
     *
     * @param str      текст
     * @param damage   нанесенный урон
     * @param position координаты х и у, в которых отобразится сообщение
     * @param color    цвет
     */
    private void showDamage(String str, int damage, Vector2 position, Color color) {
        stringBuilder.clear();
        stringBuilder.append(str).append(damage);
        infoController.setup(position.x, position.y,
            stringBuilder, color);
    }

    /**
     * Столкновение астероида и корабля
     *Если корабль столкнулся с астероидом, то:
     * они отталкиваются друг от друга и им придаются ускорения в противоположные друг от друга стороны
     * если корабль - герой, то он получает урон
     * @param ship
     */
    private void boom(Ship ship) {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(ship.getHitArea()) && ship.isAlive()) {
                push(a, ship);

                float sumScl = ship.getHitArea().radius * 2 + a.getHitArea().radius;

                ship.getVelocity().mulAdd(tmpVec, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, -200.0f * ship.getHitArea().radius / sumScl);

                a.takeDamage(2);
                if (ship instanceof Hero) {
                    int hurt = (int) (level * a.getScale() * 5);
                    hero.takeDamage(hurt);
                    showDamage("HP -", hurt, hero.getPosition(), RED);
                    ((Hero) ship).addScore(a.getHpMax() * 20);
                }
            }
        }
    }

    /**
     * Метод для отталкивания объектов, реализующий интерфейс {@link Pushable}
     */
    private void push(Pushable somebody1, Pushable somebody2) {
        float dst = somebody1.getPosition().dst(somebody2.getPosition());
        float halfOverLen = (somebody1.getHitArea().radius + somebody2.getHitArea().radius - dst) / 2.0f;
        tmpVec.set(somebody2.getPosition()).sub(somebody1.getPosition()).nor();
        somebody2.getPosition().mulAdd(tmpVec, halfOverLen);
        somebody1.getPosition().mulAdd(tmpVec, -halfOverLen);
    }

    /**
     * Метод для проверки столкновения героя и босса
     * Если они столкнулись, то они отталкиваются друг от друга и каждый получает урон
     */
    private void checkInteractions() {
        if (hero.getHitArea().overlaps(botBoss.getHitArea())) {
            push(hero, botBoss);
            hero.takeDamage(1);
            botBoss.takeDamage(1);
        }
    }

    /**
     * Метод для очистки ресурсов
     */
    public void dispose() {
        background.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
