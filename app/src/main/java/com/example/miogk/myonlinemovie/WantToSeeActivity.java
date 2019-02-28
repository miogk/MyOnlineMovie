package com.example.miogk.myonlinemovie;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;
import com.example.miogk.myonlinemovie.utilssssss.MySqliteDatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import service.ApiService;

public class WantToSeeActivity extends AppCompatActivity {
    MySqliteDatabaseHelper mySqliteDatabaseHelper;
    @BindView(R.id.activity_want_to_see)
    RecyclerView activity_want_to_see;
    private String[] movieIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_see);
        ButterKnife.bind(this);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        mySqliteDatabaseHelper = new MySqliteDatabaseHelper(this);
        SQLiteDatabase mDatabase = mySqliteDatabaseHelper.getReadableDatabase();
        String username = getIntent().getStringExtra("username");
        String sql = "select want_to_see from user where username = ?";
        Cursor cursor = mDatabase.rawQuery(sql, new String[]{username});
        if (cursor.moveToNext()) {
            String wts = cursor.getString(cursor.getColumnIndex("want_to_see"));
            if (!TextUtils.isEmpty(wts)) {
                movieIds = wts.split(":");
                if (movieIds.length > 0) {
                    getDataFromNet(movieIds);
                }
            }
        }
    }

    private void getDataFromNet(String[] movieIds) {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(ConstantUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
//        service.getHotMovieContentWithRxJava();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        String[] movieIds;

        public MyAdapter(String[] movieIds) {
            this.movieIds = movieIds;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(WantToSeeActivity.this).inflate(R.layout.the_result_of_search_movie, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return movieIds.length;
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


