package com.example.miogk.myonlinemovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.example.miogk.myonlinemovie.fragment.A;
import com.example.miogk.myonlinemovie.fragment.B;
import com.example.miogk.myonlinemovie.fragment.C;
import com.example.miogk.myonlinemovie.fragment.D;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] names = {"正在热映", "即将上映"};
    @BindView(R.id.spinner)
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ButterKnife.bind(this);
        init();
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());

    }


    private void init() {
        A a = A.newInstance();
        B b = B.newInstance();
        C c = C.newInstance();
        D d = D.newInstance();
        fragments.add(a);
        fragments.add(b);
//        fragments.add(c);
//        fragments.add(d);
    }
}