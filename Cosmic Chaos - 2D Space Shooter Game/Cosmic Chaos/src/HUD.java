import java.awt.*;

public class HUD {

    private Game game;
    private Player player;

    public HUD(Game game) {
        this.game = game;
        this.player = game.player;
    }

    public void render(Graphics g) {
        // health bar
        g.setColor(Color.red);
        g.fillRect(20, 30, (int)((player.health/100f)*400), 30);

        g.setColor(Color.black);
        g.drawRect(20, 30, 400, 30);

        // show ammo
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("Ammo: " + player.ammo + " ", 40, 115);

        // show score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("Score: " + player.score + " ", 1350, 115);
    }
}
