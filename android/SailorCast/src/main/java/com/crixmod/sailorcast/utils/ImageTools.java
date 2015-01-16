package com.crixmod.sailorcast.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Created by fire3 on 2015/1/7.
 */
public class ImageTools {
    public static final float VER_POSTER_RATIO = 0.73f;
    public static final float HOR_POSTER_RATIO = 1.5f;


    public static void displayImage(ImageView view, String picUrl, int height, int width) {

        Picasso.with(view.getContext()).load(picUrl).resize(height,width).centerCrop().into(view);
    }

    public static void displayImage(ImageView view, String picUrl) {

        Picasso.with(view.getContext()).load(picUrl).into(view);
    }


    public static void fixHorPosterRatio(final ImageView image) {
            image.post(new Runnable() {
                @Override
                public void run() {
                    int width = image.getWidth();
                    int height = Math.round((float) width / HOR_POSTER_RATIO);
                    image.getLayoutParams().height = height;
                    image.setVisibility(View.VISIBLE);
                }
            });
    }


    public static void fixVerPosterRatio(final ImageView image) {
        image.post(new Runnable() {
            @Override
            public void run() {
                int width = image.getWidth();
                int height = Math.round((float) width / VER_POSTER_RATIO);
                image.getLayoutParams().height = height;
                image.setVisibility(View.VISIBLE);
            }
        });
    }

    public static Point getGridVerPosterSize(Context mContext) {
        int width = getScreenWidthPixels(mContext)/3;
        width = width - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.margin_small);
        int height = Math.round((float) width / VER_POSTER_RATIO);
        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }
    public static Point getGridHorPosterSize(Context mContext) {
        int width = getScreenWidthPixels(mContext)/3;
        width = width - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.margin_small);
        int height = Math.round((float) width / HOR_POSTER_RATIO);
        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }


    public static int getScreenWidthPixels(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getScreenHeightPixels(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }


    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

}
