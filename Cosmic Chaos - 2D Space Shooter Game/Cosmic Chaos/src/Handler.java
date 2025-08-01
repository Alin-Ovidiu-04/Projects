import java.awt.*;
import java.util.LinkedList;

public class Handler {
    public LinkedList<GameObject> object = new LinkedList<GameObject>();
    public Player player;

    public void update() {
        player.update();

        for (int i = 0; i < object.size(); i++) {
            object.get(i).update();
        }
    }

    public void render(Graphics g) {

        for (int i = 0; i < object.size(); i++) {
            object.get(i).render(g);
        }

        player.render(g);
    }

    public void addObject(GameObject tempObject) {
        object.add(tempObject);
    }

    public void removeObject(GameObject tempObject) {

        if(tempObject.id == ID.Enemy)
            player.score += 100;
        if(tempObject.id == ID.Asteroid)
            player.score += 50;

        object.remove(tempObject);
    }

    public void reset() {
        object.clear();
    }

    public void addPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}