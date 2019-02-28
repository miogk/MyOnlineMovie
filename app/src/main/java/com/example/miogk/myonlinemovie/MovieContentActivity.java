package com.example.miogk.myonlinemovie;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.miogk.myonlinemovie.customView.MyCustomView;
import com.example.miogk.myonlinemovie.domain.AllReviews;
import com.example.miogk.myonlinemovie.domain.HotMovieContent;
import com.example.miogk.myonlinemovie.domain.PersonalInformation;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.example.miogk.myonlinemovie.utilssssss.MySqliteDatabaseHelper;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import service.ApiService;

/**
 *
 */
public class MovieContentActivity extends AppCompatActivity {
    private Activity activity = this;
    private static final String TAG = "MovieContentActivity";
    private Retrofit retrofit;
    @BindView(R.id.coordinator_layout)
    ViewGroup coordinator_layout;
    @BindView(R.id.movie_content_view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.movie_content2_back)
    ImageButton back;
    @BindView(R.id.collapsing_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.activity_movie_conten2_image_view)
    ImageView activity_movie_conten2_image_view;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.original_title)
    TextView original_title;
    @BindView(R.id.tool_bar_movie_name)
    TextView tool_bar_movie_name;
    @BindView(R.id.movie_type)
    TextView movie_type;
    @BindView(R.id.movie_shangying)
    TextView movie_shangying;
    @BindView(R.id.movie_wish)
    TextView wish;
    @BindView(R.id.movie_content2_starts)
    TextView stars;
    @BindView(R.id.movie_content2_rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.movie_content2_rating_count)
    TextView rating_count;
    @BindView(R.id.baifenbi_content)
    ViewGroup content_baifenbi;
    @BindView(R.id.first_left_layout)
    ViewGroup first_left_layout;
    @BindView(R.id.summary)
    TextView summary;
    @BindView(R.id.director_actor_recycler_view)
    RecyclerView director_actor_recycler_view;
    @BindView(R.id.number_of_picture)
    TextView photoCount;
    @BindView(R.id.recycler_view_pictures)
    RecyclerView recycler_view_pictures;
    @BindView(R.id.total_photos)
    TextView total_photos;
    @BindView(R.id.progress_bar_layout)
    ViewGroup progress_bar_layout;
    @BindView(R.id.recycler_view_trailers_clips)
    RecyclerView recycler_view_trailers_clips;
    @BindView(R.id.total_trailers_clips)
    TextView total_trailers_clips;
    int currentPosition = 1;
    @BindView(R.id.popular_comments_recycler_view)
    RecyclerView popular_comments_recycler_view;
    //总短评数
    @BindView(R.id.total_comments_count)
    TextView total_comments_count;
    @BindView(R.id.popular_reviews_recycler_view)
    RecyclerView popular_reviews_recycler_view;
    @BindView(R.id.total_reviews_count)
    TextView total_reviews_count;
    private String movieTitle;
    @BindView(R.id.yanzhi_renyuan)
    View yanzhi_renyuan;
    @BindView(R.id.duanping)
    View duanping;
    @BindView(R.id.yingping)
    View yingping;
    @BindView(R.id.loading_progress_loading_anim)
    ViewGroup loading_progress_loading_anim;
    @BindView(R.id.loading_progress_try_again)
    ViewGroup loading_progress_try_again;
    @BindView(R.id.buy_ticket_button)
    Button buy_ticket_button;
    @BindView(R.id.floating_action_button)
    FloatingActionButton floating_action_button;
    @BindView(R.id.want_to_see)
    RadioButton want_to_see;
    @BindView(R.id.have_seen)
    RadioButton have_seen;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    //    private String url = "https://movie.douban.com/subject/26394152/mobile";
    private ArrayList images = new ArrayList(Arrays.asList(R.drawable.p2541662397, R.drawable.p2541662397, R.drawable.p2541662397));
    private ArrayList<String> photos = new ArrayList<>();
    private MyAdapter myAdapter;
    private String movieId;
    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                //每隔10秒自动滑动
