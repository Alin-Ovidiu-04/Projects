import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    public boolean isRunning;
    private Thread thread;

    public int width = 1920;
    public int height = 1080;

    public STATE state;

    public Player player;

    Handler handler;

    HUD hud;

    Menu menu;
    LevelMenu level;

    public Spawner spawner;

    public Levels levels;
    public SoundManager soundManager;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {
        new Window(width, height, "Cosmic Chaos", this);

        handler = new Handler();

        state = STATE.Menu;

        soundManager = new SoundManager();

        menu = new Menu(this);

        spawner = new Spawner(this, handler);
        level = new LevelMenu(this);

        player = new Player(width/2, height-200, this, handler);
        handler.addPlayer(player);

        this.addKeyListener(new KeyInput(this));
        this.addMouseListener(new MouseInput(this));

        SpriteLoader.load();

        hud = new HUD(this);

        //spawner.spawnBoss();
        //spawner.spawnEnemies();

        soundManager.playMusic(2);

        start();
    }

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        // asta nu merge pentru Enemy si Enemy2(care sunt folosite in update ca sa vada daca mai ai inamici sau nu),
        // pt ca nu sunt in Handler( te folosesti direct de nrOfEnemies care e public)
        handler.reset();

        // quick fix pt problema de mai sus
        Enemy2.nrOfEnemies = 0;
        Enemy1.nrOfEnemies = 0;

        player.health = 100;
        player.ammo = 100;
        player.score = 0;

        resetMousePosition();
        //spawner.spawnEnemies1();

        level.reset();

        this.state = STATE.Menu;
    }

    public void run() {
        this.requestFocus();

        long lastTime = System.nanoTime();
        double amountOfUpdates = 60f;
        double nsPerUpdate = (1 / amountOfUpdates) * 1000000000;     // time in nanoseconds for each update
        long delta = 0;                                              // time in nanoseconds elapsed from last update

        while (isRunning) {
            long currentTime = System.nanoTime();
            delta += currentTime - lastTime;
            lastTime = currentTime;

            // check if the difference between updates >= the required nanoseconds per update
            if (delta >= nsPerUpdate) {
                update();
                delta -= nsPerUpdate;
            }

            render();
        }
        stop();
    }

    public void update() {
        switch (state) {
            case Game -> {
                handler.update();
                spawner.update();

                // death
                if (player.health <= 0) {
                    restart();
                }

                else if (Enemy2.nrOfEnemies == 0 && Enemy1.nrOfEnemies == 0 ) {
                    //spawner.spawnBoss();
                    state = STATE.Level;
                }
            }
            case Menu -> menu.update();
            case Level -> level.update();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        //============= Area for rendering to screen =================================
        switch (state) {
            case Game -> renderGame(g);
            case Menu -> menu.render(g);
            case Level -> level.render(g);
        }

        //============= Exit area for rendering to screen ===============================
        g.dispose();
        bs.show();
    }

    public void renderGame(Graphics g) {
        // background
        int offset = SpriteLoader.background_image1.getHeight();
        for (int y = 0; y < height; y += offset) {
            g.drawImage(SpriteLoader.background_image1, 0, y, null);
            g.drawImage(SpriteLoader.background_image2, 0, y, null);
            g.drawImage(SpriteLoader.background_image3, 0, y, null);
        }

        handler.render(g);

        hud.render(g);
    }

    // puts mouse in the starting position
    public void resetMousePosition() {
        try {
            (new Robot()).mouseMove(width/3 + 100, height - 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}