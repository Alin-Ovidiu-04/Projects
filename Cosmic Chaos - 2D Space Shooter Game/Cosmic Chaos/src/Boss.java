import java.awt.*;
import java.util.Random;

public class Boss extends Enemy2 {

    long lastAsteroidShootTime;
    long lastPickupSpawnTime;

    Game game;
    Handler handler;

    public Boss(int x, int y, ID id, Game game, Handler handler) {
        super(x, y, id, game, handler);

        this.game = game;
        this.handler = handler;

        size = 256;
        maxHealth = health = 200;

        lastAsteroidShootTime = System.currentTimeMillis();
        lastPickupSpawnTime = System.currentTimeMillis();
    }

    public void update() {
        super.update();

        // shoot asteroids every 2 seconds
        if ((System.currentTimeMillis()-lastAsteroidShootTime) > 2000) {
            shootAsteroids(x+size/2 - 50, y+size/2);
            lastAsteroidShootTime = System.currentTimeMillis();
        }

        // spawn random pickup every 4 seconds
        if ((System.currentTimeMillis()-lastPickupSpawnTime) > 4000) {
            Random random = new Random();
            int x = random.nextInt() % game.height;

            game.spawner.dropRandomPickup(x, y-30);

            lastPickupSpawnTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(Graphics g) {
        // hit box
        // g.drawRect(x+size/5, y+size/6, size-100, size-90);

        g.drawImage(SpriteLoader.boss_image, x, y, null);

        // health bar
        g.setColor(Color.red);
        g.fillRect(200, 800, (int)(((double)health/maxHealth)*1000), 30);
        g.setColor(Color.black);
        g.drawRect(200, 800, 1000, 30);
    }

    private void shootAsteroids(int x, int y) {
        // down
        Asteroid newAsteroid = new Asteroid(x, y, ID.Asteroid, game);
        newAsteroid.setDirectionTowards(x, y + 10);
        handler.addObject(newAsteroid);

        // down-right
        Asteroid newAsteroid1 = new Asteroid(x, y, ID.Asteroid, game);
        newAsteroid1.setDirectionTowards(x + 10, y + 10);
        handler.addObject(newAsteroid1);

        // down-left
        Asteroid newAsteroid2 = new Asteroid(x, y, ID.Asteroid, game);
        newAsteroid2.setDirectionTowards(x - 10, y + 10);
        handler.addObject(newAsteroid2);

        // left
        Asteroid newAsteroid3 = new Asteroid(x, y, ID.Asteroid, game);
        newAsteroid3.setDirectionTowards(x - 10, y);
        handler.addObject(newAsteroid3);

        // right
        Asteroid newAsteroid4 = new Asteroid(x, y, ID.Asteroid, game);
        newAsteroid4.setDirectionTowards(x + 10, y);
        handler.addObject(newAsteroid4);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x+size/5, y+size/6, size-100, size-90);
    }
}
