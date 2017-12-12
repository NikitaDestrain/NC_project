package server.gui.notificationwindow;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * A class that allows you to control the  sound when a user is notified
 */

public class Sound {
    /**
     * Indicates whether the sound was loaded or not
     */
    private boolean released = false;
    /**
     * Melody
     */
    private Clip clip = null;
    /**
     * Sound parameters
     */
    private FloatControl volumeC = null;
    /**
     * Shows whether the sound is playing now or not
     */
    private boolean playing = false;

    /**
     *
     * @param f path to file with sound
     */
    public Sound(File f) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(new Listener());
            volumeC = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;
        }
    }

    /**
     *  Show audio download status
     * @return <b> true </b> if the download is successful. <b> false </b> if loading failed
     */

    public boolean isReleased() {
        return released;
    }



    /**
     * Indicates whether the sound is playing at the moment
     * @return <b> true </b> if playing. <b> false </b> if not playing
     */

    public boolean isPlaying() {
        return playing;
    }


    /**
     * The method that starts the sound reproduction
     * @param breakOld if <b> true </b> the sound will be interrupted and restarted. if <b> false </b> nothing will happen
     */
    public void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    /**
     * @see #play()
     */
    public void play() {
        play(true);
    }

    /**
     * Stops playback
     */

    public void stop() {
        if (playing) {
            clip.stop();
        }
    }


    /**
     * Volume setting
     * @param x  must be between 0 and 1 (from the quietest to the loudest)
     */
    public void setVolume(float x) {
        if (x<0) x = 0;
        if (x>1) x = 1;
        float min = volumeC.getMinimum();
        float max = volumeC.getMaximum();
        volumeC.setValue((max-min)*x+min);
    }



    /**
     * Returns the current volume
     * @return value from 0 to 1
     */
    public float getVolume() {
        float v = volumeC.getValue();
        float min = volumeC.getMinimum();
        float max = volumeC.getMaximum();
        return (v-min)/(max-min);
    }



    /**
     * Waiting for the sound to stop playing
     */
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing) clip.wait();
            } catch (InterruptedException exc) {}
        }
    }



    /**
     * Method for playing sound from the specified path to the file
     * @param s path to sound file
     * @return object Sound
     */
    public static Sound playSound(String s) {
        File f = new File(s);
        Sound snd = new Sound(f);
        snd.play();
        return snd;
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}