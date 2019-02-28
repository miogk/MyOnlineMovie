package com.example.miogk.myonlinemovie.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MyCustomRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    public MyCustomRadioButton(Context context) {
        super(context);
    }

    public MyCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void toggle() {
//        super.toggle();
        setChecked(!isChecked());
        if (!isChecked()) {
            ((RadioGroup) getParent()).clearCheck();
        }
    }
}
