import java.awt.*;
import java.util.Random;

public class Levels {
    private LvlType Type;
    public Spawner spawner;
    public Levels(LvlType type, Spawner s) {
        this.Type = type;
        this.spawner = s;

        Spawn();
    }

    public void Spawn(){
        switch (this.Type)
        {
            case Lvl1:
                spawner.spawnEnemies1();
                break;
            case Lvl2:
                spawner.spawnEnemies2();
                break;
            case Lvl3:
                spawner.spawnBoss();
                break;
        }
    }

    public enum LvlType{
        Lvl1(),
        Lvl2(),
        Lvl3()
    }
}
