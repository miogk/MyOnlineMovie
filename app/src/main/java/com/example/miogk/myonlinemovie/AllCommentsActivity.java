package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.domain.AllComments;
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

public class AllCommentsActivity extends AppCompatActivity {
    @BindView(R.id.all_comments_recycler_view)
    RecyclerView all_comments_recycler_view;
    Activity activity = this;
    private String movieId;
    private String start = "0";
    private String count = "20";
    @BindView(R.id.progress_bar_layout)
    ViewGroup progress_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_comments_layout);
        ButterKnife.bind(this);
        MyUtils.statusBarBackgroundColor(getWindow(), this, R.color.white);
        //小米的官方黑色字体设置
        MyUtils.MIUISetStatusBarLightMode(this, true);
        MyUtils.statusBarLightModeInAndroid(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        getDataFromNet();
    }

    private void getDataFromNet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<AllComments> observable = service.getAllComments(movieId, start, count);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllComments>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllComments allComments) {
                        ArrayList<AllComments.Comments> popular_comment = allComments.comments;
                        all_comments_recycler_view.setLayoutManager(new LinearLayoutManager(activity));
                        MyAdapter adapter = new MyAdapter(popular_comment);
                        all_comments_recycler_view.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {

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
        ArrayList<AllComments.Comments> popular_comment;

        MyAdapter(ArrayList<AllComments.Comments> popular_comment) {
            this.popular_comment = popular_comment;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.activity_all_comments_item, null, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            AllComments.Comments comment = popular_comment.get(i);
            String avatar = comment.author.avatar;
            String name = comment.author.name;
            String value = comment.rating.value;
            String created_at = comment.created_at;
            String useful = comment.useful_count;
            String content = comment.content;
            Glide.with(activity).load(avatar).into(myViewHolder.author_avatar);
            myViewHolder.author_name.setText(name);
            myViewHolder.author_rating_bar.setRating(Float.parseFloat(value));
            myViewHolder.author_created_at.setText(created_at);
            myViewHolder.author_userful_count.setText(useful);
            myViewHolder.author_comment_content.setText(content);
        }

        @Override
        public int getItemCount() {
            return popular_comment.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.author_avatar)
            ImageView author_avatar;
            @BindView(R.id.author_name)
            TextView author_name;
            @BindView(R.id.author_rating_bar)
            RatingBar author_rating_bar;
            @BindView(R.id.author_created_at)
            TextView author_created_at;
            @BindView(R.id.author_userful_count)
            TextView author_userful_count;
            @BindView(R.id.author_comment_content)
            TextView author_comment_content;

            public MyViewHolder(@NonNull View itemView) {
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