//                timer.schedule(timerTask, 10000, 10000);
            }
        }
    };
    private TimerTask timerTask;
    Timer timer = new Timer();
    private String movieImageUrl;
    private String movieImageUrl1;
    private MySqliteDatabaseHelper mSqliteDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private SharedPreferences mSharedPreferences;

    private void getWantToSeeOrHaveSeen(String username) {
        String sql = "select want_to_see, have_seen from user where username = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            //是否有保存过这部电影的want_to_see
            String wts = cursor.getString(cursor.getColumnIndex("want_to_see"));
            if (!TextUtils.isEmpty(wts)) {
                if (wts.contains(movieId)) {
                    want_to_see.setChecked(true);
                    want_to_see.setClickable(false);
                }
            }
            String hs = cursor.getString(cursor.getColumnIndex("have_seen"));
            if (!TextUtils.isEmpty(hs)) {
                //是否有保存过这部电影的
                if (hs.contains(movieId)) {
                    have_seen.setChecked(true);
                    have_seen.setClickable(false);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_content2);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        Log.e(TAG, "onCreate:   " + movieId);
        movieImageUrl1 = intent.getStringExtra("movieImageUrl");
        Log.e(TAG, "onCreate: " + getPackageResourcePath());
        mSqliteDatabaseHelper = new MySqliteDatabaseHelper(this);
        mDatabase = mSqliteDatabaseHelper.getReadableDatabase();
        mSharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = mSharedPreferences.getString("username", "");
        if (!TextUtils.isEmpty(username)) {
            getWantToSeeOrHaveSeen(username);
        }
        //全屏切换高度不一致修复
        MyUtils.initStatusBar(R.color.white, this);
        //利用反射修改ViewPager自动滑动速度
//        fixedSpeedOfViewPager(viewPager);
        MyUtils.statusBarBackgroundColor(getWindow(), this, R.color.white);
        //小米的官方黑色字体设置
        MyUtils.MIUISetStatusBarLightMode(this, true);
        MyUtils.statusBarLightModeInAndroid(this);
        want_to_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断是否登录
                if (TextUtils.isEmpty(username)) {
                    want_to_see.setChecked(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("用户还未登录,请先登录");
                    final AlertDialog dialog = builder.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            dialog.cancel();
                            startActivity(intent);
                        }
                    }, 1500);
                } else {
                    //用户已登录,并且之前没有have_seen看过这影片.
                    if (!have_seen.isChecked()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("您是想看" + movieTitle + "吗?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues contentValues = new ContentValues();
                                Cursor cursor = null;
                                try {
                                    cursor = mDatabase.rawQuery("select want_to_see, have_seen from user where username = ?", new String[]{username});
                                    //查询之前有没有want_to_see或者_have_seen
                                    if (cursor.moveToFirst()) {
                                        String wts = cursor.getString(cursor.getColumnIndex("want_to_see"));
                                        String hs = cursor.getString(cursor.getColumnIndex("have_seen"));
                                        if (TextUtils.isEmpty(wts)) {
                                            wts = movieId;
                                        } else {
                                            if (!wts.contains(movieId)) {
                                                wts = wts + ":" + movieId;
                                            }
                                        }
                                        contentValues.put("want_to_see", wts);
                                        mDatabase.update("user", contentValues, "username = ?", new String[]{username});
                                        //如果已经看过这影片，就用""替换,然后update.
                                        if (hs.contains(movieId)) {
                                            if (hs.contains(":" + movieId)) {
                                                hs = hs.replace(":" + movieId, "");
                                            } else {
                                                hs = hs.replace(movieId, "");
                                            }
                                            contentValues.clear();
                                            contentValues.put("have_seen", hs);
                                            mDatabase.update("user", contentValues, "username = ?", new String[]{username});
                                        }
                                        want_to_see.setClickable(false);
                                    } else {
                                        Log.e(TAG, "onClick: want_to_see is null");
                                    }
                                } finally {
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                }
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                want_to_see.setChecked(false);
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
        have_seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果用户没有登录
                if (TextUtils.isEmpty(username)) {
                    have_seen.setChecked(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("用户还未登录,请先登录");
                    final AlertDialog dialog = builder.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(activity, LoginActivity.class);
                            dialog.cancel();
                            startActivity(intent);
                        }
                    }, 1500);
                    //如果有登录
                } else {
                    if (have_seen.isChecked()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("您是已经看了" + movieTitle + "吗?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues contentValues = new ContentValues();
                                Cursor cursor = mDatabase.rawQuery("select want_to_see, have_seen from user where username = ?", new String[]{username});
                                //有此column的话
                                if (cursor.moveToNext()) {
                                    String wts = cursor.getColumnName(cursor.getColumnIndex("want_to_see"));
                                    String hs = cursor.getString(cursor.getColumnIndex("have_seen"));
                                    if (TextUtils.isEmpty(hs)) {
                                        hs = movieId;
                                    } else {
                                        hs = hs + ":" + movieId;
                                    }
                                    contentValues.put("have_seen", hs);
                                    mDatabase.update("user", contentValues, "username = ?", new String[]{username});
                                    have_seen.setClickable(false);
                                    //如果之前看过have_seen这影片,判断是否是第一个movieId或不是第一个:movieId并用""替换,然后update.
                                    if (wts.contains(movieId)) {
                                        if (wts.contains(":" + movieId)) {
                                            wts = wts.replace(":" + movieId, "");
                                        } else {
                                            wts = wts.replace(movieId, "");
                                        }
                                        contentValues.clear();
                                        contentValues.put("want_to_see", wts);
                                        mDatabase.update("user", contentValues, "username = ?", new String[]{username});
                                    }
                                } else {
                                    Log.e(TAG, "onClick: have_seen is null");
                                }
                            }
                        });
                        builder.show();
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                have_seen.setChecked(false);
                                dialog.cancel();
                            }
                        });
                    }
                }
            }
        });
        buy_ticket_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                Intent intent = new Intent(activity, ShowCinemaActivity.class);
                intent.putExtra("movieTitle", movieTitle);
                ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
            }
        });
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(true);
                nestedScrollView.scrollTo(0, 0);
            }
        });
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setSupportActionBar(toolbar);
        //利用反射注册tab点击事件
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) {
                break;
            }
            try {
                Class c = tab.getClass();
                Field field = c.getDeclaredField("view");
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) {
                    break;
                }
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        switch (position) {
                            case 0:
                                appBarLayout.setExpanded(true, true);
                                nestedScrollView.scrollTo(0, 0);
                                break;
                            case 1:
                                appBarLayout.setExpanded(false, false);
                                nestedScrollView.smoothScrollTo(0, (int) (yanzhi_renyuan.getY() + toolbar.getHeight() * 2 - 17));
                                break;
                            case 2:
                                appBarLayout.setExpanded(false, false);
                                nestedScrollView.smoothScrollTo(0, (int) (duanping.getY() + toolbar.getHeight() * 2 - 17));
                                break;
                            case 3:
                                appBarLayout.setExpanded(false, false);
                                nestedScrollView.smoothScrollTo(0, (int) (yingping.getY() + toolbar.getHeight() * 2 - 17));
                                break;
                        }
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //nestedScrollView滑动事件，对应tablayoutItem的selected
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.e(TAG, "onScrollChange: " + oldScrollY + " --- " + scrollY + " -- " + (yingping.getY() + toolbar.getHeight() * 2));
                if (scrollY < (yanzhi_renyuan.getY() + toolbar.getHeight() * 2 - 17)) {
                    if (!tabLayout.getTabAt(0).isSelected()) {
                        tabLayout.getTabAt(0).select();
                    }
                    if (floating_action_button.isOrWillBeShown()) {
                        floating_action_button.hide();
                    }
                } else if ((yanzhi_renyuan.getY() + toolbar.getHeight() * 2 - 17) <= scrollY && scrollY < (duanping.getY() + toolbar.getHeight() * 2 - 17)) {
                    if (!tabLayout.getTabAt(1).isSelected()) {
                        tabLayout.getTabAt(1).select();
                    }
                    if (floating_action_button.isOrWillBeShown()) {
                        floating_action_button.hide();
                    }
                } else if ((duanping.getY() + toolbar.getHeight() * 2 - 17) <= scrollY && scrollY < (yingping.getY() + toolbar.getHeight() * 2 - 17)) {
                    if (!tabLayout.getTabAt(2).isSelected()) {
                        tabLayout.getTabAt(2).select();
                    }
                    if (floating_action_button.isOrWillBeHidden()) {
                        floating_action_button.show();
                        //没有滑动的话3秒后自动消失
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                floating_action_button.hide();
                            }
                        }, 3000);
                    }
                } else {
                    if (!tabLayout.getTabAt(3).isSelected()) {
                        tabLayout.getTabAt(3).select();
                    }
                    if (floating_action_button.isOrWillBeHidden()) {
                        floating_action_button.show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                floating_action_button.hide();
                            }
                        }, 3000);
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //根据appBarLayout的偏移量来改变toolbar背景的透明度
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    toolbar.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                }
                toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white), Math.abs(i * 1.0f) / appBarLayout.getTotalScrollRange()));
            }
        });

