package com.crixmod.sailorcast.model;

import android.util.Log;

import com.crixmod.sailorcast.SailorCast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fire3 on 15-2-2.
 */
public class SCFailLog {
    @Expose
    SCSite site;

    @Expose
    String url;

    @Expose
    String Reason;

    @Expose
    String exceptionString = null;


    public SCFailLog(int siteID, String url, String reason, Exception e) {
        this.site = new SCSite(siteID);
        this.url = url;
        Reason = reason;
        if(e!=null)
            exceptionString = Log.getStackTraceString(e);
    }
    public SCFailLog(int siteID, String url, String reason) {
        this.site = new SCSite(siteID);
        this.url = url;
        Reason = reason;
    }

    public String toJson() {
        return  SailorCast.getGson().toJson(this);
    }


}
