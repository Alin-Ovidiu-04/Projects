import java.awt.*;

public class Asteroid extends GameObject {

    Game game;

    Handler handler;

    Animation death_animation;

    private int size = 48;
    private int speed = 4;

    private int health;

    private double directionX, directionY;

    public Asteroid(int x, int y, ID id, Game game) {
        super(x, y, id);
        this.game = game;
        this.handler = game.handler;

        death_animation = new Animation(SpriteLoader.asteroid_images, 12);


        this.health = 10;
    }

    @Override
    public void update() {
        // move asteroid
        this.x += this.directionX * this.speed;
        this.y += this.directionY * this.speed;

        // collision with bullet
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ID.Bullet)
                if (getBounds().intersects(tempObject.getBounds())) {
                    // delete the bullet
                    handler.removeObject(tempObject);

                    // damage asteroid
                    this.health -= handler.getPlayer().dmg;

                    // start death animation
                    if (this.health < 0) {
                        death_animation.start();
                    }
                }
        }

        death_animation.run();

        // death
        if (death_animation.isOnLastFrame()) {
            handler.removeObject(this);
        }
    }

    @Override
    public void render(Graphics g) {
        // hitbox
        // g.drawRect(x+size/2, y+size/2, size, size);

        g.drawImage(death_animation.getCurrentFrame(), x, y, null);
    }

    public void setDirectionTowards(int x, int y) {
        // direction to center
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // normalize direction
        directionX = deltaX / distance;
        directionY = deltaY / distance;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x+size/2, y+size/2, size, size);
    }
}
