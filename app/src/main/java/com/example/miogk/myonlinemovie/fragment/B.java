package com.example.miogk.myonlinemovie.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.MovieContentActivity;
import com.example.miogk.myonlinemovie.R;
import com.example.miogk.myonlinemovie.ShowCinemaActivity;
import com.example.miogk.myonlinemovie.domain.HotMovie;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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

import static com.example.miogk.myonlinemovie.utilssssss.ConstantUtils.TIME_OUT;

@SuppressLint("HandlerLeak")
public class B extends Fragment {
    private static B b;
    private static final String TAG = "B";
    private boolean isFirst = true;
    @BindView(R.id.a_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    private final int EVERY_PAGE_COUNT = 10;
    private int start = 0;
    private int count = EVERY_PAGE_COUNT;
    private int total;
    private static String city = "上海";

    private boolean firstTime = true;
    private boolean isFirstTime = true;
    private MyAdapter myAdapter;
    private boolean hasMore = true;
    private boolean isLoad;
    private boolean isRefresh;
    private final int num_of_refresh = 0;
    private final int num_of_load = 1;
    @BindView(R.id.progress_bar_layout)
    ViewGroup progress_bar_layout;

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        ButterKnife.bind(this, view);
        initViews();
//        junitText();
//        junitText2(start, EVERY_PAGE_COUNT, null);
        return view;
    }

    public static B newInstance(String ccity) {
        city = ccity;
        return new B();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirst) {
            if (progress_bar_layout != null) {
                progress_bar_layout.setVisibility(View.VISIBLE);
            }
            isFirst = false;
            junitText2(start, EVERY_PAGE_COUNT, null);
        }
        Log.e(TAG, "setUserVisibleHint: " + isVisibleToUser);
    }

    private void initViews() {
        smart_refresh_layout.setRefreshHeader(new ClassicsHeader(this.getContext()));
        smart_refresh_layout.setRefreshFooter(new ClassicsFooter(this.getContext()));
        smart_refresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoad = true;
                isRefresh = false;
                if (count >= total) {
                    count = total;
                } else {
                    count += EVERY_PAGE_COUNT;
                }
                junitText2(start, count, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                isLoad = false;
                hasMore = true;
                count = EVERY_PAGE_COUNT;
                junitText2(0, EVERY_PAGE_COUNT, refreshLayout);
            }
        });
    }

    private void junitText2(final int s, int c, final RefreshLayout refreshLayout) {
        Log.e(TAG, "junitText2: ");
//        如果是下拉并且没有size>total没有数据了就生成吐司并返回
        if (isLoad && !hasMore) {
            Toast.makeText(getContext(), "已经到底了", Toast.LENGTH_SHORT).show();
            refreshLayout.finishLoadMore(true);
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantUtils.BASE_URL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<HotMovie> observable = apiService.getComingMovieUserRxjava(city, s, c);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotMovie>() {

                    private long start;

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HotMovie hotMovie) {
                        start = System.currentTimeMillis();
                        total = Integer.parseInt(hotMovie.total);
                        int size = hotMovie.subjects.size();
                        if (isFirstTime) {
                            myAdapter = new MyAdapter(hotMovie);
                            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            mRecyclerView.setAdapter(myAdapter);
                            if (total > size) {
                                hasMore = true;
                            }
                            isFirstTime = false;
                        }
                        if (isRefresh || (isLoad && hasMore)) {
                            myAdapter.subjects.clear();
                            myAdapter.subjects = hotMovie.subjects;
                            myAdapter.notifyDataSetChanged();
                        }
                        if (size >= total) {
                            hasMore = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isRefresh && refreshLayout != null) {
                            refreshLayout.finishRefresh(false);
                        } else if (isLoad && refreshLayout != null) {
                            refreshLayout.finishLoadMore(false);
                        }
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        if (isRefresh && refreshLayout != null) {
                            refreshLayout.finishRefresh(true);
                        } else if (isLoad && refreshLayout != null) {
                            refreshLayout.finishLoadMore(true);
                        }
                        if (progress_bar_layout != null) {
                            progress_bar_layout.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: ");
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        HotMovie hotMovie;
        ArrayList<HotMovie.Subjects> subjects;

        MyAdapter(HotMovie hotMovie) {
            this.hotMovie = hotMovie;
            subjects = hotMovie.subjects;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.a_recycler_view_item, null);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final HotMovie.Subjects subject = subjects.get(i);
            String average = subject.rating.average;
            String starts = subject.rating.stars;
            myViewHolder.starts.setText(average);
            myViewHolder.rating_bar.setRating(Float.parseFloat(starts) / 10);
            myViewHolder.textView.setText(subject.title);
            Glide.with(getContext()).load(subject.images.large).into(myViewHolder.imageView);
            View view = myViewHolder.view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MovieContentActivity.class);
                    intent.putExtra("movieId", subject.id);
                    intent.putExtra("movieTitle", subject.title);
                    startActivity(intent);
                }
            });
            myViewHolder.buy_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowCinemaActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.image_view)
            ImageView imageView;
            @BindView(R.id.movie_name)
            TextView textView;
            @BindView(R.id.a_recycler_view_rating_bar)
            RatingBar rating_bar;
            @BindView(R.id.a_recycler_view_average)
            TextView starts;
            @BindView(R.id.a_recycler_view_buy_ticket)
            Button buy_ticket;
            View view;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                ButterKnife.bind(this, itemView);
            }
        }
    }
}