import java.awt.image.BufferedImage;

public class SpriteSheet {

    final private BufferedImage image;
    final private int size;

    public SpriteSheet(BufferedImage image, int size) {
        this.image = image;
        this.size = size;
    }

    public BufferedImage grabImage(int col, int row, int width, int height) {
        return image.getSubimage((col * size) - size, (row * size) - size, width, height);
    }
}