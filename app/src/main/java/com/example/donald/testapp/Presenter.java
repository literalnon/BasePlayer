package com.example.donald.testapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * Created by bloold on 19.07.17.
 */

public class Presenter extends BasePresenter<Model, BaseView> {

    private MediaPlayer mediaPlayer;

    public Presenter(Context context){
        model = new Model(context);
        mediaPlayer = new MediaPlayer();
    }

    public void bindView(BaseView view){
        this.view = view;
    }

    public void play(){
        boolean looping = mediaPlayer.isLooping();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(model.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(looping);
            view.play();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //handler.removeCallbacks(runnable);
                    model.incCurPosition();
                    view.play();
                    play();
                }
            });
        } catch (Exception e) {
            Log.e("mediaPlayer", e.toString());
        }
    }

    public void play(int position){
        model.setCurPosition(position);
        play();
    }

    public void dragFile(File dragFile, int newPos){
        model.dragfile(dragFile, newPos);
    }

    public int getPosition(File f){
        return model.getPosition(f);
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public void addMp3(File file){
        model.addmp3(file);
    }

    public File getMp3File(int location){
        return model.getFile(location);
    }
    public int getSizeMp3(){
        return model.getSizeMp3();
    }

    public int incCurPosition(){
        return model.incCurPosition();
    }

    public int decCurPosition(){
        return model.decCurPosition();
    }

    public int getCurrentPosition(){
        return model.getCurPosition();
    }

    public int getMaxVolume(){
        return model.getMaxVolume();
    }

    public int getCurVolume(){
        return model.getCurVolume();
    }

    public void setVolume(int volume){
        model.setVolume(volume);
    }

    public void setProgress(int progress){
        mediaPlayer.seekTo(progress);
    }

    public boolean playerIsPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void playerStart(){
        mediaPlayer.start();
    }

    public void playerStop(){
        mediaPlayer.stop();
    }

    public void setLooping(boolean looping){
        mediaPlayer.setLooping(looping);
    }

    public boolean playerIsLooping(){
        return mediaPlayer.isLooping();
    }

    public String getName(){
        return model.getNameMp3();
    }

    public MainView getMainView(){
        return view.getMainView();
    }

}
