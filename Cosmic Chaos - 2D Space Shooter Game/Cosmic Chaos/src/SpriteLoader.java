import java.awt.image.BufferedImage;

public class SpriteLoader {
    public static BufferedImageLoader imageLoader;

    public static BufferedImage[] player_image = new BufferedImage[4];

    public static BufferedImage[] engine_image = new BufferedImage[4];
    public static BufferedImage[] body_shield = new BufferedImage[10];
    public static BufferedImage[] planet = new BufferedImage[77];
    public static BufferedImage background_image1, background_image2, background_image3;
    public static BufferedImage enemy_image, boss_image;
    public static SpriteSheet body_shield_sprite;
    public static SpriteSheet engine_sprite;
    public static SpriteSheet shield_pickup_sprite, ammo_pickup_sprite;
    public static SpriteSheet asteroid_sprite;
    public static BufferedImage shield_pickup, ammo_pickup;

    public static BufferedImage asteroid_image;
    public static BufferedImage[] asteroid_images = new BufferedImage[7];


    public static void load() {
        imageLoader = new BufferedImageLoader();

        // load sprite sheets
        shield_pickup_sprite = new SpriteSheet(imageLoader.loadImage("/sprites/player/Pickup Icon - Shield Generator - All around shield.png"), 10);
        ammo_pickup_sprite = new SpriteSheet(imageLoader.loadImage("/sprites/player/Pickup Icon - Weapons - Rocket.png"), 10);
        body_shield_sprite = new SpriteSheet(imageLoader.loadImage("/sprites/player/body_shield.png"),192);
        engine_sprite = new SpriteSheet(imageLoader.loadImage("/sprites/player/Main Ship - Engine.png"),144);
        asteroid_sprite = new SpriteSheet(imageLoader.loadImage("/sprites/Asteroid - Explode.png"), 96);

        // player sprites
        player_image[0] = imageLoader.loadImage("/sprites/player/Main Ship - Base - Full health.png");
        player_image[1] = imageLoader.loadImage("/sprites/player/Main Ship - Base - Slight damage-export.png");
        player_image[2] = imageLoader.loadImage("/sprites/player/Main Ship - Base - Damaged-export.png");
        player_image[3] = imageLoader.loadImage("/sprites/player/Main Ship - Base - Very damaged-export.png");

        //player shield sprites
        for( int i = 0; i < 10; i++)
        {
            body_shield[i] = body_shield_sprite.grabImage(i+1,1,192,192);
        }
        //ship - engine sprites
        for(int i = 0; i < 4; i++)
        {
            engine_image[i] = engine_sprite.grabImage(i+1, 1, 144, 144);
        }

        // asteroid sprites
        for (int i = 0; i < 7; i++) {
            asteroid_images[i] = asteroid_sprite.grabImage(i+1, 1, 96, 96);
        }

        // planet sprites
        for(int i = 1; i <= 77; i++)
        {
            planet[i-1] = imageLoader.loadImage("/sprites/planet/Earth-Like planet" + i + ".png");
        }

        // background images
        background_image1 = imageLoader.loadImage("/sprites/background/Starry background  - Layer 01 - Void.png");
        background_image2 = imageLoader.loadImage("/sprites/background/Starry background  - Layer 03 - Stars.png");
        background_image3 = imageLoader.loadImage("/sprites/background/Starry background  - Layer 03 - Stars 2.png");

        // enemy sprites
        enemy_image = imageLoader.loadImage("/sprites/enemies/Kla'ed - Fighter - Base.png");

        // boss sprites
        boss_image = imageLoader.loadImage("/sprites/enemies/Kla'ed - Dreadnought - Base.png");

        // shield pickup sprite
        shield_pickup = shield_pickup_sprite.grabImage(1, 1, 32, 32);

        // ammo pickup sprite
        ammo_pickup = ammo_pickup_sprite.grabImage(1, 1, 32, 32);

        asteroid_image = imageLoader.loadImage("/sprites/Asteroid.png");
    }
}
