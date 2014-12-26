package com.crixmod.sailorcast.siteapi;

import com.squareup.okhttp.Callback;

/**
 * Created by fire3 on 14-12-26.
 */
abstract public class BaseSiteApi {
    abstract public void doSearch(String key, OnSearchRequestListener listener) throws Exception;
}
