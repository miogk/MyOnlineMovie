package com.example.miogk.myonlinemovie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PLTextureView extends AppCompatActivity {
    Activity activity = this;
    private static final String TAG = "PLTEXTUREVIEW";
    @BindView(R.id.PLVideoTextureView)
    PLVideoTextureView mVideoView;
    @BindView(R.id.loading_texture_view)
    ProgressBar loading_view;
    @BindView(R.id.pl_video_texture_view_layout_play)
    ImageView play;
    Handler handler = new Handler();
    @BindView(R.id.pl_video_texture_view_layout_seek_bar)
    SeekBar pl_video_texture_view_layout_seek_bar;
    @BindView(R.id.pl_video_texture_view_layout_start)
    TextView pl_video_texture_view_layout_start;
    @BindView(R.id.pl_video_texture_view_layout_duration)
    TextView pl_video_texture_view_layout_duration;
    @BindView(R.id.pl_video_texture_view_layout_fullscreen)
    ImageView pl_video_texture_view_layout_fullscreen;
    private boolean is_full_screen = false;
    private MyRunnable myRunnable;
    private float mPosX;
    private float mPosY;
    private float mCurrentPosX;
    private float mCurrentPosY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.pl_video_texture_view_layout);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
//        MediaController mediaController = new com.example.miogk.myonlinemovie.utilssssss.MediaController(activity);
//        mVideoView.setMediaController(mediaController);
        mVideoView.setBufferingIndicator(loading_view);
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN); //原始尺寸
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);//适应屏幕
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);//全屏铺满
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
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
                    case MotionEvent.ACTION_MOVE:
                        mCurrentPosX = event.getX();
                        mCurrentPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((mPosX - mCurrentPosX) > 50) {
                            Log.e(TAG, "onTouch: 向左");
                        } else if ((mCurrentPosX - mPosX) > 50) {
                            Log.e(TAG, "onTouch: 向右");
                        } else if ((mPosY - mCurrentPosY) > 50) {
                            Log.e(TAG, "onTouch: 向上");
                        } else if ((mCurrentPosY - mPosY) > 50) {
                            Log.e(TAG, "onTouch: 向下");
                        }
                        break;
                }
                return true;
            }
        });
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);//16:9
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_4_3);//4:3
//                    final PLVideoView mVideoView = view.findViewById(R.id.PLVideoView);
        pl_video_texture_view_layout_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mVideoView.seekTo(progress);
                if (!mVideoView.isPlaying()) {
                    mVideoView.start();
                }
            }
        });
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                mVideoView.start();
                long duration = mVideoView.getDuration();
                pl_video_texture_view_layout_duration.setText(formatDuration(duration));
                pl_video_texture_view_layout_seek_bar.setMax((int) duration);
                pl_video_texture_view_layout_fullscreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            //垂直方向，那么切换成水平方向
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        }
                        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        }
                    }
                });
                myRunnable = new MyRunnable();
                handler.postDelayed(myRunnable, 1000);
            }
        });
        mVideoView.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                play.setImageResource(R.mipmap.play2);
                handler.removeCallbacks(myRunnable);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged: " + newConfig.getLayoutDirection());
    }

    private void toggle() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(myRunnable);
            play.setImageResource(R.mipmap.pause);
        } else {
            mVideoView.start();
            play.setImageResource(R.mipmap.play2);
            handler.postDelayed(myRunnable, 1000);
        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            pl_video_texture_view_layout_start.setText(formatDuration(mVideoView.getCurrentPosition()));
            pl_video_texture_view_layout_seek_bar.setProgress((int) mVideoView.getCurrentPosition());
            handler.postDelayed(this, 1000);
        }
    }

    //格式化时间戳
    private String formatDuration(long duration) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return simpleDateFormat.format(duration);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(myRunnable);
            play.setImageResource(R.mipmap.pause);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mVideoView.start();
    }

    @Override
    public void onBackPressed() {
        //判断屏幕是否是横屏，是的话关闭播放并退出
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mVideoView.stopPlayback();
            super.onBackPressed();
        } else {
            //是竖屏的话先转化为横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    class Solution {
        public String toLowerCase(String str) {
            return str.toLowerCase();
        }
    }
}