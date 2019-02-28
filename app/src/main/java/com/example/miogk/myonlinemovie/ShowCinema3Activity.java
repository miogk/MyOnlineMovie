package com.example.miogk.myonlinemovie;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ShowCinema3Activity extends AppCompatActivity {
    private SpeechSynthesizer speechSynthesizer;
    private static final String TAG = "ShowCinemaActivity";
    private Handler handler = new Handler();
    private AssetManager assetManager;
    private MyTimeRunnable myTimeRunnable;
    private int soundCount = 0;
    private MediaPlayer mediaPlayer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cinema3);
        assetManager = getAssets();
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    Log.e(TAG, "初始化错误,失败码: " + i);
                }
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        long countDownTime = date.getTime();
        Log.e(TAG, "onCreate: " + countDownTime + " -- " + (countDownTime - System.currentTimeMillis()) / 1000 / 60 / 60);
        myTimeRunnable = new MyTimeRunnable();
        handler.postDelayed(myTimeRunnable, (countDownTime - System.currentTimeMillis()));
    }

    class MyTimeRunnable implements Runnable {
        @Override
        public void run() {
            startSpeeking();
        }
    }

    private void startSpeeking() {
        speechSynthesizer.startSpeaking("12点了接财神了!噼里啪啦噼里啪啦放鞭炮了放鞭炮了恭喜发财恭喜发财!", new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                try {
                    AssetFileDescriptor afd = assetManager.openFd("bianpaoyanhua.mp3");
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (soundCount != 2) {
                                mp.start();
                                soundCount++;
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        supportFinishAfterTransition();
        if (speechSynthesizer != null) {
            speechSynthesizer.stopSpeaking();
            speechSynthesizer.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }
}