import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] bufferedImages;

    private long lastFrameChangeTime;

    private boolean isRunning;

    private int nrFrames, currentFrame;

    private double speed;

    Animation(BufferedImage[] bufferedImages, double speed) {
        this.bufferedImages = bufferedImages;
        this.speed = speed;

        nrFrames = bufferedImages.length;
        currentFrame = 0;

        lastFrameChangeTime = System.currentTimeMillis();
    }

    public void start() {
        isRunning = true;
    }

    public void run() {
        if (isRunning) {
            // change frames based on speed
            if ((System.currentTimeMillis() - lastFrameChangeTime) > 1f/speed*1000) {
                nextFrame();
                lastFrameChangeTime = System.currentTimeMillis();
            }
        }
    }

    public void nextFrame() {
        currentFrame++;

        // loop back to 0
        if (currentFrame >= nrFrames) {
            currentFrame -= nrFrames;
        }
    }

    public BufferedImage getCurrentFrame() {
        return bufferedImages[currentFrame];
    }

    public boolean isOnLastFrame() {
        if (currentFrame == nrFrames-1)
            return true;
        return false;
    }
}
