package com.example.miogk.myonlinemovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenPhotoActivity extends AppCompatActivity {
    @BindView(R.id.all_screen_view_pager)
    ViewPager all_screen_view_pager;
    private int currentPosition;
    @BindView(R.id.all_screen_photo_radio_group)
    RadioGroup all_screen_photo_radio_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_screen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        final ArrayList<String> photos = (ArrayList<String>) intent.getSerializableExtra("photos");
        for (int i = 0; i < photos.size() - 2; i++) {
            RadioButton radioButton = new RadioButton(this);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioButton.setId(i + 1);
            radioButton.setPadding(0, 0, 5, 0);
            radioButton.setButtonDrawable(R.drawable.all_screen_photo_radio_group_drawable_change);
            all_screen_photo_radio_group.addView(radioButton);
        }
        int position = intent.getIntExtra("position", 0);
        all_screen_view_pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return photos.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
                PhotoView photoView = new PhotoView(FullScreenPhotoActivity.this);
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAfterTransition();
                    }
                });
                Glide.with(FullScreenPhotoActivity.this).load(photos.get(position)).into(photoView);
                container.addView(photoView);
                return photoView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return o == view;
            }
        });
        all_screen_view_pager.setCurrentItem(position);
        all_screen_photo_radio_group.check(position);
        all_screen_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                all_screen_photo_radio_group.check(i);
                currentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (currentPosition == photos.size() - 1) {
                        currentPosition = 1;
                        all_screen_view_pager.setCurrentItem(currentPosition, false);
                    } else if (currentPosition == 0) {
                        currentPosition = photos.size() - 2;
                        all_screen_view_pager.setCurrentItem(currentPosition, false);
                    } else {
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }
}