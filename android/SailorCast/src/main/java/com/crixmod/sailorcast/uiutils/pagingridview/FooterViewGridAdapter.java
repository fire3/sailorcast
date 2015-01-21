/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crixmod.sailorcast.uiutils.pagingridview;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import java.util.ArrayList;

/**
 * ListAdapter used when a HeaderGridView has header views. This ListAdapter
 * wraps another one and also keeps track of the header views and their
 * associated data objects.
 *<p>This is intended as a base class; you will probably not need to
 * use this class directly in your own code.
 */
public class FooterViewGridAdapter implements WrapperListAdapter, Filterable {

    private final ListAdapter mAdapter;

    // These two ArrayList are assumed to NOT be null.
    // They are indeed created when declared in HeaderGridView and then shared.
    ArrayList<HeaderGridView.FixedViewInfo> mFooterViewInfos;

    // Used as a placeholder in case the provided info views are indeed null.
    // Currently only used by some CTS tests, which may be removed.
    static final ArrayList<HeaderGridView.FixedViewInfo> EMPTY_INFO_LIST =
        new ArrayList<HeaderGridView.FixedViewInfo>();

    boolean mAreAllFixedViewsSelectable;

    private final boolean mIsFilterable;

    public FooterViewGridAdapter(ArrayList<HeaderGridView.FixedViewInfo> headerViewInfos,
                                 ArrayList<HeaderGridView.FixedViewInfo> footerViewInfos,
                                 ListAdapter adapter) {
        mAdapter = adapter;
        mIsFilterable = adapter instanceof Filterable;



        if (footerViewInfos == null) {
            mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            mFooterViewInfos = footerViewInfos;
        }

        mAreAllFixedViewsSelectable = areAllListInfosSelectable(mFooterViewInfos);
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    public boolean isEmpty() {
        return mAdapter == null || mAdapter.isEmpty();
    }

    private boolean areAllListInfosSelectable(ArrayList<HeaderGridView.FixedViewInfo> infos) {
        if (infos != null) {
            for (HeaderGridView.FixedViewInfo info : infos) {
                if (!info.isSelectable) {
                    return false;
                }
            }
        }
        return true;
    }



    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
	        HeaderGridView.FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.view == v) {
                mFooterViewInfos.remove(i);

                mAreAllFixedViewsSelectable = areAllListInfosSelectable(mFooterViewInfos);

                return true;
            }
        }

        return false;
    }

    public int getCount() {
        if (mAdapter != null) {
            return getFootersCount() + mAdapter.getCount();
        } else {
            return getFootersCount();
        }
    }

    public boolean areAllItemsEnabled() {
        if (mAdapter != null) {
            return mAreAllFixedViewsSelectable && mAdapter.areAllItemsEnabled();
        } else {
            return true;
        }
    }

    public boolean isEnabled(int position) {
        // Adapter
        final int adjPosition = position;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.isEnabled(adjPosition);
            }
        }

        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return mFooterViewInfos.get(adjPosition - adapterCount).isSelectable;
    }

    public Object getItem(int position) {
        // Adapter
        final int adjPosition = position;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItem(adjPosition);
            }
        }

        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return mFooterViewInfos.get(adjPosition - adapterCount).data;
    }

    public long getItemId(int position) {
        if (mAdapter != null) {
            int adjPosition = position;
            int adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    public boolean hasStableIds() {
        if (mAdapter != null) {
            return mAdapter.hasStableIds();
        }
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Adapter
        final int adjPosition = position;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getView(adjPosition, convertView, parent);
            }
        }

        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return mFooterViewInfos.get(adjPosition - adapterCount).viewContainer;
    }

    public int getItemViewType(int position) {
        if (mAdapter != null) {
            int adjPosition = position;
            int adapterCount = mAdapter.getCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        return AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
    }

    public int getViewTypeCount() {
        if (mAdapter != null) {
            return mAdapter.getViewTypeCount();
        }
        return 1;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(observer);
        }
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(observer);
        }
    }

    public Filter getFilter() {
        if (mIsFilterable) {
            return ((Filterable) mAdapter).getFilter();
        }
        return null;
    }
    
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }
}
