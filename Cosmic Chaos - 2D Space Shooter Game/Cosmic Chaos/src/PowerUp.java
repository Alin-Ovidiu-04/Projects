import java.awt.*;

public class PowerUp extends GameObject{

    Type type;
    Handler handler;
    public PowerUp(int x, int y, ID id, Type type, Handler handler) {

        super(x, y, id);

        this.type = type;
        this.handler = handler;

        this.velY = 2;
    }

    @Override
    public void update() {
        y += velY;

        if (y >= 1100) {
            handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(SpriteLoader.shield_pickup, x, y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y,30, 30);
    }

    public enum Type {
        Shield()
    }

}
