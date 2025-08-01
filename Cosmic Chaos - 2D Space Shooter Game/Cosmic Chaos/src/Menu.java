import java.awt.*;
import java.awt.event.KeyEvent;

public class Menu {
    Game game;

    String[] options = {
            "Start",
            "Options",
            "Exit"
    };

    private int selection;

    public Menu(Game game) {
        selection = 0;
        this.game = game;
    }
    public void Input(KeyEvent e) {
        switch (e.getKeyCode()) {
            // enter game
            case KeyEvent.VK_ENTER -> {
                switch (selection) {
                    case 0 -> {  // Start
                        game.resetMousePosition();
                        game.state = STATE.Level;
                    }
                    case 1 -> {  // Options

                    }
                    case 2 -> {  // Exit
                        System.exit(0);
                    }
                }
            }

            // change selection
            case KeyEvent.VK_DOWN -> {
                selection++;
                if (selection >= options.length) selection = 0;
            }
            case KeyEvent.VK_UP -> {
                selection--;
                if (selection < 0) selection = options.length - 1;
            }

            // exit game
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }

    public void update() {
    }

    public void render(Graphics g) {
        // background
        int offset = SpriteLoader.background_image1.getHeight();
        for (int y = 0; y < game.height; y += offset) {
            g.drawImage(SpriteLoader.background_image1, 0, y, null);
            g.drawImage(SpriteLoader.background_image2, 0, y, null);
            g.drawImage(SpriteLoader.background_image3, 0, y, null);
        }

        // title
        g.setColor(Color.white);
        g.setFont(new Font("Impact", Font.TRUETYPE_FONT, 90));
        g.drawString("Cosmic Chaos", 500, 250);

        g.setFont(new Font("Impact", Font.TRUETYPE_FONT, 40));

        // options
        for (int i = 0; i < options.length; i++) {
            if (selection == i) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.white);
            }

            g.drawString(options[i], 700, 450 + i*100);
        }

    }
}
