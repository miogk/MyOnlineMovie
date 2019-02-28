package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.miogk.myonlinemovie.domain.PersonalInformation;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import service.ApiService;

public class PersonalInformationActivity extends AppCompatActivity {
    Activity activity = this;
    private static final String TAG = "PersonalInformationActi";
    @BindView(R.id.personal_appbar_layout)
    AppBarLayout personal_appbar_layout;
    @BindView(R.id.personal_circle_image_view)
    CircleImageView personal_circle_image_view;
    @BindView(R.id.personal_tool_bar)
    Toolbar personal_tool_bar;
    @BindView(R.id.personal_inflate_layout)
    ViewGroup personal_inflate_layout;
    @BindView(R.id.personal_collapsing)
    CollapsingToolbarLayout personal_collapsing;
    @BindView(R.id.personal_back)
    ImageButton personal_back;
    @BindView(R.id.personal_name)
    TextView personal_name;
    @BindView(R.id.personal_professions)
    TextView personal_professions;
    @BindView(R.id.personal_summary)
    TextView personal_summary;
    @BindView(R.id.personal_photos_recycler_view)
    RecyclerView personal_photos_recycler_view;
    @BindView(R.id.personal_works_recycler_view)
    RecyclerView personal_works_recycler_view;
    @BindView(R.id.personal_nested_scroll_view)
    NestedScrollView personal_nested_scroll_view;
    @BindView(R.id.personal_collapsing_image_view)
    ImageView personal_collapsing_image_view;
    @BindView(R.id.personal_birthday)
    TextView personal_birthday;
    @BindView(R.id.personal_born_place)
    TextView personal_born_place;
    @BindView(R.id.personal_constellation)
    TextView personal_constellation;
    @BindView(R.id.loading_progress_loading_anim)
    ViewGroup loading_progress_loading_anim;
    @BindView(R.id.loading_progress_try_again)
    ViewGroup loading_progress_try_again;
    @BindView(R.id.progress_bar_layout)
    ViewGroup progress_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setSupportActionBar(personal_tool_bar);
        MyUtils.statusBarBackgroundColor(getWindow(), this, R.color.white);
        //小米的官方黑色字体设置
        MyUtils.MIUISetStatusBarLightMode(this, true);
        MyUtils.statusBarLightModeInAndroid(this);
        Intent intent = getIntent();
        //吴京 -- 1000525
        String id = intent.getStringExtra("id");
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        personal_appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int minVisibleHeight = appBarLayout.getMinimumHeightForVisibleOverlappingContent();
                int totalRange = appBarLayout.getTotalScrollRange();
                int offsetY = totalRange - minVisibleHeight;
                int rang = appBarLayout.getTotalScrollRange();
//                if (i == 0) {
//                    personal_tool_bar.setVisibility(View.GONE);
//                } else {
//                    personal_tool_bar.setVisibility(View.VISIBLE);
//                }
//                personal_tool_bar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white), Math.abs(i * 1.0f) / appBarLayout.getTotalScrollRange()));
            }
        });
        getDataFromNet(id);
    }

    private int measureView(final View view) {
        final int[] height = new int[2];
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height[0] = view.getHeight();
                height[1] = view.getWidth();
            }
        });
        return height[0];
    }

    //渐变背景色
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    private void getDataFromNet(final String id) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                readTimeout(5000, TimeUnit.SECONDS).
                writeTimeout(5000, TimeUnit.SECONDS).
                connectTimeout(5000, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtils.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<PersonalInformation> observable = apiService.getPeronalInformation(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PersonalInformation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PersonalInformation o) {
                        String name_en = o.name_en;
                        String name = o.name;
                        //中文名和英文名
                        personal_name.setText(name + "(" + name_en + ")");
                        //职位
                        ArrayList<String> professions = o.professions;
                        StringBuilder profession = new StringBuilder();
                        for (String s : professions) {
                            if (!TextUtils.isEmpty(s)) {
                                profession.append(s);
                            }
                        }
                        personal_professions.setText(profession.toString());
                        personal_birthday.setText(o.birthday);
                        personal_born_place.setText(o.born_place);
                        personal_constellation.setText(o.constellation);
                        //头像
                        RequestOptions requestOptions = new RequestOptions();
//                        requestOptions.placeholder(R.mipmap.yihua);
                        Glide.with(activity).load(o.avatars.medium).into(personal_collapsing_image_view);
                        Glide.with(activity).load(o.avatars.medium).into(personal_circle_image_view);
                        personal_summary.setText(o.summary);
                        //剧照
                        ArrayList<PersonalInformation.Work.Photo> photos = o.photos;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        personal_photos_recycler_view.setLayoutManager(linearLayoutManager);
                        personal_photos_recycler_view.setAdapter(new MyAdapter(photos));
                        //作品
                        ArrayList<PersonalInformation.Work> works = o.works;
                        personal_works_recycler_view.setLayoutManager(new LinearLayoutManager(activity));
                        personal_works_recycler_view.setAdapter(new MyWorkAdapter(works));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loading_progress_loading_anim.getVisibility() == View.VISIBLE) {
                            loading_progress_loading_anim.setVisibility(View.GONE);
                            loading_progress_try_again.setVisibility(View.VISIBLE);
                            loading_progress_try_again.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getDataFromNet(id);
                                    loading_progress_loading_anim.setVisibility(View.VISIBLE);
                                    loading_progress_try_again.setVisibility(View.GONE);
                                }
                            });
                        }
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        if (progress_bar_layout.getVisibility() == View.VISIBLE) {
                            progress_bar_layout.setVisibility(View.GONE);
                        }
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    //剧照数据
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<PersonalInformation.Work.Photo> photos;

        MyAdapter(ArrayList<PersonalInformation.Work.Photo> photos) {
            this.photos = photos;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.recycler_view_pictures_layout, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            PersonalInformation.Work.Photo photo = photos.get(i);
            String image = photo.image;
            Glide.with(activity).load(image).into(myViewHolder.recycler_view_pictures_layout_image);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView recycler_view_pictures_layout_image;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                recycler_view_pictures_layout_image = itemView.findViewById(R.id.recycler_view_pictures_layout_image);
            }
        }
    }

    //作品
    class MyWorkAdapter extends RecyclerView.Adapter<MyWorkAdapter.MyWorkViewHolder> {
        ArrayList<PersonalInformation.Work> works;

        MyWorkAdapter(ArrayList<PersonalInformation.Work> works) {
            this.works = works;
        }

        @NonNull
        @Override
        public MyWorkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.the_result_of_search_movie, null);
            return new MyWorkViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyWorkViewHolder myWorkViewHolder, int i) {
            final PersonalInformation.Work work = works.get(i);
            String url = work.subject.images.medium;
            String title = work.subject.title;
            ArrayList<String> genres = work.subject.genres;
            String year = work.subject.year;
            String average = work.subject.rating.average;
            ArrayList<PersonalInformation.Work.Subject.Casts> casts = work.subject.casts;
            Glide.with(activity).load(url).into(myWorkViewHolder.the_result_of_search_movie_image_view);
            myWorkViewHolder.the_result_of_search_movie_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, MovieContentActivity.class);
                    intent.putExtra("movieId", work.subject.id);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            myWorkViewHolder.the_result_of_search_movie_title.setText(title);
            StringBuilder sb = new StringBuilder();
            for (PersonalInformation.Work.Subject.Casts c : casts) {
                sb.append(c.name + "\\");
            }
            myWorkViewHolder.the_result_of_search_movie_yanzhirenyuan.setText(sb.toString());
            sb = null;
            sb = new StringBuilder();
            for (String g : genres) {
                sb.append(g + "\\");
            }
            myWorkViewHolder.the_result_of_search_movie_genres.setText(sb.toString());
            sb = null;
            sb = new StringBuilder();
            myWorkViewHolder.the_result_of_search_movie_year.setText(year);
            myWorkViewHolder.the_result_of_search_movie_point.setText(average);
            myWorkViewHolder.the_result_of_search_movie_rating.setRating((Float.parseFloat(work.subject.rating.stars) / Float.parseFloat(work.subject.rating.max)));
        }

        @Override
        public int getItemCount() {
            return works.size();
        }

        class MyWorkViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.the_result_of_search_movie_image_view)
            ImageView the_result_of_search_movie_image_view;
            @BindView(R.id.the_result_of_search_movie_title)
            TextView the_result_of_search_movie_title;
            @BindView(R.id.the_result_of_search_movie_yanzhirenyuan)
            TextView the_result_of_search_movie_yanzhirenyuan;
            @BindView(R.id.the_result_of_search_movie_genres)
            TextView the_result_of_search_movie_genres;
            @BindView(R.id.the_result_of_search_movie_year)
            TextView the_result_of_search_movie_year;
            @BindView(R.id.the_result_of_search_movie_point)
            TextView the_result_of_search_movie_point;
            @BindView(R.id.the_result_of_search_movie_rating)
            RatingBar the_result_of_search_movie_rating;

            public MyWorkViewHolder(@NonNull View itemView) {
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