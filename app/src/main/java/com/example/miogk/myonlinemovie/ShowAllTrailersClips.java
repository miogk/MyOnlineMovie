package com.example.miogk.myonlinemovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowAllTrailersClips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_trailers_clips_layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}