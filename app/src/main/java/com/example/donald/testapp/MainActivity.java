package com.example.donald.testapp;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.donald.testapp.Fragment.ListFragment;
import com.example.donald.testapp.Fragment.PlayerViewFragment;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final int REQUEST_STORAGE = 1001;
    private static final String PLAYER_FRAGMENT_TAG = "player_fragment";
    private static final String LIST_FRAGMENT_TAG = "list_fragment";

    private Presenter presenter;
    private PlayerViewFragment playerFragment;
    private ListFragment listFragment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        if(Build.VERSION.SDK_INT >= 23) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }

        /*
        btnPlay.setClickable(false);


        context = this;

        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        presenter = new Presenter(context);
                        playerFragment = PlayerViewFragment.newInstance();
                        presenter.bindView(playerFragment);

                        getFragmentManager().beginTransaction().add(R.id.main_container, playerFragment, PLAYER_FRAGMENT_TAG).commit();

                    }
                }).run();
            } else {
                Toast.makeText(MainActivity.this, "Разрешения не получены", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void HidePlayerFragment() {
        FragmentManager manager = getFragmentManager();
        listFragment = (ListFragment) manager.findFragmentByTag(LIST_FRAGMENT_TAG);
        if(listFragment == null) {
            listFragment = ListFragment.newInstance();
            manager.beginTransaction().hide(playerFragment).add(R.id.main_container, listFragment, LIST_FRAGMENT_TAG).commit();
        }else{
            manager.beginTransaction().hide(playerFragment).show(listFragment).commit();
        }
    }

    @Override
    public void HideListFragment() {
        getFragmentManager().beginTransaction().hide(listFragment).show(playerFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if(playerFragment.isHidden()) {
            HideListFragment();
        } else {
            super.onBackPressed();
        }
    }
};
