import java.awt.*;

public class Enemy1 extends GameObject {
    public static int nrOfEnemies = 0;

    Game game;
    Handler handler;
    protected int size = 192, offset = 60;

    protected int health;

    protected int maxHealth;
    private int atkSpeed;

    private long lastAtkTime;
    private boolean isShooting;
    public Enemy1(int x, int y, ID id, Game game, Handler handler) {
        super(x, y, id);

        this.game = game;
        this.handler = handler;

        this.isShooting = true;

        this.maxHealth = this.health = 20;
        this.atkSpeed = 1;

        this.lastAtkTime = System.currentTimeMillis();

        nrOfEnemies++;
    }

    @Override
    public void update() {
        // check collision with bullet
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ID.Bullet)
                if (getBounds().intersects(tempObject.getBounds())) {
                    // delete the bullet
                    handler.removeObject(tempObject);

                    // damage enemy
                    this.health -= handler.getPlayer().dmg;
                }
        }

        // death
        if (this.health <= 0) {
            // drop pickup
            game.spawner.dropRandomPickup(x+size/2, y+size/2);

            // play sound
            game.soundManager.playSoundEffect(1);

            nrOfEnemies--;
            handler.removeObject(this);
        }

        // shoot with the current attack speed
        boolean isReadyForNextAttack = (System.currentTimeMillis() - lastAtkTime) > (1000 / atkSpeed);

        if (isShooting && isReadyForNextAttack) {
            shoot();
            lastAtkTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(Graphics g) {
        // hit box
        // g.setColor(Color.red);
        // g.drawRect(x + offset, y + offset, size - offset*2, size - offset*2);

        drawHealthBar(g, health, maxHealth);

        g.drawImage(SpriteLoader.enemy_image, x, y, null);
    }

    public void drawHealthBar(Graphics g, int health, int maxHealth) {
        // health bar
        g.setColor(Color.red);
        g.fillRect(x+55, y+40, health*4, 5);
        g.setColor(Color.black);
        g.drawRect(x+55, y+40, maxHealth*4, 5);
    }

    public void shoot() {
        handler.addObject(new EnemyBullet1(x + size/2, y + size/2, ID.EnemyBullet, handler));
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + offset, y + offset, size - offset*2, size - offset*2);
    }
}
