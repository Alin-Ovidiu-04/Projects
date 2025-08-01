import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    public Clip music;
    List<Clip> soundEffects = new ArrayList<Clip>();

    Clip clip;
    URL[] soundURL = new URL[10];

    public SoundManager() {
        soundURL[0] = getClass().getResource("/sounds/laser_gun_sound.wav");
        soundURL[1] = getClass().getResource("/sounds/explosion_sound.wav");
        soundURL[2] = getClass().getResource("/sounds/menu_music.wav");
        soundURL[3] = getClass().getResource("/sounds/level1_music.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSoundEffect(int i) {
        removeInactiveSE();

        setFile(i);
        soundEffects.add(clip);
        clip.start();
    }

    public void playMusic(int i) {
        stopMusic();
        setFile(i);
        music = clip;
        music.loop(Clip.LOOP_CONTINUOUSLY);
        music.start();
    }

    // stops music
    public void stopMusic() {
        if (music != null) {
            music.close();
            music = null;
        }
    }

    public void removeInactiveSE() {
        for (int i = soundEffects.size()-1; i >= 0; i--) {
            Clip c = soundEffects.get(i);
            if (!c.isActive()) {
                c.close();
                soundEffects.remove(i);
            }
        }
    }
}
