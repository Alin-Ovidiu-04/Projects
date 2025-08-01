import java.awt.*;

public class Planet extends GameObject {

    private long animationTime;
    public Planet(int x, int y, ID id) {
        super(x, y, id);

        animationTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if(x> 150)
        {
            x--;
        }
    }

    @Override
    public void render(Graphics g) {
            g.drawImage(SpriteLoader.planet[(int) (System.currentTimeMillis() - animationTime) / 30 % 77 ], x, y, null);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
