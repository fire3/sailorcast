package com.crixmod.sailorcast.uiutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.crixmod.sailorcast.utils.ImageTools;

/**
 * Created by fire3 on 15-1-8.
 */
public class VerPosterImageView extends ImageView {
    public VerPosterImageView(Context context) {
        super(context);
    }

    public VerPosterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerPosterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = Math.round(getMeasuredWidth() / ImageTools.VER_POSTER_RATIO);
        setMeasuredDimension(getMeasuredWidth(), height); //Snap to width
    }
}
