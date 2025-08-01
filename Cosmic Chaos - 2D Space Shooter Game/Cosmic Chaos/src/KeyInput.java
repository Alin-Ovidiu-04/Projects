import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {
    Game game;
    Player player;

    public KeyInput(Game game) {
        this.game = game;
        this.player = game.player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(game.state) {
            case Game -> gameInput(e);
            case Menu -> game.menu.Input(e);
            case Level -> game.level.Input(e);
        }
    }

    public void gameInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        else if (e.getKeyCode() == KeyEvent.VK_R) {
            game.restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}