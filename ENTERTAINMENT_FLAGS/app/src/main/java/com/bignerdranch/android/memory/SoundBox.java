/*
 *5.03.2017
 *this class rules assets
 */

package com.bignerdranch.android.memory;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SoundBox implements Serializable{
    private static final String SOUNDS_FOLDER = "sounds_for_project";
    private static final int MAX_SOUNDS = 3;
    private static SoundBox sSoundBox;
    private AssetManager mAssets;
    private SoundPool mSoundPool;
    private List<Sound> mSounds = new ArrayList<>();
    private float soundPower = 1.f;

    private SoundBox(Context context){
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public static SoundBox getSoundBox(Context context){
        if (sSoundBox == null) {
            sSoundBox = new SoundBox(context);
            return sSoundBox;
        }
        else {
            return sSoundBox;
        }
    }

    private void loadSounds(){
        String[] soundNames;
        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
        }
        catch(IOException exc) {
            exc.printStackTrace();
            return;
        }

        for (String fileName : soundNames){
            try {
                String assetPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }
            catch (IOException exc){
                exc.printStackTrace();
            }
        }
    }//private void loadSounds()

    public List<Sound> getSounds(){
        return mSounds;
    }

    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, soundPower, soundPower, 1, 0, 1.f);
    }

    public void switchOffSound(){
        soundPower = 0.f;
    }

    public void switchOnSound(){
        soundPower = 1.f;
    }

    public float getSoundPower() {
        return soundPower;
    }

    public void setSoundPower(float soundPower) {
        this.soundPower = soundPower;
    }

    public void release(){
        mSoundPool.release();
    }
}
