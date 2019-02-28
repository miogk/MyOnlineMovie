package com.example.miogk.myonlinemovie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.miogk.myonlinemovie.customView.SeatTable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowCinemaActivity extends AppCompatActivity{
    private Activity activity = this;
//    private static final String TAG = "ShowCinemaActivity";
//    @BindView(R.id.show_cinema_root)
//    LinearLayout root;
//    @BindView(R.id.show_cinema_l)
//    LinearLayout show_cinema_l;
//    @BindView(R.id.show_cinema_2)
//    LinearLayout show_cinema_2;
//    @BindView(R.id.show_cinema_3)
//    LinearLayout show_cinema_3;
//    @BindView(R.id.show_cinema_4)
//    LinearLayout show_cinema_4;
//    @BindView(R.id.show_cinema_5)
//    LinearLayout show_cinema_5;
//    @BindView(R.id.show_cinema_6)
//    LinearLayout show_cinema_6;
//    @BindView(R.id.show_cinema_7)
//    LinearLayout show_cinema_7;
//    @BindView(R.id.show_cinema_8)
//    LinearLayout show_cinema_8;
//    @BindView(R.id.show_cinema_9)
//    LinearLayout show_cinema_9;
//    @BindView(R.id.show_cinema_l0)
//    LinearLayout show_cinema_l0;
//    private ScaleGestureDetector scaleGestureDetector;
//    @BindView(R.id.show_cinema_all)
//    LinearLayout show_cinema_all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cinema3);
        SeatTable view = findViewById(R.id.seat_table);
        view.setData(3,10);
        view.setMaxSelected(10);
        view.setScreenName("miogk");
//        ButterKnife.bind(this);
//        scaleGestureDetector = new ScaleGestureDetector(this, this);
//        for (int i = 0; i < show_cinema_all.getChildCount(); i++) {
//            for (int j = 0; j < 10; j++) {
//                final ImageView imageView = new ImageView(activity);
//                imageView.setPadding(2, 0, 2, 0);
//                imageView.setTag(i + "" + j);
//                imageView.setImageResource(R.mipmap.seat_empty);
//                ((LinearLayout) show_cinema_all.getChildAt(i)).addView(imageView);
//                imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        switch (event.getActionMasked()) {
//                            case MotionEvent.ACTION_DOWN:
//                                imageView.setImageResource(R.mipmap.seat_full);
//                                Log.e(TAG, "onTouch: " + imageView.getTag());
//                                break;
//                        }
//                        return true;
//                    }
//                });
//            }
//        }
    }

}