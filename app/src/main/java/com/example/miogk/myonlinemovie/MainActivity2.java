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

import com.example.miogk.myonlinemovie.fragment.A;
import com.example.miogk.myonlinemovie.fragment.B;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity2 extends AppCompatActivity {
    Activity activity = this;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] names = {"正在热映", "即将上映"};
    private static final String TAG = "MainActivity";
    private String city = "上海";
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    @BindView(R.id.search)
    ImageView search;
    private A a;
    private B b;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio_button_movie)
    RadioButton radio_buton_movie;
    @BindView(R.id.radio_button_cinema)
    RadioButton radio_button_cinema;
    @BindView(R.id.radio_button_setting)
    RadioButton radio_button_setting;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ButterKnife.bind(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_movie:
                        break;
                    case R.id.radio_button_cinema:
                        Intent intent = new Intent(activity, PersonalInformationActivity.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.radio_button_setting:
                        break;
                }
            }
        });
        init();
        Window window = getWindow();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                ActivityCompat.startActivity(activity, new Intent(activity, SearchActivity.class), optionsCompat.toBundle());
            }
        });
        window.setStatusBarColor(getResources().getColor(R.color.pink));
        final String[] cityies = getResources().getStringArray(R.array.spinnerArray);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                city = cityies[position];
//                Log.e(TAG, "onItemSelected: " + city);
//                myFragmentPagerAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }
    }


    private void init() {
        a = A.newInstance(city);
        b = B.newInstance(city);
//        C c = C.newInstance();
//        D d = D.newInstance();
        fragments.add(a);
        fragments.add(b);
//        fragments.add(c);
//        fragments.add(d);
    }
}