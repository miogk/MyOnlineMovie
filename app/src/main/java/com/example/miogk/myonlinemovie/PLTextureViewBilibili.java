package com.example.miogk.myonlinemovie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnImageCapturedListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PLTextureViewBilibili extends AppCompatActivity implements View.OnClickListener {
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
    private HideBottomLayoutRunnable hideBottomLayoutRunnable;
    private float mPosX;
    private float mPosY;
    private float mCurrentPosX;
    private float mCurrentPosY;
    @BindView(R.id.pl_video_texture_view_bottom_layout)
    LinearLayout pl_video_texture_view_bottom_layout;
    @BindView(R.id.pl_video_texture_view_top_layout)
    ViewGroup pl_video_texture_view_top_layout;
    @BindView(R.id.pl_video_texture_view_top_layout_back)
    ImageView back;
    @BindView(R.id.pl_video_texture_view_top_layout_share)
    ImageView share;
    @BindView(R.id.pl_video_texture_view_repeat)
    ImageView repeat;
    @BindView(R.id.pl_video_texture_view_top_layout_drawer)
    ImageView pl_video_texture_view_top_layout_drawer;
    @BindView(R.id.pl_video_texture_view_repeat_drawer_layout)
    DrawerLayout pl_video_texture_view_repeat_drawer_layout;
    @BindView(R.id.pl_video_texture_view_capture)
    ImageView pl_video_texture_view_capture;
    @BindView(R.id.pl_video_texture_view_unlock)
    ImageView pl_video_texture_view_unlock;
    @BindView(R.id.pl_video_texture_view_lock01)
    ImageView pl_video_texture_view_lock01;
    @BindView(R.id.pl_video_texture_view_lock02)
    ImageView pl_video_texture_view_lock02;
    @BindView(R.id.pl_video_texture_view_lock_layout)
    ViewGroup pl_video_texture_view_lock_layout;
    boolean lock_on = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.pl_video_texture_view_bilibili_layout);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
//        MediaController mediaController = new com.example.miogk.myonlinemovie.utilssssss.MediaController(activity);
//        mVideoView.setMediaController(mediaController);
//        mVideoView.setBufferingIndicator(loading_view);
//        initViewsHeight();
        mVideoView.setBufferingIndicator(loading_view);
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN); //原始尺寸
//        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);//适应屏幕
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);//全屏铺满
        //从右侧打开Drawer
        pl_video_texture_view_top_layout_drawer.setOnClickListener(this);
        pl_video_texture_view_capture.setOnClickListener(this);
        pl_video_texture_view_unlock.setOnClickListener(this);
        pl_video_texture_view_lock01.setOnClickListener(this);
        pl_video_texture_view_lock02.setOnClickListener(this);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPosX = event.getX();
                        mPosY = event.getY();
//                        toggle();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentPosX = event.getX();
                        mCurrentPosY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //如果屏幕没有锁住，就正常操作
                        if (!lock_on) {
                            if (pl_video_texture_view_bottom_layout.getVisibility() == View.VISIBLE) {
                                handler.removeCallbacks(hideBottomLayoutRunnable);
                                setViewGone();
                            } else {
                                handler.removeCallbacks(hideBottomLayoutRunnable);
                                setViewVisible();
                                handler.postDelayed(hideBottomLayoutRunnable, 5000);
                            }
                            //如果屏幕锁住了就只显示pl_video_texture_view_lock_layout并5秒后消失
                        } else {
                            if (pl_video_texture_view_lock_layout.getVisibility() == View.VISIBLE) {
                                pl_video_texture_view_lock_layout.setVisibility(View.GONE);
                            } else {
                                pl_video_texture_view_lock_layout.setVisibility(View.VISIBLE);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pl_video_texture_view_lock_layout.setVisibility(View.GONE);
                                    }
                                }, 5000);
                            }
                        }
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
        //截图数据回调
        mVideoView.setOnImageCapturedListener(new PLOnImageCapturedListener() {
            @Override
            public void onImageCaptured(final byte[] bytes) {
                repeat.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(activity).load(bytes).into(repeat);
                    }
                });

            }
        });

        //视频准备完成之后调用
        mVideoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                mVideoView.start();
                //5000毫秒后隐藏控制界面
                play.setImageResource(R.mipmap.pause);
                hideBottomLayoutRunnable = new HideBottomLayoutRunnable();
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断屏幕是否是横屏，是的话关闭播放并退出
                        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            mVideoView.stopPlayback();
                            handler.removeCallbacks(myRunnable);
                            handler.removeCallbacks(hideBottomLayoutRunnable);
                            finish();
                        } else {
                            //是竖屏的话先转化为横屏
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        }
                    }
                });
                handler.postDelayed(hideBottomLayoutRunnable, 5000);
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                            handler.removeCallbacks(myRunnable);
                            handler.removeCallbacks(hideBottomLayoutRunnable);
                            play.setImageResource(R.mipmap.play2);
                            handler.postDelayed(hideBottomLayoutRunnable, 5000);
                        } else {
                            mVideoView.start();
                            play.setImageResource(R.mipmap.pause);
                            handler.removeCallbacks(hideBottomLayoutRunnable);
                            handler.postDelayed(myRunnable, 1000);
                            handler.postDelayed(hideBottomLayoutRunnable, 5000);
                        }
                    }
                });
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
                repeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoView.start();
                        repeat.setVisibility(View.GONE);
                    }
                });
                repeat.setVisibility(View.VISIBLE);
                handler.removeCallbacks(myRunnable);
            }
        });
    }

    private void setViewVisible() {
        pl_video_texture_view_top_layout.setVisibility(View.VISIBLE);
        pl_video_texture_view_bottom_layout.setVisibility(View.VISIBLE);
        pl_video_texture_view_capture.setVisibility(View.VISIBLE);
        pl_video_texture_view_unlock.setVisibility(View.VISIBLE);
    }


    private void setViewGone() {
        pl_video_texture_view_bottom_layout.setVisibility(View.GONE);
        pl_video_texture_view_top_layout.setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            pl_video_texture_view_capture.setVisibility(View.GONE);
            pl_video_texture_view_unlock.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            initViewsHeight();
        }
        super.onConfigurationChanged(newConfig);
    }

    private void initViewsHeight() {
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int height = displayMetrics.heightPixels;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        layoutParams.rightMargin = 15;
        layoutParams.topMargin = height / 4;
        pl_video_texture_view_capture.setLayoutParams(layoutParams);
        layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        layoutParams.rightMargin = 15;
        layoutParams.bottomMargin = height / 4;
        pl_video_texture_view_unlock.setLayoutParams(layoutParams);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pl_video_texture_view_top_layout_drawer:
                pl_video_texture_view_repeat_drawer_layout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.pl_video_texture_view_unlock:
                lock_on = true;
                setViewGone();
                pl_video_texture_view_lock_layout.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pl_video_texture_view_lock_layout.setVisibility(View.GONE);
                    }
                }, 5000);
                break;
            case R.id.pl_video_texture_view_lock01:
            case R.id.pl_video_texture_view_lock02:
                lock_on = false;
                pl_video_texture_view_lock_layout.setVisibility(View.GONE);
                setViewVisible();
                handler.postDelayed(hideBottomLayoutRunnable, 5000);
                break;
            case R.id.pl_video_texture_view_capture:
                long d = mVideoView.getDuration();
                mVideoView.captureImage(d);
                break;
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

    class HideBottomLayoutRunnable implements Runnable {
        @Override
        public void run() {
            setViewGone();
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
            handler.removeCallbacks(myRunnable);
            handler.removeCallbacks(hideBottomLayoutRunnable);
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