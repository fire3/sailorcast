package com.crixmod.sailorcast.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by fire3 on 2015/1/7.
 */
public class ImageTools {
    public static void displayImage(ImageView view, String picUrl, final DisplayImageOptions displayImageOptions) {
        final String pic = picUrl;
        final ImageView imageView = view;
        imageView.post(new Runnable() {
            @Override
            public void run() {
                ImageAware imageAware = new ImageViewAware(imageView, false);

                if (SailorCast.isNetworkMobile()) {
                    //mobile
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("drawable://" + R.drawable.pic_defaultposter, imageAware, displayImageOptions);
                } else if (SailorCast.isNetworkFWifi()) {
                    //wifi
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(pic, imageAware, displayImageOptions);
                }
            }
        });
    }

    public static void displayImage(ImageView view, String picUrl, Drawable failDrawable) {
        final String pic = picUrl;
        final ImageView imageView = view;
        final DisplayImageOptions displayImageOptions =
                new DisplayImageOptions.Builder()
                        .resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .showImageOnLoading(failDrawable)
                        .showImageOnFail(failDrawable)
                        //.displayer(new FadeInBitmapDisplayer(100))
                        .delayBeforeLoading(10)
                        //.imageScaleType(ImageScaleType.EXACTLY)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

        imageView.post(new Runnable() {
            @Override
            public void run() {
                ImageAware imageAware = new ImageViewAware(imageView, false);

                if (SailorCast.isNetworkMobile()) {
                    //mobile
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("drawable://" + R.drawable.pic_defaultposter, imageAware, displayImageOptions);
                } else if (SailorCast.isNetworkFWifi()) {
                    //wifi
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(pic, imageAware, displayImageOptions);
                }
            }
        });
    }

    public static void displayImage(ImageView view, String picUrl) {
        final String pic = picUrl;
        //Drawable failDrawable = view.getContext().getResources().getDrawable(R.drawable.sohu_pic_defaultposter);
        final ImageView imageView = view;
        final DisplayImageOptions displayImageOptions =
                new DisplayImageOptions.Builder()
                        .resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        //.showImageOnLoading(failDrawable)
                        //.showImageOnFail(failDrawable)
                        //.displayer(new FadeInBitmapDisplayer(100))
                        .delayBeforeLoading(10)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();

        imageView.post(new Runnable() {
            @Override
            public void run() {
                ImageAware imageAware = new ImageViewAware(imageView, false);

                if (SailorCast.isNetworkMobile()) {
                    //mobile
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("drawable://" + R.drawable.pic_defaultposter, imageAware, displayImageOptions);
                } else if (SailorCast.isNetworkFWifi()) {
                    //wifi
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(pic, imageAware, displayImageOptions);
                }
            }
        });
    }


    public static void fixHorPosterRatio(final ImageView image) {
            image.post(new Runnable() {
                @Override
                public void run() {
                    int width = image.getWidth();
                    int height = Math.round((float) width / 240.0f * 180.0f);
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
                int height = Math.round((float) width / 240.0f * 330.0f);
                image.getLayoutParams().height = height;
                image.setVisibility(View.VISIBLE);
            }
        });
    }




}
