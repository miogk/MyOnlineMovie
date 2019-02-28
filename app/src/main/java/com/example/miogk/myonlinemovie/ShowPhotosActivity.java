package com.example.miogk.myonlinemovie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.domain.ShowPhotos;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import java.util.ArrayList;

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

public class ShowPhotosActivity extends AppCompatActivity {
    private String photo_count;
    @BindView(R.id.show_photos_recycler_view)
    RecyclerView show_photos_recycler_view;
    int spanCount;
    @BindView(R.id.progress_bar_layout)
    ViewGroup progress_bar_layout;
    Activity activity = this;
    @BindView(R.id.show_photos_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.show_photos_movie_title)
    TextView show_photos_movie_title;
    @BindView(R.id.show_photos_back)
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photos);
        ButterKnife.bind(this);
        MyUtils.statusBarBackgroundColor(getWindow(), this, R.color.white);
        //小米的官方黑色字体设置
        MyUtils.MIUISetStatusBarLightMode(this, true);
        MyUtils.statusBarLightModeInAndroid(this);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setSupportActionBar(toolbar);
        //获取Intent传来的数据
        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("movieTitle");
        String movieId = intent.getStringExtra("movieId");
        photo_count = intent.getStringExtra("photo_count");
        show_photos_movie_title.setText(movieTitle + "的剧照");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showPhotos(movieId);
    }

    @SuppressLint("CheckResult")
    private void showPhotos(final String movieId) {
        if (progress_bar_layout != null) {
            progress_bar_layout.setVisibility(View.VISIBLE);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<ShowPhotos> observable = service.getPhotosWitRxJava(movieId, "0", photo_count);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShowPhotos>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ShowPhotos showPhotos) {
                        ArrayList<ShowPhotos.Photo> photos = showPhotos.photos;
                        MyAdapter mAdapter = new MyAdapter(photos);
                        show_photos_recycler_view.setAdapter(mAdapter);
                        show_photos_recycler_view.setLayoutManager(new GridLayoutManager(ShowPhotosActivity.this, 2));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progress_bar_layout != null) {
                            progress_bar_layout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (progress_bar_layout != null) {
                            progress_bar_layout.setVisibility(View.GONE);
                        }
                    }
                });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<ShowPhotos.Photo> photos;

        MyAdapter(ArrayList<ShowPhotos.Photo> photos) {
            this.photos = photos;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ShowPhotosActivity.this).inflate(R.layout.show_photos_item_item, null, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
            Glide.with(ShowPhotosActivity.this).load(photos.get(i).thumb).into(myViewHolder.imageView);
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, JuzhaoItemActivity.class);
                    intent.putExtra("url", photos.get(i).thumb);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            View rootView;

            @SuppressLint("ClickableViewAccessibility")
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                rootView = itemView;
                imageView = itemView.findViewById(R.id.show_photos_item_image_view);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}