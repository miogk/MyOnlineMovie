package com.example.miogk.myonlinemovie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.R;
import com.example.miogk.myonlinemovie.domain.HotMovieContent;

public class DirectorAndActorFragment extends Fragment {
    private static final String TAG = "DirectorAndActorFragmen";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.director_actor_layout, container, false);
        Bundle b = getArguments();
        HotMovieContent.Casts c = (HotMovieContent.Casts) b.get("o");
        Log.e(TAG, "instantiateItem: " + container);
        ImageView director_actor_image_view = view.findViewById(R.id.director_actor_image_view);
        TextView name_en = view.findViewById(R.id.director_actor_name_en);
        TextView name = view.findViewById(R.id.director_actor_name);
        TextView job = view.findViewById(R.id.director_actor_job);
        Glide.with(getActivity()).load(c.avatars.small).into(director_actor_image_view);
        name.setText(c.name);
        name_en.setText(c.name_en);
        if (b.containsKey("director")) {
            job.setText("导演");
        } else {
            job.setText("主演");
        }
        return view;
    }
}
