package com.azharkova.writemesound;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aniou_000 on 03.08.2015.
 */
public class RobotoCondensedTextView extends TextView {
    public RobotoCondensedTextView(Context context) {
        super(context);
        init(context);
    }

    public RobotoCondensedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RobotoCondensedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {

        Typeface roboto=Typeface.createFromAsset(context.getAssets(),"RobotoCondensed-Regular.ttf");
        this.setTypeface(roboto);
    }
}
