import java.awt.*;
import java.awt.event.KeyEvent;

public class LevelMenu {
    Game game;

    Planet planet;
    String[] options = {
            "1.SunBlow",
            "2.MoonFall",
            "3.Eclipse"
    };

    Boolean[] completed = {false, false, false};

    private long animationTime;
    double []angle = {Math.toRadians(120), Math.toRadians(240), Math.toRadians(360)};
    double rotationSpeed;
    int majorAxis;
    int minorAxis;
    boolean active;
    private int selection;

    public LevelMenu(Game game) {
        selection = 0;
        active = false;
        this.game = game;
        planet = new Planet(500,100,ID.Planet);
        rotationSpeed = 0.005;
        majorAxis = 230; // Axele mari ale orbitei eliptice
        minorAxis = 190; // Axele mici ale orbitei eliptice

        animationTime = System.currentTimeMillis();
    }
    public void Input(KeyEvent e) {
        switch (e.getKeyCode()) {
            // enter game
            case KeyEvent.VK_ENTER -> {
                switch (selection) {
                    case 0 -> {  // Lvl1
                        if(completed[0] == false) {
                            active = true;
                        }
                    }
                    case 1 -> {  // Lvl2
                        if(completed[1] == false) {
                            active = true;
                        }
                    }
                    case 2 -> {  // Lvl3
                        if(completed[2] == false) {
                            active = true;
                        }
                    }
                }
            }

            // change selection
            case KeyEvent.VK_RIGHT -> {
                selection++;
                if (selection >= options.length) selection = 0;
            }
            case KeyEvent.VK_LEFT -> {
                selection--;
                if (selection < 0) selection = options.length - 1;
            }

            // exit game
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }

    public void update() {
        if(minorAxis == majorAxis)
        {
            planet.update();
        }

        for (int i = 0; i < 3; i++)
        {
            angle[i] += rotationSpeed;
        }

        if(active)
        {
            if(majorAxis > 0)
                majorAxis --;
            if(minorAxis >0)
                minorAxis --;
        }

        if(planet.getX() == 150) {

            switch (selection) {
                case 0 -> {  // Lvl1
                    if(completed[0] == false) {
                        game.soundManager.playMusic(3);
                        game.resetMousePosition();
                        game.handler.reset();
                        game.levels = new Levels(Levels.LvlType.Lvl1, game.spawner);
                        game.state = STATE.Game;
                        restart(game, 0);
                    }
                }
                case 1 -> {  // Lvl2
                    game.resetMousePosition();
                    game.handler.reset();
                    game.levels = new Levels(Levels.LvlType.Lvl2,game.spawner);
                    game.state = STATE.Game;
                    restart(game,1);
                }
                case 2 -> {  // Lvl3
                    game.resetMousePosition();
                    game.handler.reset();
                    game.levels = new Levels(Levels.LvlType.Lvl3,game.spawner);
                    game.state = STATE.Game;
                    restart(game,2);
                }
            }
        }
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

        int planetX = planet.getX() + 585; // Coordonata X a planetei
        int planetY = planet.getY() + 335; // Coordonata Y a planetei


        g.setFont(new Font("Impact", Font.TRUETYPE_FONT, 40));
        for (int i = 0; i < options.length; i++) {
            double X = planetX + majorAxis * Math.cos(angle[i]);
            double Y = planetY + minorAxis * Math.sin(angle[i]);


            if (selection == i) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.white);
            }

            g.drawString(options[i], (int)X, (int)Y);
        }

        //planet
        planet.render(g);

        //ship

        g.drawImage(SpriteLoader.engine_image[(int)(System.currentTimeMillis()-animationTime)/50%4],planet.getX()-300,planet.getY()+250,null);
        g.drawImage(SpriteLoader.player_image[0], planet.getX()-300, planet.getY()+250, null);

    }

    public void restart(Game game, int poz){
        completed[poz] = true;
        selection = 0;
        active = false;
        this.game = game;
        planet = new Planet(500,100,ID.Planet);
        rotationSpeed = 0.005;
        majorAxis = 230; // Axele mari ale orbitei eliptice
        minorAxis = 190; // Axele mici ale orbitei eliptice

        animationTime = System.currentTimeMillis();
    }

    public void reset(){
        completed[0] = false;
        completed[1] = false;
        completed[2] = false;

        selection = 0;
        active = false;
        this.game = game;
        planet = new Planet(500,100,ID.Planet);
        rotationSpeed = 0.005;
        majorAxis = 230; // Axele mari ale orbitei eliptice
        minorAxis = 190; // Axele mici ale orbitei eliptice

        animationTime = System.currentTimeMillis();
    }

}
