import java.awt.*;

public class Bullet extends GameObject {
    Handler handler;
    private int speed;

    public Bullet(int x, int y, ID id, Handler handler) {
        super(x, y, id);

        this.handler = handler;

        speed = 15;
    }
    @Override
    public void update() {
        // moves the bullet UP
        velY = speed;
        y -= velY;

        // destroy the bullet if it gets out of bounds
        if (y < -20) {
            handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {

        g.setColor(Color.green);
        g.fillOval(x,y,15,15);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 15, 15);
    }
}
