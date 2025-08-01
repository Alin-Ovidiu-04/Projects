import java.awt.*;

import static java.lang.Math.abs;

public class EnemyBullet2 extends GameObject{

    Handler handler;
    private int speed;

    private Player player;

    private int playerX, playerY;

    private double directionX, directionY;

    public EnemyBullet2(int x, int y, ID id, Handler handler) {
        super(x, y, id);

        this.handler = handler;

        this.player = handler.getPlayer();

        this.speed = 5;

        // calculate direction to target
        double deltaX = (player.x + player.size/2) - x;
        double deltaY = (player.y + player.size/2) - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // normalize direction
        directionX = deltaX / distance;
        directionY = deltaY / distance;

        playerX = player.x;
        playerY = player.y;
    }
    @Override
    public void update() {
        // moves the bullet
        move();

        // destroy the bullet if it gets out of bounds
        if (y < -20 || y > 1080 || x < 0 || x > 1940) {
            handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {

        g.setColor(Color.magenta);
        g.fillOval(x,y,15,15);
    }

    public void move() {
        // update position
        x += directionX * speed;
        y += directionY * speed;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 15, 15);
    }
}
