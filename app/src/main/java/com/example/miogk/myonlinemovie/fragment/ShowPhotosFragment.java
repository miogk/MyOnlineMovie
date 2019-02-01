package com.example.miogk.myonlinemovie.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.miogk.myonlinemovie.R;
import com.example.miogk.myonlinemovie.domain.ShowPhotos;
import com.example.miogk.myonlinemovie.utilssssss.ConstantUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import service.ApiService;

public class ShowPhotosFragment extends Fragment {
    private int start;
    private int count;
    private String movieId;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        start = (int) bundle.get("start");
        count = (int) bundle.get("count");
        movieId = (String) bundle.get("movieId");
        showPhotos();
        View view = inflater.inflate(R.layout.show_photos_item, null, false);
        recyclerView = view.findViewById(R.id.show_photos_item_recycler_view);
        return view;
    }

    private void showPhotos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<ShowPhotos> photoObservable = apiService.getPhotosWitRxJava(movieId, String.valueOf(start), String.valueOf(count));
        photoObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShowPhotos>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ShowPhotos showPhotos) {
                        ArrayList<ShowPhotos.Photo> photos = showPhotos.photos;
                        MyAdapter mAdapter = new MyAdapter(photos);
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.show_photos_item_item, null, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            Glide.with(getContext()).load(photos.get(i).thumb).into(myViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.show_photos_item_image_view);
            }
        }
    }
}