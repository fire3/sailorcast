package com.crixmod.sailorcast.model;

import android.util.Log;

import com.crixmod.sailorcast.SailorCast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fire3 on 15-2-2.
 */
public class SCFailLog {
    public final static int TYPE_HTTP_FAILURE = 1;
    public final static int TYPE_JSON_ERR = 2;
    public final static int TYPE_URL_ERR = 3;

    @Expose
    int type;

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

    public SCFailLog(int siteID, int  type) {
        this.site = new SCSite(siteID);
        this.type = type;
    }

    public void setException(Exception e) {
        if(e!=null)
            exceptionString = Log.getStackTraceString(e);
    }

    public void setReason(String reason) {
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
