package com.example.miogk.myonlinemovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.domain.HotMovieContent;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowAllTrailersClips extends AppCompatActivity {
    Activity activity = this;
    private String movieTitle;
    private ArrayList<HotMovieContent.Trailers> trailers;//预告片
    private ArrayList<HotMovieContent.Bloopers> bloopers;//花絮
    private ArrayList<HotMovieContent.Clips> clips;//片段
    @BindView(R.id.show_all_trailers_clips_title)
    TextView show_all_trailers_clips_title;
    @BindView(R.id.show_all_trailers_recycler_view)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.show_all_bloopers_recycler_view)
    RecyclerView bloopersRecyclerView;
    @BindView(R.id.show_all_clips_recycler_view)
    RecyclerView clipsRecyclerView;
    private static final String TAG = "ShowAllTrailersClips";
    @BindView(R.id.show_all_bloopers_recycler_view_layout)
    ViewGroup show_all_bloopers_recycler_view_layout;
    @BindView(R.id.show_all_clips_recycler_view_layout)
    ViewGroup show_all_clips_recycler_view_layout;
    @BindView(R.id.show_all_trailers_recycler_view_layout)
    ViewGroup show_all_trailers_recycler_view_layout;
    @BindView(R.id.show_all_bloopers_trailers_clips_tool_bar)
    Toolbar show_all_bloopers_trailers_clips_tool_bar;
    @BindView(R.id.show_all_bloopers_trailers_clips_back)
    ImageButton show_all_bloopers_trailers_clips_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_trailers_clips_layout);
        //全屏切换高度不一致修复
//        MyUtils.initStatusBar(R.color.white, this);
//        MyUtils.statusBarBackgroundColor(getWindow(), this, R.color.white);
//        //小米的官方黑色字体设置
        MyUtils.MIUISetStatusBarLightMode(this, true);
//        MyUtils.statusBarLightModeInAndroid(this);
        ButterKnife.bind(this);
        Window window = getWindow();
        /**解决全屏切换高度不一致的问题
         */
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        window.setStatusBarColor(getResources().getColor(R.color.black));