//        Glide.with(this).load(movieImageUrl1).into(activity_movie_conten2_image_view);
        getDataFromNet(movieId);
    }

//调节ViewPager滑动速度
    //    private void fixedSpeedOfViewPager(ViewPager viewPager) {
//        try {
//            Field field = viewPager.getClass().getDeclaredField("mScroller");
//            MyScroll myScroll = new MyScroll(this, new LinearOutSlowInInterpolator());
////            myScroll.setDuration(6000);
//            field.setAccessible(true);
//            field.set(viewPager, myScroll);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    //渐变背景色
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    private void getDataFromNet(final String movieId) {
//        Glide.with(this).load(movieImageUrl1).into(activity_movie_conten2_image_view);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantUtils.BASE_URL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<HotMovieContent> observable = apiService.getHotMovieContentWithRxJava(movieId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotMovieContent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(final HotMovieContent o) {
                        final ArrayList<HotMovieContent.Photos> photos2 = o.photos;
                        photoCount.setText("1/" + photos2.size());
                        for (HotMovieContent.Photos hcp : photos2) {
                            photos.add(hcp.image);
                        }
                        //头尾各加一张图片比如原来是ArryList(1,2,3),加完之后成ArrayList(3,1,2,3,1),初始定位到index=1;
                        photos.add(0, photos2.get(photos2.size() - 1).image);
                        photos.add(photos2.get(0).image);
                        myAdapter = new MyAdapter(photos);
                        viewPager.setAdapter(myAdapter);
                        viewPager.setCurrentItem(1);
                        //定时runnable
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentPosition++;
                                        viewPager.setCurrentItem(currentPosition);
                                    }
                                });
                            }
                        };

                        //传回主线程
