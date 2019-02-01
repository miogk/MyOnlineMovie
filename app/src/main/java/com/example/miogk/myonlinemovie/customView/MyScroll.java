package com.example.miogk.myonlinemovie.customView;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class MyScroll extends Scroller {
    private int duration = 1500;

    public MyScroll(Context context) {
        super(context);
    }

    public MyScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, this.duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, duration*10);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
