package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.domain.SingleReview;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

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

public class SingleReviewActivity extends AppCompatActivity {
    private Activity activity = this;
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
    private String reviewId;
    @BindView(R.id.base_tool_bar)
    Toolbar toolbar;
    @BindView(R.id.base_author_name)
    TextView author_name;
    @BindView(R.id.base_back)
    ImageButton back;
    private String authorName;
    @BindView(R.id.loading_progress_loading_anim)
    ViewGroup loading_progress_loading_anim;
    @BindView(R.id.loading_progress_try_again)
    ViewGroup loading_progress_try_again;
    @BindView(R.id.progress_bar_layout)
    ViewGroup progress_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_review_layout);
        ButterKnife.bind(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
//        MyUtils.setMiuiStatusBarDarkMode(activity, true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        MyUtils.statusBarLightModeInAndroid(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        reviewId = intent.getStringExtra("reviewId");
        authorName = intent.getStringExtra("author_name");
        author_name.setText(authorName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getDataFromNet();
    }

    private void getDataFromNet() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<SingleReview> observable = service.getSingleReview(reviewId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleReview>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final SingleReview singleReview) {
                        Glide.with(activity).load(singleReview.author.avatar).into(popular_reviews_image_view);
                        popular_reviews_name.setText(singleReview.author.name);
                        popular_reviews_rating_bar.setRating(Float.parseFloat(singleReview.rating.value));
                        popular_reviews_title.setText(singleReview.title);
                        popular_reviews_summary.setLayerType(View.LAYER_TYPE_NONE, null);
                        String summary = singleReview.summary;
                        SpannableString more = new SpannableString(summary + "更多");
                        //可点击的富文本
                        more.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                popular_reviews_summary.setText(singleReview.content.trim());
                                popular_reviews_summary.setMovementMethod(null);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(getResources().getColor(R.color.yellow));
                            }
                        }, more.length() - 2, more.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        popular_reviews_summary.setMovementMethod(LinkMovementMethod.getInstance());
                        popular_reviews_summary.setText(more);
                        popular_reviews_created_at.setText(singleReview.created_at);
                        popular_reviews_useful.setText(singleReview.useful_count);
                        popular_reviews_useless.setText(singleReview.useless_count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading_progress_loading_anim.setVisibility(View.GONE);
                        loading_progress_try_again.setVisibility(View.VISIBLE);
                        loading_progress_try_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loading_progress_loading_anim.setVisibility(View.VISIBLE);
                                loading_progress_try_again.setVisibility(View.GONE);
                                getDataFromNet();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        progress_bar_layout.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}