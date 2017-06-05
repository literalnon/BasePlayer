package com.example.donald.testapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;
import java.security.Permissions;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Context context;

    static Vector<File> mp3files;
    static Button btnPlay;
    static AudioManager manager;
    static SeekBar sbPassed;
    static MediaPlayer mediaPlayer;
    static int curPosition = 0;
    static Handler handler;
    static Runnable runnable;
    static TextView tvFileName;

    private static final int REQUEST_STORAGE = 1001;
    Button btnList, btnRepeat, btnNext, btnPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= 23) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }

        btnList = (Button)findViewById(R.id.btnList);
        btnRepeat = (Button)findViewById(R.id.btnRepeat);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPrev = (Button)findViewById(R.id.btnPrev);
        btnPlay = (Button)findViewById(R.id.btnPlay);

        btnPlay.setClickable(false);
        mp3files = new Vector<>();

        mediaPlayer = new MediaPlayer();

        manager = (AudioManager)getSystemService(AUDIO_SERVICE);
        context = this;

        SeekBar sbVolume = (SeekBar)findViewById(R.id.sbVolume);
        sbVolume.setMax(manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sbVolume.setProgress(manager.getStreamVolume(AudioManager.STREAM_MUSIC));
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbPassed = (SeekBar)findViewById(R.id.sbPassed);
        sbPassed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListActivity.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setText("play");
                }else{
                    mediaPlayer.start();
                    btnPlay.setText("pause");
                }
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.setLooping(mediaPlayer.isLooping() ? false : true);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition = (++curPosition) % mp3files.size();
                PlayRes(curPosition);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curPosition = (curPosition - 1 + mp3files.size()) % mp3files.size();
                PlayRes(curPosition);
            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                sbPassed.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(runnable, 1000);
            }
        };

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.removeCallbacks(runnable);
                curPosition = (++curPosition) % mp3files.size();
                PlayRes(curPosition);
            }
        });

        tvFileName = (TextView) findViewById(R.id.tvFileName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        findMp3File();
                    }
                }).run();
            } else {
                Toast.makeText(MainActivity.this, "Разрешения не получены", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public Vector<File> findFileInDirectory(File dir) {
        Vector<File> files = new Vector<>();
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                if (f.getName().contains(".mp3"))
                    files.add(f);
            } else if (f.isDirectory())
                files.addAll(findFileInDirectory(f));
        }
        return files;
    }

    public void findMp3File() {
        MainActivity.mp3files = findFileInDirectory(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
    }

    static public void PlayRes(int position){
        boolean looping = mediaPlayer.isLooping();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(mp3files.get(position).getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(looping);
            sbPassed.setMax(mediaPlayer.getDuration());
            sbPassed.setProgress(0);
            curPosition = position;
            handler.postDelayed(runnable, 1000);
            btnPlay.setText("pause");
            tvFileName.setText(mp3files.get(position).getName());
        } catch (Exception e) {
            Log.e("mediaPlayer", e.toString());
        }
    }
};