//                        handler.sendEmptyMessage(0x123);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageScrolled(int i, float v, int i1) {

                            }

                            @Override
                            public void onPageSelected(int i) {
                                currentPosition = i;
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                Log.e(TAG, "onPageScrollStateChanged: " + state);
                                if (state == ViewPager.SCROLL_STATE_IDLE) {
                                    if (currentPosition == photos.size() - 1) {
                                        currentPosition = 1;
                                        viewPager.setCurrentItem(currentPosition, false);
                                        photoCount.setText(1 + "/" + photos2.size());
                                    } else if (currentPosition == 0) {
                                        currentPosition = photos.size() - 2;
                                        viewPager.setCurrentItem(currentPosition, false);
                                        photoCount.setText(photos2.size() + "/" + photos2.size());
                                    } else {
                                        photoCount.setText(currentPosition + "/" + photos2.size());
                                    }
                                }
                            }
                        });
                        coordinator_layout.setVisibility(View.VISIBLE);
                        //电影类型、国家、时长等
                        ArrayList<String> gs = o.genres;
                        StringBuilder sb = new StringBuilder();
                        for (String g : gs) {
                            sb.append(g + " / ");
                        }
                        sb.append(o.countries.get(0));
                        sb.append(" / " + o.languages.get(0));
                        if (o.durations.size() != 0) {
                            sb.append(" / " + o.durations.get(0));
                        }

                        //上映时间
                        ArrayList<String> pubdates = o.pubdates;
                        for (String p : pubdates) {
                            if (p.contains("中国")) {
                                movie_shangying.setText(p);
                            }
                        }
                        first_left_layout.setVisibility(View.VISIBLE);
                        wish.setText(o.wish_count + "人想看");
                        movie_type.setText(sb.toString());
                        movieTitle = o.title;
                        title.setText(o.title);
                        tool_bar_movie_name.setText(o.title);
                        original_title.setText(o.original_title);
                        //获取评分和星级
                        stars.setText(o.rating.average);
                        rating_count.setText(o.ratings_count + "人评价");
                        ratingBar.setVisibility(View.VISIBLE);
                        ratingBar.setRating(Float.parseFloat(o.rating.stars) / 10);

                        //第一排右侧的评分百分比的显示
                        calculateBaifenbi(o);
                        //电影简介内容

                        summary.setText(o.summary);
                        getDirectorAndActor(o);
                        getPictures(o);
                        //预告片花絮片段
                        getTrailersClips(o);
                        //获取热门短评
                        getPopularComments(o);
                        //获取长篇影评
                        getPopularReviews();
                        total_reviews_count.setText("更多" + o.reviews_count + "条影评");
                        Glide.with(activity).load(o.images.large).into(activity_movie_conten2_image_view);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Calendar calendar = Calendar.getInstance();
                        long time = calendar.getTime().getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        String formatTime = simpleDateFormat.format(time);
                        MyUtils.putInSharedPreferences(activity, "errordata", formatTime, e.toString());
                        loading_progress_try_again.setVisibility(View.VISIBLE);
                        loading_progress_loading_anim.setVisibility(View.GONE);
                        loading_progress_try_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loading_progress_try_again.setVisibility(View.GONE);
                                loading_progress_loading_anim.setVisibility(View.VISIBLE);
                                getDataFromNet(movieId);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        if (progress_bar_layout != null) {
                            progress_bar_layout.setVisibility(View.GONE);
                            loading_progress_try_again.setVisibility(View.GONE);
                        }
                        Log.e(TAG, "onComplete: ");
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                floating_action_button.hide();
//                            }
//                        }, 3000);
                    }
                });
    }

    //热门影评
    private void getPopularReviews() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<AllReviews> reviewsObservable = service.getAllReviews(movieId, "0", "5");
        reviewsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllReviews>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllReviews allReviews) {
                        ArrayList<AllReviews.Review> reviews = allReviews.reviews;
                        popular_reviews_recycler_view.setLayoutManager(new LinearLayoutManager(activity));
                        MyAdapter6 myAdapter6 = new MyAdapter6(reviews);
                        popular_reviews_recycler_view.setAdapter(myAdapter6);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        buy_ticket_button.setVisibility(View.VISIBLE);
                    }
                });
    }

    //热门短评
    private void getPopularComments(HotMovieContent o) {
        ArrayList<HotMovieContent.Comments> comments = o.popular_comments;
        MyAdapter5 myAdapter5 = new MyAdapter5(comments);
        popular_comments_recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        popular_comments_recycler_view.setAdapter(myAdapter5);
        total_comments_count.setText("更多" + o.comments_count + "条短评");
        total_comments_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                Intent intent = new Intent(activity, AllCommentsActivity.class);
                intent.putExtra("movieTitle", movieTitle);
                intent.putExtra("movieId", movieId);
                ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
            }
        });
    }


    //更多预告片,花絮视频
    private void getTrailersClips(final HotMovieContent o) {
        final ArrayList<HotMovieContent.Trailers> trailers = o.trailers;
        final ArrayList<HotMovieContent.Bloopers> bloopers = o.bloopers;
        final ArrayList<HotMovieContent.Clips> clips = o.clips;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view_trailers_clips.setLayoutManager(linearLayoutManager);
        recycler_view_trailers_clips.setAdapter(new MyAdapter4(trailers));
        total_trailers_clips.setText("更多" + (trailers.size() + bloopers.size() + clips.size()) + "个视频");
        total_trailers_clips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieContentActivity.this);
                Intent intent = new Intent(activity, ShowAllTrailersClips.class);
                intent.putExtra("trailers", trailers);
                intent.putExtra("bloopers", bloopers);
                intent.putExtra("clips", clips);
                intent.putExtra("movieTitle", movieTitle);
                ActivityCompat.startActivity(MovieContentActivity.this, intent, optionsCompat.toBundle());
