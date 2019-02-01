package com.example.miogk.myonlinemovie.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.miogk.myonlinemovie.R;

public class MyCustomView extends View {
    private float num;
    private Paint paint = new Paint();
    private Context context;
    private static final String TAG = "MyCustomView";


    public MyCustomView(Context context, float num) {
        super(context);
        this.num = num;
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setNum(float num) {
        this.num = num;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(R.color.yellow));
        RectF rect = new RectF(0, 0, num, 30);
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) num, 30);
        layoutParams.gravity = Gravity.CENTER;
        this.setLayoutParams(layoutParams);
    }
}