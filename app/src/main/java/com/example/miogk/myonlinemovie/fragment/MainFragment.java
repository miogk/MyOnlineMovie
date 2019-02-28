package com.example.miogk.myonlinemovie.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.miogk.myonlinemovie.R;
import com.example.miogk.myonlinemovie.SearchActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {
    @BindView(R.id.main_fragment_toolbar)
    Toolbar main_fragment_toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] names = {"正在热映", "即将上映"};
    //    @BindView(R.id.spinner)
//    Spinner spinner;
    @BindView(R.id.gaode_city)
    TextView gaode_city;
    private static final String TAG = "MainActivity";
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    @BindView(R.id.search)
    ImageView search;
    private A a;
    private B b;
    private AMapLocationClient mLocationClient;


    private void init() {
//        if (activity.getActionBar() != null) {
//            activity.getActionBar().hide();
//        }
//        activity.setSupportActionBar(main_fragment_toolbar);
        fragments.add(new A());
        fragments.add(new B());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
                ActivityCompat.startActivity(getActivity(), new Intent(getActivity(), SearchActivity.class), optionsCompat.toBundle());
            }
        });
        initAndroidLocation();
        final String[] cityies = getResources().getStringArray(R.array.spinnerArray);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                city = cityies[position];
//                Log.e(TAG, "onItemSelected: " + city);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationClient.stopLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLocationClient.isStarted()) {
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    private void ttt() {
        mLocationClient = new AMapLocationClient(getContext());
        ///获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        AMapLocationClientOption mClientOption = new AMapLocationClientOption();
        mClientOption.setOnceLocationLatest(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式
        //高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
        mClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        gaode_city.setText(city);
                    } else {
                        Log.e(TAG, "onLocationChanged: " + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        mLocationClient.stopLocation();
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ttt();
            } else {
                Toast.makeText(getContext(), "您没有使用此功能的权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initAndroidLocation() {
        //这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_SETTINGS, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            ttt();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationClient.onDestroy();
    }
}