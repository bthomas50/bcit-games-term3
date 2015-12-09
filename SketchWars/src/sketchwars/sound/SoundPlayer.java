/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.sound;

import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.util.ArrayList;

/**
 *
 * @author David Ly <ly_nekros@hotmail.com>
 */
public final class SoundPlayer
{
    
    private static final ArrayList<Clip> bgmList = new ArrayList<Clip>();
    private static final ArrayList<String> sfxList = new ArrayList<String>();
    private SoundPlayer()
    {}
    
    public static void loadSound()
    {     
            sfxList.add("content/sfx/bluntAttack.wav");
            sfxList.add("content/sfx/rifleShot.wav");
            sfxList.add("content/sfx/throwingObject.wav");
            sfxList.add("content/bgm/mainTheme.wav");
            try
            {
                AudioInputStream soundStream = AudioSystem.getAudioInputStream(new File("content/bgm/loopMainTheme.wav"));
                bgmList.add(getClip(soundStream));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    
    public static void playSFX(int refNumber, boolean autostart, float gain) throws Exception 
    {
        /*Loading new audiostream for each call allows for multiple instances of the same sound to occur
        ie: Two players shoot a rocket at the same time
        */
        AudioInputStream soundStream = AudioSystem.getAudioInputStream(new File(sfxList.get(refNumber)));
        final Clip clip = getClip(soundStream);
        clip.setFramePosition(0);
        
        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                LineEvent.Type type = event.getType();
                if (type == LineEvent.Type.STOP) {
                    clip.close();
                }
            }
        });
        
       // incrementGainIfPossible(refNumber, gain);
        
        if(autostart) clip.start();
    }
    
    public static void playMusic(int refNumber, boolean loop, float gain) throws Exception 
    {
        Clip clip = bgmList.get(refNumber);
        clip.setFramePosition(0);

        incrementGainIfPossible(refNumber, gain);

        if(loop) 
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        
    }

    public static void incrementGainIfPossible(int refNumber , float gain)
    {
        if(bgmList.get(refNumber).isControlSupported(FloatControl.Type.MASTER_GAIN))
        {
            // values have min/max values, for now don't check for outOfBounds values
            FloatControl gainControl = (FloatControl)bgmList.get(refNumber).getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gainControl.getValue() + gain);
        }
    }
    
    private static Clip getClip(AudioInputStream soundStream) throws Exception
    {
        Clip clip = null;
        try {
            AudioFormat format = soundStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(soundStream);
        } catch (IllegalArgumentException | LineUnavailableException | IOException ex) {
            System.err.println(ex);
        }
        return clip;
    }

    public static void pause(int refNumber)
    {
        bgmList.get(refNumber).stop();
    }
    
    public static void resume(int refNumber)
    {
        bgmList.get(refNumber).start();
    }   

}
