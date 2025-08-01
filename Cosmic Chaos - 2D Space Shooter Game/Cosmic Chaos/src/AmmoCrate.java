import java.awt.*;

public class AmmoCrate extends GameObject {

    Handler handler;
    private int size;

    public AmmoCrate(int x, int y, ID id, Handler handler) {
        super(x, y, ID.AmmoCrate);

        this.handler = handler;

        size = 20;
        velY = 2;
    }

    @Override
    public void update() {
        y += velY;

        // delete if it gets out of screen
        if (y >= 1200) {
            handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(SpriteLoader.ammo_pickup, x, y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}
