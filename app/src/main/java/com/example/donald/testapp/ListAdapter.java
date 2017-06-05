package com.example.donald.testapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.preference.TwoStatePreference;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

/**
 * Created by Donald on 22.04.2017.
 */
public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Activity context;
    File f;
    View fileView;
    private int drag_y;
    private int drag_x;

    public ListAdapter(Activity context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return MainActivity.mp3files.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.mp3files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        view.setTag(MainActivity.mp3files.elementAt(position));

        TextView textView = ((TextView) view.findViewById(android.R.id.text1));

        if(MainActivity.mediaPlayer.isPlaying() && position == MainActivity.curPosition)
            textView.setText(">" + MainActivity.mp3files.get(position).getName());
        else
            textView.setText(MainActivity.mp3files.get(position).getName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)v).setText("> " + ((TextView) v).getText().toString());
                notifyDataSetChanged();
                MainActivity.btnPlay.setClickable(true);
                MainActivity.PlayRes(position);
                context.finish();
            }
        });

        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case (DragEvent.ACTION_DROP):
                        ((TextView) v).setText(f.getName());
                        break;
                    case (DragEvent.ACTION_DRAG_LOCATION):
                        swap(v);
                        swap(position);
                        break;
                }
                return true;
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View.DragShadowBuilder shadowBuilder = new MyShadow(v,new Point(drag_x,drag_y));
                v.startDrag(null, shadowBuilder, position, 0);

                ((TextView)v).setText("");

                f = MainActivity.mp3files.get(position);
                fileView = v;
                return true;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    drag_y = (int)event.getY();
                    drag_x = (int)event.getX();
                }
                return false;
            }
        });
        return view;
    }

    public void swap(int j){
        MainActivity.mp3files.remove(f);
        MainActivity.mp3files.insertElementAt(f, j);
    }

    public void swap(View v){
        String v1_text = ((TextView) v).getText().toString();
        ((TextView)fileView).setText(v1_text);
        ((TextView)v).setText("");
        fileView = v;

    }

}