package com.crixmod.sailorcast.uiutils;

/**
 * Created by fire3 on 14-12-26.
 */


import android.content.Context;

import com.crixmod.sailorcast.uiutils.prefs.AppPrefs;

public enum ActivityId {
    UNKNOWN("Unknown"),
    READER("Reader"),
    NOTIFICATIONS("Notifications");

    private final String mStringValue;

    private ActivityId(final String stringValue) {
        mStringValue = stringValue;
    }

    public String toString() {
        return mStringValue;
    }

    public static void trackLastActivity(Context context, ActivityId activityId) {
        if (activityId != null) {
            AppPrefs.setLastActivityStr(activityId.name());
        }
    }

    /**
     * Map special cases of activities that can't be restored
     */
    public ActivityId autoRestoreMapper() {
        switch (this) {
            // All other screen can be restored
            default:
                return this;
        }
    }

    public static ActivityId getActivityIdFromName(String activityString) {
        if (activityString == null) {
            return ActivityId.UNKNOWN;
        }
        try {
            return ActivityId.valueOf(activityString);
        } catch (IllegalArgumentException e) {
            // default to UNKNOWN in case the activityString is bogus
            return ActivityId.UNKNOWN;
        }
    }
}
