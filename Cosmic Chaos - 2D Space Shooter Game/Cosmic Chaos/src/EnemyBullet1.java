import java.awt.*;


public class EnemyBullet1 extends GameObject{

    Handler handler;
    private int speed;

    public EnemyBullet1(int x, int y, ID id, Handler handler) {

        super(x, y, id);

        this.handler = handler;

        this.speed = 8;

    }
    @Override
    public void update() {
        // moves the bullet
        y += speed;

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

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 15, 15);
    }
}
