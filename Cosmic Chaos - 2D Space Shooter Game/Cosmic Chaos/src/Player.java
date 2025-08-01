import java.awt.*;
import java.awt.event.KeyListener;

import static java.lang.Math.round;

public class Player extends GameObject {
    private Game game;
    private Handler handler;

    public int health;
    public int ammo;

    public int score;

    public int dmg;
    private int speed;
    private int atkSpeed;

    private int stage;
    private long lastAtkTime;

    private long animationTime;
    public int size = 144, offset = 30;

    private boolean isShooting;

    private boolean shield;
    private long shieldTime = 0;

    public Player(int x, int y, Game game, Handler handler) {
        super(x, y, ID.Player);

        this.game = game;
        this.handler = handler;

        health = 100;
        ammo = 100;
        score= 0;
        speed = 3;
        dmg = 5;

        atkSpeed = 4;
        lastAtkTime = System.currentTimeMillis();
        animationTime = System.currentTimeMillis();
        isShooting = false;
    }

    @Override
    public void update() {

        if(System.currentTimeMillis()-shieldTime > 5000)
        {
            shield = false;
        }

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (getBounds().intersects(tempObject.getBounds())) {
                switch (tempObject.getId()) {
                    // collision with enemy bullet
                    case EnemyBullet -> {
                        // delete the bullet
                        handler.removeObject(tempObject);

                        // decrease health
                        if (!shield)
                            this.health -= 10;
                    }

                    // collision with enemy ship
                    case Enemy -> {
                        // delete the enemy
                        handler.removeObject(tempObject);

                        if (shield) {   // deactivate shield
                            shield = false;
                        }
                        else {  // death
                            this.health = 0;
                        }
                    }

                    // collision with ammo crate
                    case AmmoCrate -> {
                        this.ammo += 20;
                        handler.removeObject(tempObject);
                    }

                    //collision with power-up
                    case PowerUp -> {
                        // delete the power-up
                        handler.removeObject(tempObject);

                        // activate power-up
                        shield = true;
                        shieldTime = System.currentTimeMillis();
                    }

                    // collision with asteroid
                    case Asteroid -> {
                        handler.removeObject(tempObject);

                        if (!shield) {
                            health -= 20;
                        }
                    }
                }
            }
        }



        // make player follow the mouse
        this.x = (int)(MouseInfo.getPointerInfo().getLocation().getX() - game.getLocationOnScreen().getX()) - size/2;
        this.y = (int)(MouseInfo.getPointerInfo().getLocation().getY() - game.getLocationOnScreen().getY()) - size/2;

        // forbid player to get out of screen
        if (x < 0) x = 0;
        else if (x+size > game.width) x = game.width - size;
        if (y < 0) y = 0;
        else if (y+size > game.height) y = game.height - size;

        // shoot with the current attack speed
        boolean isReadyForNextAttack = (System.currentTimeMillis() - lastAtkTime) > (1000 / atkSpeed);

        if (isShooting && isReadyForNextAttack && ammo > 0) {
            shoot();
            lastAtkTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(Graphics g) {
        // hit box
        // g.setColor(Color.blue);
        // g.drawRect(x+offset, y+offset, size-offset*2, size-offset*2);

        g.drawImage(SpriteLoader.engine_image[(int)(System.currentTimeMillis()-animationTime)/50%4],x,y,null);

        if(health >= 100)
            g.drawImage(SpriteLoader.player_image[0], x, y, null);
        else
            g.drawImage(SpriteLoader.player_image[3 - health/25], x, y, null);

        if(shield)
        {
            g.drawImage(SpriteLoader.body_shield[(int)(System.currentTimeMillis()-shieldTime)/75%10],x-24,y-22,null);
        }
    }

    public void toggleShooting() {
        isShooting = !isShooting;
    }

    public void shoot() {
        ammo--;
        handler.addObject(new Bullet(x + size/2-7, y + size/2, ID.Bullet, handler));

        game.soundManager.playSoundEffect(0);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x+offset, y+offset, size-offset*2, size-offset*2);
    }
}
