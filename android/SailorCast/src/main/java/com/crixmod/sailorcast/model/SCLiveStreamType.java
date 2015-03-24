package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 15-3-11.
 */

public class SCLiveStreamType {
    private int typeId;
    public final static String TYPE_CAST_NAME = "轮播频道";  //轮播台
    public final static int TYPE_CAST = 0;  //轮播台

    public final static int TYPE_PROVINCE_TV = 1;  //卫视频道
    public final static String TYPE_PROVINCE_TV_NAME = "卫视频道";  //卫视频道

    public final static String TYPE_CCTV_NAME = "央视频道";  //轮播台
    public final static int TYPE_CCTV = 2;  //cctv

    public SCLiveStreamType(int typeId) {
        this.typeId = typeId;
    }

    public static int getTypeCount() {
        return 2;
        //央视频道暂时不提供使用。
    }

    public int getTypeId() {
        return typeId;
    }

    public static String getTypeName(int typeId) {
        if(typeId == TYPE_CAST)
            return TYPE_CAST_NAME;
        if(typeId == TYPE_CCTV)
            return TYPE_CCTV_NAME;
        if(typeId == TYPE_PROVINCE_TV)
            return TYPE_PROVINCE_TV_NAME;
        return null;
    }
}

