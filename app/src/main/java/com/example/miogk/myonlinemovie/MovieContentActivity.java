package com.example.miogk.myonlinemovie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.miogk.myonlinemovie.customView.MyCustomView;
import com.example.miogk.myonlinemovie.domain.AllReviews;
import com.example.miogk.myonlinemovie.domain.HotMovieContent;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    Activity activity = this;
    @BindView(R.id.popular_comments_recycler_view)
    RecyclerView popular_comments_recycler_view;
    //总短评数
    @BindView(R.id.total_comments_count)
    TextView total_comments_count;
    @BindView(R.id.popular_reviews_recycler_view)
    RecyclerView popular_reviews_recycler_view;
    @BindView(R.id.total_reviews_count)
    TextView total_reviews_count;

    //    private String url = "https://movie.douban.com/subject/26394152/mobile";
    private ArrayList images = new ArrayList(Arrays.asList(R.drawable.p2541662397, R.drawable.p2541662397, R.drawable.p2541662397));
    private ArrayList<String> photos = new ArrayList<>();
    private MyAdapter myAdapter;
    private String movieId;
    @SuppressLint("HandlerLeak") private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                //每隔5秒自动滑动
//                timer.schedule(timerTask, 1000, 5000);
            }
        }
    };
    private TimerTask timerTask;
    Timer timer = new Timer();
    private String movieImageUrl;
    private String movieImageUrl1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_content2);
        ButterKnife.bind(this);
        //全屏切换高度不一致修复
        initStatusBar(R.color.white);
        //利用反射修改ViewPager自动滑动速度
//        fixedSpeedOfViewPager(viewPager);
        MyUtils.statusBarBackgroundColor(getWindow(), this, R.color.white);
        //小米的官方黑色字体设置
        MyUtils.MIUISetStatusBarLightMode(this, true);
        MyUtils.statusBarLightModeInAndroid(this);
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
                                appBarLayout.setExpanded(true);
                                break;
                            case 1:
                                appBarLayout.setExpanded(false);
                                break;
                            case 2:
                                appBarLayout.setExpanded(false);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    toolbar.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                }
                toolbar.setBackgroundColor(changeAlpha(Color.WHITE, Math.abs(i * 1.0f) / appBarLayout.getTotalScrollRange()));
            }
        });
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        movieImageUrl1 = intent.getStringExtra("movieImageUrl");
//        Glide.with(this).load(movieImageUrl1).into(activity_movie_conten2_image_view);
        getDataFromNet(movieId);
    }

    /**
     * 状态栏处理：解决全屏切换非全屏页面被压缩问题
     */
    public void initStatusBar(int barColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(this);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            //设置状态栏颜色
            rectView.setBackgroundColor(getResources().getColor(barColor));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
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
                        handler.sendEmptyMessage(0x123);
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
                        if (progress_bar_layout != null) {
                            progress_bar_layout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (progress_bar_layout != null) {
                            progress_bar_layout.setVisibility(View.GONE);
                        }
                        Log.e(TAG, "onComplete: ");
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
                intent.putExtra("movieId", movieId);
                ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
            }
        });
    }


    //更多预告片,花絮视频
    private void getTrailersClips(HotMovieContent o) {
        final ArrayList<HotMovieContent.Trailers> trailers = o.trailers;
        ArrayList<HotMovieContent.Bloopers> bloopers = o.bloopers;
        ArrayList<HotMovieContent.Clips> clips = o.clips;
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
        recycler_view_pictures.setLayoutManager(linearLayoutManager);
        recycler_view_pictures.setAdapter(new MyAdapter3(photos));
        total_photos.setText("更多" + o.photos_count + "张剧照");
        total_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieContentActivity.this);
                Intent intent = new Intent(activity, ShowPhotosActivity.class);
                intent.putExtra("movieId", movieId);
                intent.putExtra("photo_count", o.photos_count);
                ActivityCompat.startActivity(MovieContentActivity.this, intent, optionsCompat.toBundle());
//                startActivity(intent);
            }
        });
    }


    //获取导演和演员的数据并用横向的RecyclerView排列
    private void getDirectorAndActor(HotMovieContent o) {
        ArrayList<HotMovieContent.Casts> directors = o.directors;
        ArrayList<HotMovieContent.Casts> casts = o.casts;
        ArrayList<HotMovieContent.Casts> all = new ArrayList<>();
        all.addAll(directors);
        all.addAll(casts);
        MyAdapter2 myAdapter2 = new MyAdapter2(all);
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
            float num = ji / total * 500f;
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

        MyAdapter2(ArrayList<HotMovieContent.Casts> o) {
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
            HotMovieContent.Casts c = o.get(i);
            Glide.with(activity).load(c.avatars.small).into(myViewHolder.director_actor_image_view);
            myViewHolder.name.setText(c.name);
            myViewHolder.name_en.setText(c.name_en);
            if (i == 0) {
                myViewHolder.job.setText("导演");
            } else {
                myViewHolder.job.setText("主演");
            }
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
            String name = author.name;
            String value = rating.value;
            String created_at = review.created_at;
            String title = review.title;
            String useful = review.useful_count;
            String useless = review.useless_count;

            String summary = review.summary;
            Glide.with(activity).load(avatar).into(myViewHolder6.popular_reviews_image_view);
            myViewHolder6.popular_reviews_name.setText(name);
            myViewHolder6.popular_reviews_rating_bar.setRating(Float.parseFloat(value));
            myViewHolder6.popular_reviews_created_at.setText(created_at);
            myViewHolder6.popular_reviews_title.setText(title);
            myViewHolder6.popular_reviews_summary.setText(summary);
            myViewHolder6.popular_reviews_useful.setText(useful);
            myViewHolder6.popular_reviews_useless.setText(useless);
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        class MyViewHolder6 extends RecyclerView.ViewHolder {
            @BindView(R.id.popular_reviews_image_view)
            ImageView popular_reviews_image_view;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}