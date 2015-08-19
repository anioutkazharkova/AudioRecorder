package com.azharkova.writemesound;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aniou_000 on 09.08.2015.
 */
public class RobotoCondensedLightTextView extends TextView {
    public RobotoCondensedLightTextView(Context context) {
        super(context);
        init(context);
    }

    public RobotoCondensedLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RobotoCondensedLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {

        Typeface roboto=Typeface.createFromAsset(context.getAssets(),"RobotoCondensed-Light.ttf");
        this.setTypeface(roboto);
    }
}