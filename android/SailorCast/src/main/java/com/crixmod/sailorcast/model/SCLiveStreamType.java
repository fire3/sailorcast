package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 15-3-11.
 */

public class SCLiveStreamType {
    private int typeId;
    public final static String TYPE_CAST_NAME = "轮播频道";  //轮播台
    public final static int TYPE_CAST = 0;  //轮播台
    public final static String TYPE_CCTV_NAME = "央视频道";  //轮播台
    public final static int TYPE_CCTV = 1;  //cctv
    public final static int TYPE_PROVINCE_TV = 2;  //卫视频道
    public final static String TYPE_PROVINCE_TV_NAME = "卫视频道";  //卫视频道

    public SCLiveStreamType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
}

