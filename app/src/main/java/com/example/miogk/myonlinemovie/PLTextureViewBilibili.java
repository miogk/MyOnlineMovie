package com.example.miogk.myonlinemovie;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnImageCapturedListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class PLTextureViewBilibili extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {
    Activity activity = this;
    private static final String TAG = "PLTEXTUREVIEW";
    @BindView(R.id.PLVideoTextureView)
    PLVideoTextureView mVideoView;
    @BindView(R.id.loading_texture_view)
    ProgressBar loading_view;
    @BindView(R.id.pl_video_texture_view_layout_play)
    ImageView play;
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
    @BindView(R.id.pl_video_texture_view_capture_image_view)
    ImageView pl_video_texture_view_capture_image_view;
    @BindView(R.id.drawer_layout_jingxiang)
    TextView drawer_layout_jingxiang;
    @BindView(R.id.drawer_layout_degree_0)
    RadioButton drawer_layout_degree_0;
    @BindView(R.id.drawer_layout_degree_90)
    RadioButton drawer_layout_degree_90;
    @BindView(R.id.drawer_layout_degree_180)
    RadioButton drawer_layout_degree_180;
    @BindView(R.id.drawer_layout_degree_270)
    RadioButton drawer_layout_degree_270;
    @BindView(R.id.drawer_layout_speed_050)
    RadioButton drawer_layout_speed_050;
    @BindView(R.id.drawer_layout_speed_075)
    RadioButton drawer_layout_speed_075;
    @BindView(R.id.drawer_layout_speed_100)
    RadioButton drawer_layout_speed_100;
    @BindView(R.id.drawer_layout_speed_125)
    RadioButton drawer_layout_speed_125;
    @BindView(R.id.drawer_layout_speed_175)
    RadioButton drawer_layout_speed_175;
    @BindView(R.id.drawer_layout_speed_200)
    RadioButton drawer_layout_speed_200;
    @BindView(R.id.drawer_layout_screen_size_original)
    RadioButton drawer_layout_screen_size_original;
    @BindView(R.id.drawer_layout_screen_size_adjust)
    RadioButton drawer_layout_screen_size_adjust;
    @BindView(R.id.drawer_layout_screen_size_fullscreen)
    RadioButton drawer_layout_screen_size_fullscreen;
    @BindView(R.id.drawer_layout_screen_size_16_9)
    RadioButton drawer_layout_screen_size_16_9;
    @BindView(R.id.drawer_layout_screen_size_4_3)
    RadioButton drawer_layout_screen_size_4_3;
    boolean lock_on = false;
    private boolean is_mirror;
    private boolean is_twice_resume = false;
    private boolean has_complted = false;
    private int screen_width;
    private int screen_height;
    //drawer_layout
    @BindView(R.id.drawer_layout)
    ViewGroup drawer_layout;
    private GestureDetector gestureDetector;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x666) {
//                mVideoView.pause();
//                play.setImageResource(R.mipmap.play2);
                Bundle bundle = msg.getData();
                final byte[] bytes = bundle.getByteArray("bytes");
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                pl_video_texture_view_capture_image_view.setVisibility(View.VISIBLE);
                Glide.with(activity).load(bytes).into(pl_video_texture_view_capture_image_view);
                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                //移动到右上角再上下各30dp
                ObjectAnimator translationX = new ObjectAnimator().ofFloat(pl_video_texture_view_capture_image_view, "translationX", 0, width / 3);
                ObjectAnimator translationY = new ObjectAnimator().ofFloat(pl_video_texture_view_capture_image_view, "translationY", 0, -height / 3);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(translationX, translationY);
                ObjectAnimator translationY2 = new ObjectAnimator().ofFloat(pl_video_texture_view_capture_image_view, "translationY", -height / 3, -height / 3 - 30);
                ObjectAnimator translationY3 = new ObjectAnimator().ofFloat(pl_video_texture_view_capture_image_view, "translationY", -height / 3 - 30, -height / 3 + 30);
                ObjectAnimator translationY4 = new ObjectAnimator().ofFloat(pl_video_texture_view_capture_image_view, "translationY", -height / 3 + 30, -height / 3);
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.setDuration(500);
                animatorSet2.playSequentially(translationY2, translationY3, translationY4);
                AnimatorSet animatorSet3 = new AnimatorSet();
                animatorSet3.playSequentially(animatorSet, animatorSet2);
                animatorSet3.start();
                animatorSet3.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    //动画结束之后3秒把pl_video_texture_view_capture_image_view设置为View.GONE
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        saveBmp2Gallery(bitmap, String.valueOf(System.currentTimeMillis()));
                        pl_video_texture_view_capture_image_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                                mVideoView.pause();
//                                play.setImageResource(R.mipmap.play2);
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                                Intent intent = new Intent(activity, JuzhaoItemActivity.class);
                                intent.putExtra("bytes", bytes);
                                ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                            }
                        });
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pl_video_texture_view_capture_image_view.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }
    };
    private AudioManager mAudioManager;
    private int bright;
    private boolean is_first_time_change_screen_brightness = true;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pl_video_texture_view_bilibili_layout);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        gestureDetector = new GestureDetector(activity, this);
        //判断安卓系统是否是6.0以上
        if (Build.VERSION.SDK_INT >= 23) {
            MyUtils.getAllPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
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
        share.setOnClickListener(this);
        pl_video_texture_view_capture.setOnClickListener(this);
        pl_video_texture_view_unlock.setOnClickListener(this);
        pl_video_texture_view_lock01.setOnClickListener(this);
        pl_video_texture_view_lock02.setOnClickListener(this);
        drawer_layout_jingxiang.setOnClickListener(this);
        drawer_layout_degree_0.setOnClickListener(this);
        drawer_layout_degree_90.setOnClickListener(this);
        drawer_layout_degree_180.setOnClickListener(this);
        drawer_layout_degree_270.setOnClickListener(this);
        drawer_layout_speed_050.setOnClickListener(this);
        drawer_layout_speed_075.setOnClickListener(this);
        drawer_layout_speed_100.setOnClickListener(this);
        drawer_layout_speed_125.setOnClickListener(this);
        drawer_layout_speed_175.setOnClickListener(this);
        drawer_layout_speed_200.setOnClickListener(this);
        drawer_layout_screen_size_original.setOnClickListener(this);
        drawer_layout_screen_size_adjust.setOnClickListener(this);
        drawer_layout_screen_size_fullscreen.setOnClickListener(this);
        drawer_layout_screen_size_16_9.setOnClickListener(this);
        drawer_layout_screen_size_4_3.setOnClickListener(this);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
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
                play.setImageResource(R.mipmap.pause);
                if (repeat.getVisibility() == View.VISIBLE) {
                    repeat.setVisibility(View.GONE);
                }
                //影评播放结束后也需要重新绑定myRunnable
                if (has_complted) {
                    mVideoView.start();
                    handler.postDelayed(myRunnable, 1000);
                    has_complted = false;
                }
                //定位
                int progress = seekBar.getProgress();
                mVideoView.seekTo(progress);
                //暂停的话说明myRunnable已经被取消，需要重新绑定
                if (!mVideoView.isPlaying()) {
                    mVideoView.start();
                    handler.postDelayed(myRunnable, 1000);
                }
            }
        });
        mVideoView.setVideoPath(path);
        //截图数据回调
        mVideoView.setOnImageCapturedListener(new PLOnImageCapturedListener() {
            @Override
            public void onImageCaptured(final byte[] bytes) {
                Message message = new Message();
                message.what = 0x666;
                Bundle bundle = new Bundle();
                bundle.putByteArray("bytes", bytes);
                message.setData(bundle);
                handler.sendMessage(message);
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
//                        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                            //垂直方向，那么切换成水平方向
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                        }
                        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        } else {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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
                has_complted = true;
                play.setImageResource(R.mipmap.play2);
                repeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoView.start();
                        repeat.setVisibility(View.GONE);
                        has_complted = false;
                    }
                });
                repeat.setVisibility(View.VISIBLE);
                handler.removeCallbacks(myRunnable);
            }
        });
    }

    //显示组件
    private void setViewVisible() {
        pl_video_texture_view_top_layout.setVisibility(View.VISIBLE);
        pl_video_texture_view_bottom_layout.setVisibility(View.VISIBLE);
        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            pl_video_texture_view_capture.setVisibility(View.VISIBLE);
            pl_video_texture_view_unlock.setVisibility(View.VISIBLE);
        }
    }

    //隐藏组件
    private void setViewGone() {
        pl_video_texture_view_bottom_layout.setVisibility(View.GONE);
        pl_video_texture_view_top_layout.setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            pl_video_texture_view_capture.setVisibility(View.GONE);
            pl_video_texture_view_unlock.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //每次转屏幕都重新获取屏幕的width和height
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screen_height = displayMetrics.heightPixels;
        screen_width = displayMetrics.widthPixels;
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            pl_video_texture_view_capture.setVisibility(View.VISIBLE);
            pl_video_texture_view_unlock.setVisibility(View.VISIBLE);
            pl_video_texture_view_top_layout_drawer.setVisibility(View.VISIBLE);
            initViewsHeight(screen_width, screen_height);
        } else {
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) share.getLayoutParams();
            layoutParams2.rightMargin = 0;
            pl_video_texture_view_top_layout_drawer.setVisibility(View.GONE);
            pl_video_texture_view_capture_image_view.setVisibility(View.GONE);
            pl_video_texture_view_capture.setVisibility(View.GONE);
            pl_video_texture_view_unlock.setVisibility(View.GONE);
        }
    }

    //设置ViewGroup的一些属性
    private void initViewsHeight(int width, int height) {
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
        //设置drawer_layout宽度为屏幕的1/3
        ViewGroup.LayoutParams layoutParams1 = drawer_layout.getLayoutParams();
        layoutParams1.width = width / 2;
        //设置share MarginRight
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) share.getLayoutParams();
        layoutParams2.rightMargin = 100;
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
                mVideoView.captureImage(100);
                break;
            case R.id.drawer_layout_jingxiang:
                if (!is_mirror) {
                    mVideoView.setMirror(true);
                    is_mirror = true;
                } else {
                    mVideoView.setMirror(false);
                    is_mirror = false;
                }
                break;
            case R.id.drawer_layout_degree_0:
                mVideoView.setDisplayOrientation(0);
                break;
            case R.id.drawer_layout_degree_90:
                mVideoView.setDisplayOrientation(90);
                break;
            case R.id.drawer_layout_degree_180:
                mVideoView.setDisplayOrientation(180);
                break;
            case R.id.drawer_layout_degree_270:
                mVideoView.setDisplayOrientation(270);
                break;
            case R.id.drawer_layout_speed_050:
                mVideoView.setPlaySpeed(0.5f);
                break;
            case R.id.drawer_layout_speed_075:
                mVideoView.setPlaySpeed(0.75f);
                break;
            case R.id.drawer_layout_speed_100:
                mVideoView.setPlaySpeed(1.0f);
                break;
            case R.id.drawer_layout_speed_125:
                mVideoView.setPlaySpeed(1.25f);
                break;
            case R.id.drawer_layout_speed_175:
                mVideoView.setPlaySpeed(1.75f);
                break;
            case R.id.drawer_layout_speed_200:
                mVideoView.setPlaySpeed(2.0f);
                break;
            case R.id.drawer_layout_screen_size_original:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);
                break;
            case R.id.drawer_layout_screen_size_adjust:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
                break;
            case R.id.drawer_layout_screen_size_fullscreen:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
                break;
            case R.id.drawer_layout_screen_size_16_9:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);
                break;
            case R.id.drawer_layout_screen_size_4_3:
                mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_4_3);
                break;
            //分享功能
            case R.id.pl_video_texture_view_top_layout_share:
                showShare();
                break;
            default:
                break;
        }
    }

    //分享功能
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("标题.....");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public boolean onDown(MotionEvent event) {
//        Log.e(TAG, "onDown: " + event);
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
//        float mPosX = 0;
//        float mPosY = 0;
//        float mCurrentPosX = 0;
//        float mCurrentPosY = 0;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPosX = event.getX();
//                mPosY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mCurrentPosX = event.getX();
//                mCurrentPosY = event.getY();
//                //如果在屏幕的左半部分,就增加该窗口的屏幕亮度(不是整个应用的屏幕亮度，只是该窗口)
//                if (mPosX < (screen_width / 2)) {
//                    try {
//                        ContentResolver contentResolver = activity.getContentResolver();
//                        try {
//                            int mode = Settings.System.getInt(contentResolver,
//                                    Settings.System.SCREEN_BRIGHTNESS_MODE);
//                            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
//                                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
//                                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//                            }
//                        } catch (Settings.SettingNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        int bright = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//                        Window window = getWindow();
//                        WindowManager.LayoutParams layoutParams = window.getAttributes();
//                        //如果有滑动并且是向上滑动超过25的
//                        if (mCurrentPosY != 0 && (mPosY - mCurrentPosY) > 25) {
//                            layoutParams.screenBrightness = (mPosY - mCurrentPosY) / 255f;
//                            window.setAttributes(layoutParams);
//                            //如果有滑动并且是向下滑动超过25的
//                        } else if (mCurrentPosY != 0 && (mCurrentPosY - mPosY) > 25) {
//
//                        }
//                    } catch (Settings.SettingNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                //在屏幕右半部分,就增加该窗口的声音
//                if (mPosX >= (screen_width / 2)) {
//
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//
////                        Log.e(TAG, "onTouch:" + mPosX + " -- " + mCurrentPosX + " -- " + mPosY + " --- " + mCurrentPosY);
//                //如果屏幕有滑动则进行判断
//                if (mCurrentPosX != 0 && (mPosX - mCurrentPosX) > 150) {
//                    Log.e(TAG, "向左");
//                    if (!has_complted) {
//                        mVideoView.seekTo(mVideoView.getCurrentPosition() - 5000);
//                    }
//                } else if (mCurrentPosX != 0 && (mCurrentPosX - mPosX) > 150) {
//                    Log.e(TAG, "向右");
//                    if (!has_complted) {
//                        mVideoView.seekTo(mVideoView.getCurrentPosition() + 5000);
//                    }
//                } else if (mCurrentPosY != 0 && (mPosY - mCurrentPosY) > 150) {
//                    //如果在屏幕的左半部分,就增加该窗口的屏幕亮度(不是整个应用的屏幕亮度，只是该窗口)
//                    if (mPosX < (screen_width / 2)) {
//                        try {
//                            int bright = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//                            Window window = getWindow();
//                        } catch (Settings.SettingNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    //在屏幕右半部分,就增加该窗口的声音
//                    if (mPosX >= (screen_width / 2)) {
//
//                    }
//                    Log.e(TAG, "向上");
//                } else if (mCurrentPosY != 0 && (mCurrentPosY - mPosY) > 150) {
//                    //调节屏幕亮度
//                    if (mPosX < (screen_width / 2)) {
//
//                    }
//                    //调节声音
//                    if (mPosX >= (screen_width / 2)) {
//
//                    }
//                    Log.e(TAG, "向下");
//                }
//                mCurrentPosX = 0;
//                mCurrentPosY = 0;
//                break;
//        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//        Log.e(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.e(TAG, "onSingleTapUp: ");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e(TAG, "onScroll: " + e1.getX() + " -- " + e2.getX() + " -- " + distanceX + " -- " + distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.e(TAG, "onLongPress: " + e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //如果X轴上偏移量比较大则调节进度
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            //向右，快进5秒
            if ((e2.getX() - e1.getX()) > 100) {
                if (!has_complted) {
                    Log.e(TAG, "onFling: ");
                    mVideoView.seekTo(mVideoView.getCurrentPosition() + 5000);
                }
                //向左，倒退5秒
            } else if ((e1.getX() - e2.getX()) > 100) {
                if (!has_complted) {
                    mVideoView.seekTo(mVideoView.getCurrentPosition() - 5000);
                }
            }
        }
        if (Math.abs(velocityX) < Math.abs(velocityY)) {
            //只在竖屏的状态下可以调节亮度和音量
            if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                //如果Y轴上偏移量比较大则调节亮度或者音量
                if (Math.abs(velocityY) > Math.abs(velocityX)) {
                    //如果在屏幕的左半边则调节亮度
                    if (e1.getX() < (screen_width / 2)) {
                        try {
                            ContentResolver contentResolver = activity.getContentResolver();
                            int mode = Settings.System.getInt(contentResolver,
                                    Settings.System.SCREEN_BRIGHTNESS_MODE);
                            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                            }
                            //做个标记 -- 如果是第一次调节亮度
                            if (is_first_time_change_screen_brightness) {
                                bright = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                            }
                            Window window = getWindow();
                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                            //如果是向上的,增加亮度
                            if (e2.getY() < e1.getY()) {
                                bright += 50;
                                bright = bright >= 255 ? 255 : bright;
                                layoutParams.screenBrightness = bright / 255f;
                                if (is_first_time_change_screen_brightness) {
                                    is_first_time_change_screen_brightness = false;
                                }
                                //如果是向下的,减小亮度
                            } else if (e1.getY() < e2.getY()) {
                                bright -= 50;
                                bright = bright <= 0 ? 0 : bright;
                                layoutParams.screenBrightness = bright / 255f;
                                if (is_first_time_change_screen_brightness) {
                                    is_first_time_change_screen_brightness = false;
                                }
                            }
                            Log.e(TAG, "onFling: " + bright);
                            window.setAttributes(layoutParams);
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    //在屏幕右半边则调节音量
                    if (e1.getX() > (screen_width / 2)) {
                        //最大音量
                        int maxVolumn = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        //当前音量
                        int currentVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        int a = 0;
                        //如果是向上的,增加音量
                        if (e2.getY() < e1.getY()) {
//                        a = currentVolumn + (int) (Math.abs(velocityY) * 0.001);
                            a = currentVolumn + 5;
                            a = a >= maxVolumn ? maxVolumn : a;
                            a = a <= 0 ? 0 : a;
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, a, AudioManager.FLAG_SHOW_UI);
                        }
                        //如果是向下的,减小音量
                        if (e2.getY() > e1.getY()) {
//                        a = currentVolumn - (int) (Math.abs(velocityY) * 0.001);
                            a = currentVolumn - 5;
                            a = a >= maxVolumn ? maxVolumn : a;
                            a = a <= 0 ? 0 : a;
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, a, AudioManager.FLAG_SHOW_UI);
                        }
                    }
                }
            }
        }
        return true;
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
            is_twice_resume = true;
            Log.e(TAG, "onPause: " + is_twice_resume);
            handler.removeCallbacks(myRunnable);
            play.setImageResource(R.mipmap.play2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        mVideoView.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (is_twice_resume && !has_complted) {
            Log.e(TAG, "onResume: 1111111" + is_twice_resume);
            mVideoView.start();
            play.setImageResource(R.mipmap.pause);
            handler.removeCallbacks(hideBottomLayoutRunnable);
            handler.postDelayed(myRunnable, 1000);
            handler.postDelayed(hideBottomLayoutRunnable, 5000);
        }
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

    //LeeCode题目
    class Solution {
        public String toLowerCase(String str) {
            return str.toLowerCase();
        }
    }

    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public void saveBmp2Gallery(Bitmap bmp, String picName) {
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Screenshots" + File.separator;
        // 声明文件对象
        File file = new File(galleryPath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 声明输出流
        FileOutputStream outStream = null;
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".png");
            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        ContentResolver cr = activity.getContentResolver();
        Uri url = cr.insert(EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        activity.sendBroadcast(intent);
        Toast.makeText(activity, "截图保存成功,图片存放在" + fileName, Toast.LENGTH_SHORT).show();
    }
}