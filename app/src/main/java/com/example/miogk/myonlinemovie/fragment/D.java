package com.example.miogk.myonlinemovie.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miogk.myonlinemovie.R;

public class D extends Fragment {
    private boolean isFirst = true;
    private static final String TAG = "D";
    private ProgressDialog dialog;
    private static D d;
    private Handler handler = new Handler();
    private TextView textView;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_d, container, false);
        rootView = view;
        Log.e(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && isFirst) {
            Log.e(TAG, "setUserVisibleHint: in");
            isFirst = false;
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("ddddddddddd");
            dialog.setTitle("titttttttttttle");
            dialog.show();
            textView = rootView.findViewById(R.id.d_text_view);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setText("DDDDDDD");
                    dialog.cancel();
                }
            }, 1000);
            isFirst = false;
        }
        Log.e(TAG, "setUserVisibleHint: out" + isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: ");
    }

    public static D newInstance() {
        if (d == null) {
            return new D();
        }
        return d;
    }
}
