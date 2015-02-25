package com.crixmod.sailorcast.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by fire3 on 2015/2/25.
 */
public final class BrightControl {

    public static void setBrightness(Context paramContext, int paramInt)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= 19)
            {
                setScreenBrightness(paramContext, paramInt);
                return;
            }
            if ((paramContext instanceof Activity))
            {
                WindowManager.LayoutParams localLayoutParams = ((Activity)paramContext).getWindow().getAttributes();
                if (paramInt <= 20) {
                    paramInt = 20;
                }
                localLayoutParams.screenBrightness = (paramInt / 255.0F);
                ((Activity)paramContext).getWindow().setAttributes(localLayoutParams);
                return;
            }
        }
        catch (Exception localException)
        {
            Toast.makeText(paramContext, "无法改变亮度", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean a(Context paramContext)
    {
        ContentResolver localContentResolver = paramContext.getContentResolver();
        try
        {
            int i = Settings.System.getInt(localContentResolver, "screen_brightness_mode");
            return i == 1;
        }
        catch (Exception localException)
        {
            Toast.makeText(paramContext, "无法获取亮度", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static int getBrightness(Context paramContext)
    {
        return Settings.System.getInt(paramContext.getContentResolver(), "screen_brightness", -1);
    }

    public static void setScreenBrightness(Context paramContext, int paramInt)
    {
        Settings.System.putInt(paramContext.getContentResolver(), "screen_brightness", paramInt);
    }

    public static int getSharedPreferencesLight(Context paramContext)
    {
        return PreferenceManager.getDefaultSharedPreferences(paramContext).getInt("shared_preferences_light", -1);
    }

}
