package com.example.miogk.myonlinemovie.fragment;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.example.miogk.myonlinemovie.HaveSeenActivity;
import com.example.miogk.myonlinemovie.LoginActivity;
import com.example.miogk.myonlinemovie.MainActivity;
import com.example.miogk.myonlinemovie.R;
import com.example.miogk.myonlinemovie.WantToSeeActivity;
import com.example.miogk.myonlinemovie.utilssssss.MySqliteDatabaseHelper;
import com.example.miogk.myonlinemovie.utilssssss.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.setting_login_layout)
    ViewGroup setting_login_layout;
    @BindView(R.id.setting_login)
    Button setting_login;
    @BindView(R.id.want_to_see)
    Button want_to_see;
    @BindView(R.id.have_seen)
    Button have_seen;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.setting_have_login_layout)
    ViewGroup setting_have_login_layout;
    @BindView(R.id.setting_have_login)
    TextView setting_have_login;
    @BindView(R.id.setting_login_out_layout)
    ViewGroup setting_login_out_layout;
    @BindView(R.id.setting_login_out)
    Button setting_login_out;
    private MySqliteDatabaseHelper mSqliteDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "SettingFragment";
    private String username;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, null);
        ButterKnife.bind(this, view);

        setting_login.setOnClickListener(this);
        want_to_see.setOnClickListener(this);
        have_seen.setOnClickListener(this);
        setting_login_out.setOnClickListener(this);
        mSqliteDatabaseHelper = new MySqliteDatabaseHelper(getContext());
        mDatabase = mSqliteDatabaseHelper.getReadableDatabase();
        final String movieId = "10808080";
        final ImageView imageView = view.findViewById(R.id.music_imageview);
        ImageView yihua = view.findViewById(R.id.yihua);
        yihua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(imageView);
            }
        });
        return view;
    }

    private void showPopMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.menu_music_list_item_action_fragment);
        popupMenu.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        //如果sharedPreferences里有用户名
        if (!TextUtils.isEmpty(username)) {
            setting_have_login.setText(username);
            setting_have_login_layout.setVisibility(View.VISIBLE);
            setting_login_layout.setVisibility(View.GONE);
            setting_login_out_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.setting_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.want_to_see:
                checkIsLogon(WantToSeeActivity.class);
                break;
            case R.id.have_seen:
                checkIsLogon(HaveSeenActivity.class);
                break;
            case R.id.setting_login_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUtils.putInSharedPreferences(getContext(), "user", "username", "");
                        setting_login_layout.setVisibility(View.VISIBLE);
                        setting_login_layout.animate().rotation(180);
                        setting_have_login_layout.setVisibility(View.GONE);
                        setting_login_out_layout.setVisibility(View.GONE);
                        setting_have_login.setText("");
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setMessage("确定要登出吗?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
    }

    private void checkIsLogon(Class clazz) {
        if (!TextUtils.isEmpty(username)) {
            startActivity(new Intent(getActivity(), clazz).putExtra("username", username));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("您还未登录，请先登录.");
            final AlertDialog alertDialog = builder.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.cancel();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }, 1500);
        }
    }
}