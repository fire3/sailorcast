package com.crixmod.sailorcast.model;


import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by fire3 on 2015/1/19.
 */
public class SCChannelFilter {

    ArrayMap<String,ArrayList<SCChannelFilterItem>> filters;

    public SCChannelFilter(ArrayMap<String, ArrayList<SCChannelFilterItem>> filters) {
        this.filters = filters;
    }
    public SCChannelFilter() {
        filters = new ArrayMap<>();
    }

    public void addFilter(String key, ArrayList<SCChannelFilterItem> items) {
        filters.put(key,items);
    }

    public Set<String> getFilterKeys()  {
        return filters.keySet();
    }

    public ArrayList<SCChannelFilterItem> getFilterItems(String key ) {
        return filters.get(key);
    }

}
