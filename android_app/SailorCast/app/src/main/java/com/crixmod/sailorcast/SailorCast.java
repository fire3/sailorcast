package com.crixmod.sailorcast;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by fire3 on 14-12-26.
 */
public class SailorCast extends Application {
    private static Context mContext;
    private static OkHttpClient mHttpClient;
    private static Gson mGson;
    private static String FLV_API = "http://api.flvxz.com/token/9058e263a95c0dfbc1bdac83f4132822";
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHttpClient = new OkHttpClient();
        mGson = new Gson();
    }

    public static Context getContext() {
        return mContext;
    }

    public static OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    public static Gson getGson() {
        return mGson;
    }

    public static String getIMEI()
    {
       try
        {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI_Number = telephonyManager.getDeviceId();
            if ((IMEI_Number != null) && (IMEI_Number.length() > 0))
            {
                return IMEI_Number;
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return null;
    }


    public static boolean checkPermission(Context paramContext, String paramString)
    {
        return paramContext.checkCallingOrSelfPermission(paramString) == PackageManager.PERMISSION_GRANTED;
    }

    public static String getMacAddress()
    {
        try
        {
            WifiManager localWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(mContext, "android.permission.ACCESS_WIFI_STATE")) {
                return localWifiManager.getConnectionInfo().getMacAddress();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "00:11:22:33:44:55";
    }

}
