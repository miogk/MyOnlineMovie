package com.example.miogk.myonlinemovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JuzhaoItemActivity extends AppCompatActivity {
    @BindView(R.id.juzhao_item_image)
    ImageView juzhao_item_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juzhao_item);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        juzhao_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        Glide.with(this).load(url).into(juzhao_item_image);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}