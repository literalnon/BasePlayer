package com.example.donald.testapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Vector;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by bloold on 19.07.17.
 */

public class Model {
    private Vector<File> mp3files;
    private AudioManager manager;
    private int curPosition;

    public Model(Context context){
        mp3files = new Vector<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                findMp3File();
            }
        }).run();

        manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        curPosition = 0;
    }

    public boolean mp3IsEmpty(){
        return mp3files.isEmpty();
    }

    public void setCurPosition(int curPosition){
        this.curPosition = curPosition;
    }

    public void dragfile(File dragFile, int newPos){
        mp3files.remove(dragFile);
        mp3files.insertElementAt(dragFile, newPos);
    }

    public int getPosition(File f){
        return mp3files.indexOf(f);
    }

    public void addmp3(File file){
        mp3files.add(file);
    }

    public File getFile(int location){
        return mp3files.elementAt(location);
    }

    public int getSizeMp3() {
        return mp3files.size();
    }

    private void findFileInDirectory(File dir) {

        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                if (f.getName().contains(".mp3")){
                    mp3files.add(f);
                }
            } else if (f.isDirectory()) {
                findFileInDirectory(f);
            }
        }
    }

    private void findMp3File() {
        findFileInDirectory(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
    }

    public String getNameMp3(){
        return mp3files.get(curPosition).getName();
    }

    public String getAbsolutePath(){
        return mp3files.get(curPosition).getAbsolutePath();
    }

    public int incCurPosition(){
        curPosition = (++curPosition) % mp3files.size();
        return curPosition;
    }

    public int getCurVolume(){
        return manager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public int getMaxVolume(){
        return manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }


    public int decCurPosition(){
        curPosition = (curPosition - 1 + mp3files.size()) % mp3files.size();
        return curPosition;
    }

    public int getCurPosition(){
        return curPosition;
    }

    public void setVolume(int volume){
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

}
