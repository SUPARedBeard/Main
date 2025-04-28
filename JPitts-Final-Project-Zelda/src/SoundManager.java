import javafx.scene.media.AudioClip;


//imort sound effects
public class SoundManager 
{
    public static final AudioClip ATTACK_SOUND = new AudioClip(SoundManager.class.getResource("/attack.mp3").toExternalForm());
    public static final AudioClip ENEMY_DEATH = new AudioClip(SoundManager.class.getResource("/skull.mp3").toExternalForm());
    public static final AudioClip HEART_PICKUP = new AudioClip(SoundManager.class.getResource("/getheart.mp3").toExternalForm());
    public static final AudioClip BOSS_DEATH = new AudioClip(SoundManager.class.getResource("/ganondie.mp3").toExternalForm());
}