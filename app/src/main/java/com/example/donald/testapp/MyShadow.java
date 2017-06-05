package com.example.donald.testapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.PriorityQueue;

/**
 * Created by Donald on 04.06.2017.
 */
public class MyShadow extends View.DragShadowBuilder{

    private Point clickPoint;
    private View view;

    public MyShadow(View v, Point click){
        view = v;
        clickPoint = click;
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        view.draw(canvas);
    }

    @Override
    public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
        shadowSize.x = view.getWidth();
        shadowSize.y = view.getHeight();

        shadowTouchPoint.x = clickPoint.x;
        shadowTouchPoint.y = clickPoint.y;
    }
}
