package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.domain.TheResultOfSearchMovies;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;

import org.w3c.dom.Text;

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

public class SearchActivity extends AppCompatActivity {
    Activity activity = this;
    private static final String TAG = "SearchActivity";
    @BindView(R.id.app_compat_auto_complete_text_view)
    AppCompatAutoCompleteTextView app_compat_auto_complete_text_view;
    @BindView(R.id.activity_search_cancel)
    TextView activity_search_cancel;
    @BindView(R.id.activity_search_recycler_view)
    RecyclerView activity_search_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        activity_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_compat_auto_complete_text_view.setText("");
            }
        });
        String[] names = new String[]{"流浪地球", "改革春风吹满地", "老番茄", "EXO", "奈何BOSS要娶我"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, names);
        app_compat_auto_complete_text_view.setAdapter(arrayAdapter);
        app_compat_auto_complete_text_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final String movieName = v.getText().toString();
                if (!TextUtils.isEmpty(movieName)) {
                    searchMovie(movieName);
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setMessage("请输入电影名或电影类型");
                    alertDialog.show();
                }
                return false;
            }
        });
    }

    private void searchMovie(String movieName) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantUtils.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<TheResultOfSearchMovies> theResultOfSearchMovies = service.getTheResultOfSearchMovies(movieName);
        theResultOfSearchMovies.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TheResultOfSearchMovies>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TheResultOfSearchMovies theResultOfSearchMovies) {
                        ArrayList<TheResultOfSearchMovies.Subjects> subjects = theResultOfSearchMovies.subjects;
                        Log.e(TAG, "onNext: " + subjects.get(0).year);
                        MyResultOfSearchMovieAdapter adapter = new MyResultOfSearchMovieAdapter(subjects);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        activity_search_recycler_view.setLayoutManager(linearLayoutManager);
                        activity_search_recycler_view.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    class MyResultOfSearchMovieAdapter extends RecyclerView.Adapter<MyResultOfSearchMovieAdapter.MyViewHolder> {
        ArrayList<TheResultOfSearchMovies.Subjects> subjects;

        MyResultOfSearchMovieAdapter(ArrayList<TheResultOfSearchMovies.Subjects> subjects) {
            this.subjects = subjects;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.the_result_of_search_movie, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            TheResultOfSearchMovies.Subjects subject = subjects.get(i);
            final String id = subject.id;
            final String image = subject.images.medium;
            String title = subject.title;
            String director = "";
            if (subject.directors.size() != 0) {
                director = subject.directors.get(0).name;
            }
            ArrayList<TheResultOfSearchMovies.Subjects.Casts> casts = subject.casts;
            ArrayList<String> genres = subject.genres;
            String year = subject.year;
            String average = subject.rating.average;
            String stars = subject.rating.stars;
            String max = subject.rating.max;
            StringBuilder sb = new StringBuilder();
            for (TheResultOfSearchMovies.Subjects.Casts cast : casts) {
                sb.append(cast.name + "\\");
            }
//            sb.toString().substring(0, sb.toString().length() - 1);
            String yanzhirenyuan = director + "\\" + sb.toString();
            if (!TextUtils.isEmpty(image)) {
                Glide.with(activity).load(image).into(myViewHolder.the_result_of_search_movie_image_view);
            }
            //跳转到内容显示页面
            myViewHolder.the_result_of_search_movie_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, MovieContentActivity.class);
                    intent.putExtra("movieImageUrl", image);
                    intent.putExtra("movieId", id);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            if (!TextUtils.isEmpty(title)) {
                myViewHolder.the_result_of_search_movie_title.setText(title);
            }
            if (!TextUtils.isEmpty(yanzhirenyuan)) {
                myViewHolder.the_result_of_search_movie_yanzhirenyuan.setText(yanzhirenyuan);
            }
            sb = null;
            sb = new StringBuilder();
            if (genres != null && genres.size() != 0) {
                for (String genre : genres) {
                    sb.append(genre + "/");
                }
                sb.toString().substring(0, sb.length() - 1);
                myViewHolder.the_result_of_search_movie_genres.setText(sb);
            }
            if (!TextUtils.isEmpty(year)) {
                myViewHolder.the_result_of_search_movie_year.setText(year+"上映");
            }
            if (!TextUtils.isEmpty(average)) {
                myViewHolder.the_result_of_search_movie_point.setText(average);
            }
            if (!TextUtils.isEmpty(stars) && !TextUtils.isEmpty(max)) {
                myViewHolder.the_result_of_search_movie_rating.setRating(Float.parseFloat(stars) / Float.parseFloat(max));
            }
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
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

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}