package com.kkurtukov.morfing.utilities.font;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by kkurtukov on 25.01.2016.
 */
public class TypefaceUtil {

    public static void initRoboto(TextView v) {
        initFont(v, "Roboto-Regular.ttf");
    }

    public static void initRobotoBold(TextView v) {
        initFont(v, "Roboto-Bold.ttf");
    }

    public static void initFont(TextView v, String fontName) {
        Typeface font = Typeface.createFromAsset(v.getContext().getAssets(), fontName);
        v.setTypeface(font);
    }
}
