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
    String tag;

    @Expose
    String functionName;

    @Expose
    String className;

    @Expose
    SCSite site;

    @Expose
    String url;

    @Expose
    String Reason;

    @Expose
    String exceptionString = null;

    public SCFailLog(int siteID, String reason, Exception e) {
        this.site = new SCSite(siteID);
        Reason = reason;
        if(e!=null)
            exceptionString = Log.getStackTraceString(e);
    }
    public SCFailLog(int siteID, String reason) {
        this.site = new SCSite(siteID);
        Reason = reason;
    }

    public String toJson() {
        return  SailorCast.getGson().toJson(this);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
