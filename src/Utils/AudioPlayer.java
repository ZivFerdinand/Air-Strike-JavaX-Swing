package Utils;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static int BACKGROUND = 0;
    public static int LEVEL_1 = 1;

    public static int LASER_SOUND = 0;
    public static int EXPLORE_SOUND = 1;
    public static int HIT_SOUND = 2;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;

    public AudioPlayer() {
        loadSongs();
        loadEffects();
    }

    private void loadSongs() {
        String[] names = { "MainMenu-BGM", "Background-Music" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    private void loadEffects() {
        String[] effectNames = { "Laser-Sound", "Explosion", "Object-Hit", "8Bit-Select" };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);
        updateEffectsVolume(30);

    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/res/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }

        return null;

    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume(0);
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    public void setLevelSong() {
        playSong(LEVEL_1);
    }


    public void playAttackSound() {
        updateEffectsVolume(30);
        playEffect(0);
    }
    public void playDestroySound() {
        updateEffectsVolume(20);
        playEffect(1);
    }
    public void playHitSound() {
        updateEffectsVolume(10);
        playEffect(2);
    }
    public void playSelectSound() {
        updateEffectsVolume(10);
        playEffect(3);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
//        if (!effectMute)
//            playEffect(JUMP);
    }

    private void updateSongVolume() {

        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain - 5);

    }

    private void updateEffectsVolume(int reduce) {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain - reduce);
        }
    }

}
