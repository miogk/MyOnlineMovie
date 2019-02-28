package com.example.miogk.myonlinemovie.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.miogk.myonlinemovie.R;

public class MyBadgeView extends android.support.v7.widget.AppCompatButton {
    private int radius;
    private int text_color;
    private Paint paint;

    public MyBadgeView(Context context) {
        super(context);
    }

    public MyBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.red));
        paint.setStrokeWidth(5);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        View view = this.getRootView();
        Log.e("myBadgeView", "onDraw: " + view);
    }
}