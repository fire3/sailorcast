package com.crixmod.sailorcast.ui;

import android.content.Context;

import com.crixmod.sailorcast.R;

import java.util.ArrayList;

/*
 * used by DrawerAdapter to maintain a list of items in the drawer
 */

public class DrawerItems {

    private final ArrayList<DrawerItem> mItems = new ArrayList<DrawerItem>();

    public DrawerItems() {
        super();
        refresh();
    }

    private void addIfVisible(DrawerItemId itemId) {
        DrawerItem item = new DrawerItem(itemId);
        if (item.isVisible()) {
            mItems.add(item);
        }
    }

    /*
     * reset the item list and add all items that should be visible
     */
    void refresh() {
        mItems.clear();

        addIfVisible(DrawerItemId.READER);
        addIfVisible(DrawerItemId.NOTIFICATIONS);
    }

    int size() {
        return mItems.size();
    }

    DrawerItem get(int position) {
        if (position < 0 || position >= mItems.size()) {
            return null;
        }
        return mItems.get(position);
    }

    boolean hasSelectedItem(Context context) {
        for (DrawerItem item: mItems) {
            if (item.isSelected(context)) {
                return true;
            }
        }
        return false;
    }

    /*
     *
     */
    enum DrawerItemId {
        READER,
        NOTIFICATIONS;

        ActivityId toActivityId() {
            switch (this) {
                case READER:
                    return ActivityId.READER;
                case NOTIFICATIONS:
                    return ActivityId.NOTIFICATIONS;
                default :
                    return ActivityId.UNKNOWN;
            }
        }
    }

    /*
     *
     */
    static class DrawerItem {
        private final DrawerItemId mItemId;

        DrawerItem(DrawerItemId itemId) {
            mItemId = itemId;
        }

        DrawerItemId getDrawerItemId() {
            return mItemId;
        }

        int getTitleResId() {
            switch (mItemId) {
                case READER:
                    return R.string.reader;
                case NOTIFICATIONS:
                    return R.string.notifications;
                default :
                    return 0;
            }
        }

        int getIconResId() {
            switch (mItemId) {
                case READER:
                    return R.drawable.noticon_reader_alt_black;
                case NOTIFICATIONS:
                    return R.drawable.noticon_notification_black;
                default :
                    return 0;
            }
        }

        int getBadgeCount() {
            return 0;
        }

        boolean isSelected(Context context) {
            switch (mItemId) {
                /*
                case READER:
                    return context instanceof ReaderPostListActivity;
                case NOTIFICATIONS:
                    return context instanceof NotificationsActivity;
                */
                default :
                    return false;
            }
        }

        boolean isVisible() {
            switch (mItemId) {
                case READER:
                    return true;
                case NOTIFICATIONS:
                    return true;
                default :
                    return false;
            }
        }

        /*
         * returns true if the item should have a divider beneath it
         */
        boolean hasDivider() {
            return (mItemId == DrawerItemId.NOTIFICATIONS);
        }
    }
}
