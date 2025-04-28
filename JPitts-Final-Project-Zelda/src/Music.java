/*Josh Pitts
 * CPT 236
 * ZeldaDungeon Final Project */


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Music
{
    private MediaPlayer mediaPlayer;

    //constructor to initialize and start background music
    public Music(String musicFile) 
    {
    	String musicPath = getClass().getResource(musicFile).toExternalForm();
        Media media = new Media(musicPath);
        mediaPlayer = new MediaPlayer(media);

        //set the music to loop indefinitely (if needed)
        mediaPlayer.setCycleCount(1);
        mediaPlayer.setVolume(0.5); // Optional: Set the volume (0.0 to 1.0)
    }

    // Start playing the music
    public void play() 
    {
        if (mediaPlayer != null) 
        {
            mediaPlayer.play();
        }
    }

    // Stop the music
    public void stop() 
    {
        if (mediaPlayer != null) 
        {
            mediaPlayer.stop();
        }
    }
    
  //method to fade out the volume of the media player and stop it after a given duration
    public void fadeOutAndStop(Duration duration) 
    {
        //check if the media player is not null
        if (mediaPlayer != null) {
            //create a Timeline animation to fade the volume to 0 over the given duration
            Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(mediaPlayer.volumeProperty(), mediaPlayer.getVolume())), 
                //start the animation with the current volume
                new KeyFrame(duration, new KeyValue(mediaPlayer.volumeProperty(), 0)) 
                //end the animation with volume set to 0
            );
            
            //set an action to stop the media player once the fade animation finishes
            fade.setOnFinished(e -> mediaPlayer.stop());
            
            //start the fade animation
            fade.play();
        }
    }


    // Play a one-time sound effect
    public void playSoundEffect(String soundFile) 
    {
        String filePath = getClass().getResource(soundFile).toExternalForm();
        Media sound = new Media(filePath);
        MediaPlayer soundPlayer = new MediaPlayer(sound);
        soundPlayer.play();
    }
    
    public MediaPlayer getMediaPlayer() 
    {
        return mediaPlayer;
    }

    // Set the volume of the music
    public void setVolume(double volume) 
    {
        if (mediaPlayer != null) 
        {
            mediaPlayer.setVolume(volume);
        }
    }
}