//        View view = new View(activity);
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        view.setBackgroundColor(getResources().getColor(R.color.white));
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                statusBarHeight);
//        view.setLayoutParams(layoutParams);
//        decorView.addView(view);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setSupportActionBar(show_all_bloopers_trailers_clips_tool_bar);
        show_all_bloopers_trailers_clips_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        movieTitle = intent.getStringExtra("movieTitle");
        trailers = (ArrayList<HotMovieContent.Trailers>) intent.getSerializableExtra("trailers");
        bloopers = (ArrayList<HotMovieContent.Bloopers>) intent.getSerializableExtra("bloopers");
        clips = (ArrayList<HotMovieContent.Clips>) intent.getSerializableExtra("clips");
        show_all_trailers_clips_title.setText(movieTitle + "的视频");
        Log.e(TAG, "onCreate: " + clips);

        // 因为clips视频有可能是空，所有先判断预告片、片段、花絮，这些视频是否有内容
        // 默认是将这些ViewGroup先隐藏，有内容的话再填充和显示.
        if (trailers.size() != 0) {
            inflaterTrailersAdapter();
            show_all_trailers_recycler_view_layout.setVisibility(View.VISIBLE);
        }
        if (bloopers.size() != 0) {
            inflaterBloopersAdapter();
            show_all_bloopers_recycler_view_layout.setVisibility(View.VISIBLE);
        }
        if (clips.size() != 0) {
            inflaterClipsAdapter();
            show_all_clips_recycler_view_layout.setVisibility(View.VISIBLE);
        }
    }

    //片段
    private void inflaterClipsAdapter() {
        MyClipsAdapter myClipsAdapter = new MyClipsAdapter(clips);
        clipsRecyclerView.setAdapter(myClipsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        clipsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    //花絮
    private void inflaterBloopersAdapter() {
        MyBloopersAdapter myBloopersAdapter = new MyBloopersAdapter(bloopers);
        bloopersRecyclerView.setAdapter(myBloopersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        bloopersRecyclerView.setLayoutManager(linearLayoutManager);
    }

    //预告片
    private void inflaterTrailersAdapter() {
        MyTrailersAdapter myTrailersAdapter = new MyTrailersAdapter(trailers);
        trailersRecyclerView.setAdapter(myTrailersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }


    //预告片
    class MyTrailersAdapter extends RecyclerView.Adapter<MyTrailersAdapter.MyTrailersViewHolder> {
        ArrayList<HotMovieContent.Trailers> trailers;

        MyTrailersAdapter(ArrayList<HotMovieContent.Trailers> trailers) {
            this.trailers = trailers;
        }

        @NonNull
        @Override
        public MyTrailersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.show_all_bloopers_trailers_clips_item, null, false);
            return new MyTrailersViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyTrailersViewHolder myTrailersViewHolder, int i) {
            ImageView show_all_bloopers_trailers_clips_image_view = myTrailersViewHolder.show_all_bloopers_trailers_clips_image_view;
            TextView show_all_bloopers_trailers_clips_title = myTrailersViewHolder.show_all_bloopers_trailers_clips_title;
            HotMovieContent.Trailers trailer = trailers.get(i);
            final String path = trailer.resource_url;
            show_all_bloopers_trailers_clips_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, PLTextureViewBilibili.class);
                    intent.putExtra("path", path);
                    intent.putExtra("movieTitle", movieTitle);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            show_all_bloopers_trailers_clips_title.setText(trailer.title);
            Glide.with(activity).load(trailer.medium).into(show_all_bloopers_trailers_clips_image_view);
        }

        @Override
        public int getItemCount() {
            return trailers.size();
        }

        class MyTrailersViewHolder extends RecyclerView.ViewHolder {
            ImageView show_all_bloopers_trailers_clips_image_view;
            TextView show_all_bloopers_trailers_clips_title;

            public MyTrailersViewHolder(@NonNull View itemView) {
                super(itemView);
                show_all_bloopers_trailers_clips_title = itemView.findViewById(R.id.show_all_bloopers_trailers_clips_title);
                show_all_bloopers_trailers_clips_image_view = itemView.findViewById(R.id.show_all_bloopers_trailers_clips_image_view);
            }
        }
    }

    //片段
    class MyClipsAdapter extends RecyclerView.Adapter<MyClipsAdapter.MyClipsViewHolder> {
        ArrayList<HotMovieContent.Clips> clips;

        MyClipsAdapter(ArrayList<HotMovieContent.Clips> clips) {
            this.clips = clips;
        }

        @NonNull
        @Override
        public MyClipsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.show_all_bloopers_trailers_clips_item, null, false);
            return new MyClipsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyClipsViewHolder myClipsViewHolder, int i) {
            ImageView show_all_bloopers_trailers_clips_image_view = myClipsViewHolder.show_all_bloopers_trailers_clips_image_view;
            TextView show_all_bloopers_trailers_clips_title = myClipsViewHolder.show_all_bloopers_trailers_clips_title;
            HotMovieContent.Clips clip = clips.get(i);
            final String path = clip.resource_url;
            show_all_bloopers_trailers_clips_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, PLTextureViewBilibili.class);
                    intent.putExtra("path", path);
                    intent.putExtra("movieTitle", movieTitle);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            show_all_bloopers_trailers_clips_title.setText(clip.title);
            Glide.with(activity).load(clip.medium).into(show_all_bloopers_trailers_clips_image_view);
        }

        @Override
        public int getItemCount() {
            return clips.size();
        }

        class MyClipsViewHolder extends RecyclerView.ViewHolder {
            ImageView show_all_bloopers_trailers_clips_image_view;
            TextView show_all_bloopers_trailers_clips_title;

            public MyClipsViewHolder(@NonNull View itemView) {
                super(itemView);
                show_all_bloopers_trailers_clips_title = itemView.findViewById(R.id.show_all_bloopers_trailers_clips_title);
                show_all_bloopers_trailers_clips_image_view = itemView.findViewById(R.id.show_all_bloopers_trailers_clips_image_view);
            }
        }
    }

    //花絮
    class MyBloopersAdapter extends RecyclerView.Adapter<MyBloopersAdapter.MyBloopersViewHolder> {
        ArrayList<HotMovieContent.Bloopers> bloopers;

        MyBloopersAdapter(ArrayList<HotMovieContent.Bloopers> bloopers) {
            this.bloopers = bloopers;
        }

        @NonNull
        @Override
        public MyBloopersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.show_all_bloopers_trailers_clips_item, null, false);
            return new MyBloopersViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyBloopersViewHolder myBloopersViewHolder, int i) {
            ImageView show_all_bloopers_trailers_clips_image_view = myBloopersViewHolder.show_all_bloopers_trailers_clips_image_view;
            TextView show_all_bloopers_trailers_clips_title = myBloopersViewHolder.show_all_bloopers_trailers_clips_title;
            HotMovieContent.Bloopers blooper = bloopers.get(i);
            final String path = blooper.resource_url;
            show_all_bloopers_trailers_clips_title.setText(blooper.title);
            show_all_bloopers_trailers_clips_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                    Intent intent = new Intent(activity, PLTextureViewBilibili.class);
                    intent.putExtra("path", path);
                    intent.putExtra("movieTitle", movieTitle);
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            });
            Glide.with(activity).load(blooper.medium).into(show_all_bloopers_trailers_clips_image_view);
        }

        @Override
        public int getItemCount() {
            return bloopers.size();
        }

        class MyBloopersViewHolder extends RecyclerView.ViewHolder {
            ImageView show_all_bloopers_trailers_clips_image_view;
            TextView show_all_bloopers_trailers_clips_title;

            public MyBloopersViewHolder(@NonNull View itemView) {
                super(itemView);
                show_all_bloopers_trailers_clips_image_view = itemView.findViewById(R.id.show_all_bloopers_trailers_clips_image_view);
                show_all_bloopers_trailers_clips_title = itemView.findViewById(R.id.show_all_bloopers_trailers_clips_title);
            }
        }
    }
}