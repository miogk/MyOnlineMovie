package com.example.miogk.myonlinemovie.customView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.miogk.myonlinemovie.R;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;

public class MyPLVideoTextureView extends PLVideoTextureView {
    ProgressBar loading_view;
    Handler handler;
    ImageView play;

    public void init(Handler handler, ImageView play, ProgressBar loading_view) {
        this.loading_view = loading_view;
        this.handler = handler;
        this.play = play;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                loading_view.setVisibility(View.GONE);
                toggle();
                play.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        play.setVisibility(View.GONE);
                    }
                }, 1000);
                break;
        }
        return super.onTouchEvent(event);
    }

    public MyPLVideoTextureView(Context context) {
        super(context);
    }

    public MyPLVideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.stopPlayback();
    }

    private void toggle() {
        if (this.isPlaying()) {
            this.pause();
            play.setImageResource(R.mipmap.pause);
        } else {
            this.start();
            play.setImageResource(R.mipmap.play2);
        }
    }
}
