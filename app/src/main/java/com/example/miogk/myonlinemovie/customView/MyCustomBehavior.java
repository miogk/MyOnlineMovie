package com.example.miogk.myonlinemovie.customView;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

public class MyCustomBehavior extends AppBarLayout.ScrollingViewBehavior {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof CollapsingToolbarLayout;
    }
}