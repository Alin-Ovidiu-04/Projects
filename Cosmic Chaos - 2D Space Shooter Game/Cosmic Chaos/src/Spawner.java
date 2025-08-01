import java.awt.*;
import java.util.Random;

public class Spawner extends GameObject {
    private Game game;
    private Handler handler;

    private long lastAsteroidSpawnTime;

    public Spawner(Game game, Handler handler) {
        super(0, 0, null);
        this.game = game;
        this.handler = handler;

        lastAsteroidSpawnTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        // spawns asteroid every 3 seconds
        if (System.currentTimeMillis() - lastAsteroidSpawnTime > 3000) {
            spawnAsteroid();
            lastAsteroidSpawnTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(Graphics g) {

    }

    public void spawnAsteroid() {
        // calculate random position outside the screen
        Random random = new Random();
        int y = random.nextInt() % game.height;
        int x = (y % 2 == 0) ? -30 : game.width + 30;

        Asteroid newAsteroid = new Asteroid(x, y, ID.Asteroid, game);
        newAsteroid.setDirectionTowards(game.width/2, game.height/2);

        handler.addObject(newAsteroid);
    }

    public void spawnEnemies1() {
        handler.addObject(new Enemy1(1200, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy1(1000, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy1(650, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy1(300, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy1(100, 150, ID.Enemy, game, handler));
    }

    public void spawnEnemies2() {
        handler.addObject(new Enemy2(1200, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy2(1000, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy2(650, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy2(300, 150, ID.Enemy, game, handler));
        handler.addObject(new Enemy2(100, 150, ID.Enemy, game, handler));
    }

    public void spawnBoss() {
        handler.addObject(new Boss(650, 150, ID.Enemy, game, handler));
    }

    public void dropRandomPickup(int x, int y) {
        Random random = new Random();
        int rand = random.nextInt(100);
        Boolean cond1 = (0< rand)&&(rand < 50);
        Boolean cond2 = (50 < rand)&&(rand < 75);

        if (cond1) {

        }
        else {
            if(cond2)
            {
                handler.addObject(new AmmoCrate(x, y, ID.AmmoCrate, handler));
            }
            else
            {
                handler.addObject(new PowerUp(x, y, ID.PowerUp, PowerUp.Type.Shield, handler));
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
