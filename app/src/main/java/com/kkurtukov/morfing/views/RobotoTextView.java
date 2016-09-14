package com.kkurtukov.morfing.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.kkurtukov.morfing.utilities.font.TypefaceUtil;

public class RobotoTextView extends TextView {

    public RobotoTextView(Context context) {
        super(context);
        TypefaceUtil.initRoboto(this);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypefaceUtil.initRoboto(this);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypefaceUtil.initRoboto(this);
    }

}
