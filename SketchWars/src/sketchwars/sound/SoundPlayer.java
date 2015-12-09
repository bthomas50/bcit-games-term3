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
    
    private static final ArrayList<Clip> bgmList = new ArrayList<>();
    private static final ArrayList<ArrayList<Clip>> sfxList = new ArrayList<>();
    private SoundPlayer()
    {}
    
    public static void loadSound()
    {     
        try
        {
            AudioInputStream soundStream = AudioSystem.getAudioInputStream(new File("content/bgm/loopMainTheme.wav"));
            bgmList.add(getClip(soundStream));

            soundStream = AudioSystem.getAudioInputStream(new File("content/sfx/bluntAttack.wav"));
            sfxList.add(new ArrayList<Clip>());
            for(int i = 0; i < 5; i++)
            {
                sfxList.get(0).add(getClip(soundStream));
            }
            soundStream = AudioSystem.getAudioInputStream(new File("content/sfx/rifleShot.wav"));
            sfxList.add(new ArrayList<Clip>());
            for(int i = 0; i < 5; i++)
            {
                sfxList.get(0).add(getClip(soundStream));
            }
            soundStream = AudioSystem.getAudioInputStream(new File("content/sfx/throwingObject.wav"));
            sfxList.add(new ArrayList<Clip>());
            for(int i = 0; i < 5; i++)
            {
                sfxList.get(0).add(getClip(soundStream));
            }
            soundStream = AudioSystem.getAudioInputStream(new File("content/bgm/mainTheme.wav"));
            sfxList.add(new ArrayList<Clip>());
            for(int i = 0; i < 5; i++)
            {
                sfxList.get(0).add(getClip(soundStream));
            }
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
        ArrayList<Clip> allClips = sfxList.get(refNumber);
        for(Clip c : allClips)
        {
            if(!c.isRunning())
            {
                System.out.println(c);
                c.setFramePosition(0);
                if(autostart) 
                    c.start();
                break;
            }
        }
        
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
