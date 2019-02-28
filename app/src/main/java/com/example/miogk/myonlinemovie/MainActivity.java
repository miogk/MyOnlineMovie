package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.miogk.myonlinemovie.fragment.A;
import com.example.miogk.myonlinemovie.fragment.B;
import com.example.miogk.myonlinemovie.fragment.CinemaFragment;
import com.example.miogk.myonlinemovie.fragment.MainFragment;
import com.example.miogk.myonlinemovie.fragment.SettingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Activity activity = this;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio_button_movie)
    RadioButton radio_buton_movie;
    @BindView(R.id.radio_button_cinema)
    RadioButton radio_button_cinema;
    @BindView(R.id.radio_button_setting)
    RadioButton radio_button_setting;
    @BindView(R.id.main_activity_view_pager)
    ViewPager main_activity_view_pager;
    //fragment合集
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private AMapLocationClient mLocationClient;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //添加fragments
        MainFragment mainFragment = new MainFragment();
        fragments.add(mainFragment);
        CinemaFragment cinemaFragment = new CinemaFragment();
        fragments.add(cinemaFragment);
        fragments.add(new SettingFragment());
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        //默认设置第一个
        String a = "1000       000 000      00 000 000 00";
        main_activity_view_pager.setCurrentItem(0);
        main_activity_view_pager.setOffscreenPageLimit(fragments.size());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_movie:
                        main_activity_view_pager.setCurrentItem(0, false);
                        break;
                    case R.id.radio_button_cinema:
                        main_activity_view_pager.setCurrentItem(1, false);
//                        Intent intent = new Intent(activity, PersonalInformationActivity.class);
//                        Intent intent = new Intent(activity, GaodeMap.class);
//                        activity.startActivity(intent);
                        break;
                    case R.id.radio_button_setting:
                        main_activity_view_pager.setCurrentItem(2, false);
                        break;
                }
            }
        });
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.pink));
        main_activity_view_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}