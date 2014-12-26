package com.crixmod.sailorcast.utils;

/**
 * Created by fire3 on 14-12-26.
 */




import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.crixmod.sailorcast.ui.ActivityId;
import com.sailorcast.android.util.DisplayUtils;

public class SCActivityUtils {

    public static Context getThemedContext(Context context) {
        if (context instanceof ActionBarActivity) {
            ActionBar actionBar = ((ActionBarActivity)context).getSupportActionBar();
            if (actionBar != null) {
                return actionBar.getThemedContext();
            }
        }
        return context;
    }

    public static Intent getIntentForActivityId(Context context, ActivityId id) {
        final Intent intent;
        switch (id) {
            /*
            case NOTIFICATIONS:
                intent = new Intent(context, NotificationsActivity.class);
                break;
            case READER:
                intent = new Intent(context, ReaderPostListActivity.class);
                break;
            */
            default:
                intent = null;
                break;
        }

        return intent;
    }

    /*
     * returns the optimal pixel width to use for the menu drawer based on:
     * http://www.google.com/design/spec/layout/structure.html#structure-side-nav
     * http://www.google.com/design/spec/patterns/navigation-drawer.html
     * http://android-developers.blogspot.co.uk/2014/10/material-design-on-android-checklist.html
     * https://medium.com/sebs-top-tips/material-navigation-drawer-sizing-558aea1ad266
     */
    public static int getOptimalDrawerWidth(Context context) {
        Point displaySize = DisplayUtils.getDisplayPixelSize(context);
        int appBarHeight = DisplayUtils.getActionBarHeight(context);
        int drawerWidth = Math.min(displaySize.x, displaySize.y) - appBarHeight;
        int maxDp = (DisplayUtils.isXLarge(context) ? 400 : 320);
        int maxPx = DisplayUtils.dpToPx(context, maxDp);
        return Math.min(drawerWidth, maxPx);
    }

}
