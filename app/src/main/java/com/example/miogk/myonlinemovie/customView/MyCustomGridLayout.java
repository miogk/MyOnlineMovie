package com.example.miogk.myonlinemovie.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.miogk.myonlinemovie.R;

public class MyCustomGridLayout extends ViewGroup {
    int mCellWidth;
    int mCellHeight;
    private static final String TAG = "MyCustomGridLayout";

    public MyCustomGridLayout(Context context) {
        super(context);
    }

    public MyCustomGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.myCustomGridLayout);
        mCellWidth = typedArray.getDimensionPixelSize(R.styleable.myCustomGridLayout_cellWidth, -1);
        mCellHeight = typedArray.getDimensionPixelSize(R.styleable.myCustomGridLayout_cellHeight, -1);
        typedArray.recycle();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyCustomGridLayoutLayout(getContext(), attrs);
    }

    public static class MyCustomGridLayoutLayout extends ViewGroup.MarginLayoutParams {

        public MyCustomGridLayoutLayout(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }

    public void setmCellHeight(int dp) {
        this.mCellHeight = dp;
        requestLayout();
    }

    public void setCellWidth(int dp) {
        this.mCellWidth = dp;
        requestLayout();
    }

    //l=left, t=top, r=right, b=bottom
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int posX = 0;
        int posY = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MyCustomGridLayoutLayout layoutLayout = (MyCustomGridLayoutLayout) child.getLayoutParams();
            //获得margin和padding
            int leftMargin = layoutLayout.leftMargin;
            int rightMargin = layoutLayout.rightMargin;
            int topMargin = layoutLayout.topMargin;
            int bottomMargin = layoutLayout.bottomMargin;
            int leftPadding = child.getPaddingLeft();
            int rightPadding = child.getPaddingRight();
            int topPadding = child.getPaddingTop();
            int bottomPadding = child.getPaddingBottom();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int totalWidth = leftMargin + leftPadding + rightMargin + rightPadding;
            int totalHeight = topMargin + bottomMargin + topPadding + bottomPadding;
            if (posX + childWidth + totalWidth > width) {
                posX = left;
                posY += childHeight + topMargin + topPadding;
            }
            child.layout(posX + leftMargin + leftPadding, posY + topMargin + topPadding, posX + childWidth + totalWidth, posY + childHeight + totalHeight);
            posX += childWidth + totalWidth;
        }
//        int cellWidth = mCellWidth;
//        int cellHeight = mCellHeight;
//        int columns = (r - l) / cellWidth;
//        if (columns < 0) {
//            columns = 1;
//        }
//        int x = 0;
//        int y = 0;
//        int i = 0;
//        int count = getChildCount();
//        for (int index = 0; index < count; index++) {
//            final View child = getChildAt(index);
//
//            int w = child.getMeasuredWidth();
//            int h = child.getMeasuredHeight();
//
//            int left = x + ((cellWidth - w) / 2);
//            int top = y + ((cellHeight - h) / 2);
//
//            child.layout(left, top, left + w, top + h);
//            if (i >= (columns - 1)) {
//                // advance to next row
//                i = 0;
//                x = 0;
//                y += cellHeight;
//            } else {
//                i++;
//                x += cellWidth;
//            }
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //onMeasure: 1073742904 -- -2147482075
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int allChildWidth = 0;
        int allChildHeight = 0;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
//        int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST);
//        int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST);
//
//        int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            child.measure(cellWidthSpec, cellHeightSpec);
//        }
//        setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
//                resolveSize(mCellHeight * count, heightMeasureSpec));

        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }
}