//                startActivity(intent);
            }
        });
    }

    //剧照
    private void getPictures(final HotMovieContent o) {
        ArrayList<HotMovieContent.Photos> photos = o.photos;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MyAdapter3 myAdapter3 = new MyAdapter3(photos);
        recycler_view_pictures.setLayoutManager(linearLayoutManager);
        recycler_view_pictures.setAdapter(myAdapter3);
        total_photos.setText("更多" + o.photos_count + "张剧照");
        total_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieContentActivity.this);
                Intent intent = new Intent(activity, ShowPhotosActivity.class);
                intent.putExtra("movieId", movieId);
                intent.putExtra("photo_count", o.photos_count);
                intent.putExtra("movieTitle", movieTitle);
                ActivityCompat.startActivity(MovieContentActivity.this, intent, optionsCompat.toBundle());
//                startActivity(intent);
            }
        });
    }


    //获取导演和演员的数据并用横向的RecyclerView排列
    private void getDirectorAndActor(HotMovieContent o) {
        //判断是否有导演的flag
        boolean has_director = true;
        ArrayList<HotMovieContent.Casts> directors = o.directors;
        ArrayList<HotMovieContent.Casts> casts = o.casts;
        ArrayList<HotMovieContent.Casts> all = new ArrayList<>();
        //没有导演就设置为false
        if (directors.size() == 0) {
            has_director = false;
        }
        all.addAll(directors);
        all.addAll(casts);
        MyAdapter2 myAdapter2 = new MyAdapter2(all, has_director);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        director_actor_recycler_view.setLayoutManager(layoutManager);
        director_actor_recycler_view.setAdapter(myAdapter2);
    }

    //计算评分百分比，倒叙排列.
    private void calculateBaifenbi(HotMovieContent o) {
        HashMap<String, Float> details = o.rating.details;
//      float a = 123.2334f;
//      float b = (float) (Math.round(a * 100)) / 100;(这里的100就是2位小数点, 如果要其它位, 如4位, 这里两个100改成10000)
        Set<String> set = details.keySet();
        TreeSet<String> s = new TreeSet(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        s.addAll(set);
        float wu = details.get("5");
        float si = details.get("4");
        float san = details.get("3");
        float er = details.get("2");
        float yi = details.get("1");
        float total = wu + si + san + er + yi;
        for (String next : s) {
            float ji = details.get(next);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(layoutParams);
            TextView tv = new TextView(this);
            tv.setText(next + "星  ");
            linearLayout.addView(tv);
            float num = ji / total * 400f;
            float num2 = (float) (Math.round(ji / total * 100 * 10)) / 10;
            MyCustomView myCustomView = new MyCustomView(this, num);
            linearLayout.addView(myCustomView);
            TextView baifenbi_tv = new TextView(this);
            baifenbi_tv.setText("  " + num2 + "%");
            linearLayout.addView(baifenbi_tv);
            content_baifenbi.addView(linearLayout);
        }
    }

    //ViewPager的数据
    class MyAdapter extends PagerAdapter {
        ArrayList<String> photos;

        MyAdapter(ArrayList photos) {
            this.photos = photos;
        }

        @Override
        public int getCount() {
            return this.photos.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            Log.e(TAG, "instantiateItem: " + position);
            final ImageView imageView = new ImageView(activity);
            imageView.setBackground(null);
            imageView.setTransitionName("all_screen_photo_image_view");
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(activity).load(photos.get(position)).into(imageView);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //从一个角到另一个角
//                    Animator animator2 = ViewAnimationUtils.createCircularReveal(imageView, 0, 0, 0,
//                            (float) Math.hypot(imageView.getWidth(), imageView.getHeight()));
                    //从中间扩散到四周
//                    Animator animator2 = ViewAnimationUtils.createCircularReveal(imageView, imageView.getWidth() / 2, imageView.getHeight() / 2
//                            , 0, imageView.getHeight());
//                    animator2.setDuration(2000);
//                    animator2.start();
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, FullScreenPhotoActivity.class);
                    intent.putExtra("url", photos.get(position));
                    intent.putExtra("photos", photos);
                    intent.putExtra("position", position);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            Log.e(TAG, "destroyItem: " + position);
            container.removeView((ImageView) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    //演职人员
    class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {
        ArrayList<HotMovieContent.Casts> o;
        boolean hasDirector;

        MyAdapter2(ArrayList<HotMovieContent.Casts> o, boolean hasDirector) {
            this.hasDirector = hasDirector;
            this.o = o;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.director_actor_layout, null, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final HotMovieContent.Casts c = o.get(i);
            ImageView director_actor_image_view = myViewHolder.director_actor_image_view;
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.ic_launcher);
            String castId = c.id;
            if (c.avatars != null) {
                Glide.with(activity).load(c.avatars.small).apply(requestOptions).into(director_actor_image_view);
            }
            myViewHolder.name.setText(c.name);
            myViewHolder.name_en.setText(c.name_en);
            Log.e(TAG, "onBindViewHolder:" + c.name + " -- " + castId);
            if ((i == 0) && hasDirector) {
                myViewHolder.job.setText("导演");
            } else {
                myViewHolder.job.setText("主演");
            }
            director_actor_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: " + c.name);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, PersonalInformationActivity.class);
                    intent.putExtra("id", c.id);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return o.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.director_actor_image_view)
            ImageView director_actor_image_view;
            @BindView(R.id.director_actor_name_en)
            TextView name_en;
            @BindView(R.id.director_actor_name)
            TextView name;
            @BindView(R.id.director_actor_job)
            TextView job;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //剧照
    class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder> {
        ArrayList<HotMovieContent.Photos> photos;

        MyAdapter3(ArrayList<HotMovieContent.Photos> photos) {
            this.photos = photos;
        }

        @NonNull
        @Override
        public MyAdapter3.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.recycler_view_pictures_layout, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyAdapter3.MyViewHolder viewHolder, int i) {
            final HotMovieContent.Photos photo = photos.get(i);
            final RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_launcher);
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieContentActivity.this,
                            viewHolder.image, "photo123");
                    ActivityCompat.startActivity(activity,
                            new Intent(activity, JuzhaoItemActivity.class).putExtra("url", photo.thumb),
                            optionsCompat.toBundle());
                }
            });
            Glide.with(activity).load(photo.thumb).apply(options).into(viewHolder.image);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.recycler_view_pictures_layout_image)
            ImageView image;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //预告花絮片段
    class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.MyViewHolder> {
        ArrayList<HotMovieContent.Trailers> trailers;

        MyAdapter4(ArrayList<HotMovieContent.Trailers> trailers) {
            this.trailers = trailers;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.recycler_view_trailers_clips_layout, null, false);
            MyViewHolder mViewHolder = new MyViewHolder(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final HotMovieContent.Trailers t = trailers.get(i);
            final String path = t.resource_url;
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, PLTextureViewBilibili.class);
                    intent.putExtra("path", path);
                    intent.putExtra("movieTitle", movieTitle);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            Glide.with(activity).load(t.medium).into(myViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return trailers.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.recycler_view_trailers_clips_image_view);
            }
        }
    }

    //短评
    class MyAdapter5 extends RecyclerView.Adapter<MyAdapter5.MyViewHolder> {
        ArrayList<HotMovieContent.Comments> comments;

        MyAdapter5(ArrayList<HotMovieContent.Comments> comments) {
            this.comments = comments;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.popular_comments_layout, null, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            HotMovieContent.Comments comment = comments.get(i);
            String userful_count = comment.useful_count;
            String content = comment.content;
            String created_at = comment.created_at;
            HotMovieContent.Comments.Rating rating = comment.rating;
            String value = rating.value;
            HotMovieContent.Comments.Author author = comment.author;
            String author_name = author.name;
            myViewHolder.author_rating_bar.setRating(Float.parseFloat(value));
            myViewHolder.author_name.setText(author_name);
            myViewHolder.created_at.setText(created_at);
            myViewHolder.author_userful_count.setText(userful_count);
            myViewHolder.author_comment_content.setText(content);
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.author_name)
            TextView author_name;
            @BindView(R.id.author_rating_bar)
            RatingBar author_rating_bar;
            @BindView(R.id.author_created_at)
            TextView created_at;
            @BindView(R.id.author_comment_content)
            TextView author_comment_content;
            @BindView(R.id.author_userful_count)
            TextView author_userful_count;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //长篇影评
    class MyAdapter6 extends RecyclerView.Adapter<MyAdapter6.MyViewHolder6> {
        ArrayList<AllReviews.Review> reviews;

        MyAdapter6(ArrayList<AllReviews.Review> reviews) {
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public MyViewHolder6 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.popular_reviews_layout, null, false);
            return new MyViewHolder6(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder6 myViewHolder6, int i) {
            AllReviews.Review review = reviews.get(i);
            AllReviews.Review.Author author = review.author;
            AllReviews.Review.Rating rating = review.rating;
            String avatar = author.avatar;
            final String name = author.name;
            String value = rating.value;
            String created_at = review.created_at;
            String title = review.title;
            String useful = review.useful_count;
            String useless = review.useless_count;
            String summary = review.summary;
            final String reviewId = review.id;
            Glide.with(activity).load(avatar).into(myViewHolder6.popular_reviews_image_view);
            myViewHolder6.popular_reviews_name.setText(name);
            myViewHolder6.popular_reviews_rating_bar.setRating(Float.parseFloat(value));
            myViewHolder6.popular_reviews_created_at.setText(created_at);
            myViewHolder6.popular_reviews_title.setText(title);
            myViewHolder6.popular_reviews_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, SingleReviewActivity.class);
                    //传入影评ID和评论者
                    intent.putExtra("reviewId", reviewId);
                    intent.putExtra("author_name", name);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            //富文本SpannableString
            SpannableString more = new SpannableString(summary + "更多");
            //可以点击的富文本
            more.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, SingleReviewActivity.class);
                    //传入影评ID和评论者
                    intent.putExtra("reviewId", reviewId);
                    intent.putExtra("author_name", name);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }

                //设置更多属性，比如设置字体颜色
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(R.color.yellow));
                }
            }, more.length() - 2, more.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            myViewHolder6.popular_reviews_summary.setMovementMethod(LinkMovementMethod.getInstance());
            myViewHolder6.popular_reviews_summary.setText(more);
            myViewHolder6.popular_reviews_useful.setText(useful);
            myViewHolder6.popular_reviews_useless.setText(useless);
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        class MyViewHolder6 extends RecyclerView.ViewHolder {
            @BindView(R.id.popular_reviews_image_view)
            CircleImageView popular_reviews_image_view;
            @BindView(R.id.popular_reviews_name)
            TextView popular_reviews_name;
            @BindView(R.id.popular_reviews_rating_bar)
            RatingBar popular_reviews_rating_bar;
            @BindView(R.id.popular_reviews_title)
            TextView popular_reviews_title;
            @BindView(R.id.popular_reviews_summary)
            TextView popular_reviews_summary;
            @BindView(R.id.popular_reviews_created_at)
            TextView popular_reviews_created_at;
            @BindView(R.id.popular_reviews_useful)
            TextView popular_reviews_useful;
            @BindView(R.id.popular_reviews_useless)
            TextView popular_reviews_useless;


            public MyViewHolder6(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}