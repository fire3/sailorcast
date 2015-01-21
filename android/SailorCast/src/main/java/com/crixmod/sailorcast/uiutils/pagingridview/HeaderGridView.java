package com.crixmod.sailorcast.uiutils.pagingridview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class HeaderGridView extends GridView {

	private ListAdapter mAdapter;
	private Context mContext;

	/**
	 * Should be used by subclasses to listen to changes in the dataset
	 */
//	AdapterDataSetObserver mDataSetObserver;
	
	public HeaderGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public HeaderGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HeaderGridView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mContext = context;
//		super.setOnScrollListener(this);
	}
	/**
     * A class that represents a fixed view in a list, for example a header at the top
     * or a footer at the bottom.
     */
	
	private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<FixedViewInfo>();
	private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<FixedViewInfo>();
    
    private void notifiyChanged(){
    	this.requestLayout();
    	this.invalidate();
    }


	/**
	 * Add a fixed view to appear at the top of the list. If this method is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * <p>
	 * Note: When first introduced, this method could only be called before
	 * setting the adapter with {@link #setAdapter(ListAdapter)}. Starting with
	 * {@link android.os.Build.VERSION_CODES#KITKAT}, this method may be
	 * called at any time. If the ListView's adapter does not extend
	 * {@link FooterViewGridAdapter}, it will be wrapped with a supporting
	 * instance of {@link android.widget.WrapperListAdapter}.
	 *
	 * @param v The view to add.
	 * @param data Data to associate with this view
	 * @param isSelectable whether the item is selectable
	 */
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		final FixedViewInfo info = new FixedViewInfo();
		FrameLayout fl = new FullWidthFixedViewLayout(getContext());
		fl.addView(v);
		info.view = v;
		info.viewContainer = fl;
		info.data = data;
		info.isSelectable = isSelectable;
		mHeaderViewInfos.add(info);

		// Wrap the adapter if it wasn't already wrapped.
		if (mAdapter != null) {
			if (!(mAdapter instanceof FooterViewGridAdapter)) {
				mAdapter = new FooterViewGridAdapter(mHeaderViewInfos, mFooterViewInfos, mAdapter);
			}

			// Do not know if this really helps
			notifiyChanged();
		}
	}



	/**
	 * Add a fixed view to appear at the top of the list. If addHeaderView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * <p>
	 * Note: When first introduced, this method could only be called before
	 * setting the adapter with {@link #setAdapter(ListAdapter)}. Starting with
	 * {@link android.os.Build.VERSION_CODES#KITKAT}, this method may be
	 * called at any time. If the ListView's adapter does not extend
	 * {@link FooterViewGridAdapter}, it will be wrapped with a supporting
	 * instance of {@link android.widget.WrapperListAdapter}.
	 *
	 * @param v The view to add.
	 */
	public void addHeaderView(View v) {
		addHeaderView(v, null, true);
	}

	public int getHeaderViewsCount() {
		return mHeaderViewInfos.size();
	}

	/**
	 * Add a fixed view to appear at the bottom of the list. If addFooterView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * <p>
	 * Note: When first introduced, this method could only be called before
	 * setting the adapter with {@link #setAdapter(ListAdapter)}. Starting with
	 * {@link android.os.Build.VERSION_CODES#KITKAT}, this method may be
	 * called at any time. If the ListView's adapter does not extend
	 * {@link FooterViewGridAdapter}, it will be wrapped with a supporting
	 * instance of {@link android.widget.WrapperListAdapter}.
	 *
	 * @param v The view to add.
	 * @param data Data to associate with this view
	 * @param isSelectable true if the footer view can be selected
	 */
	public void addFooterView(View v, Object data, boolean isSelectable) {
		final FixedViewInfo info = new FixedViewInfo();
		FrameLayout fl = new FullWidthFixedViewLayout(getContext());
		fl.addView(v);
		info.view = v;
		info.viewContainer = fl;
		info.data = data;
		info.isSelectable = isSelectable;
		mFooterViewInfos.add(info);

		// Wrap the adapter if it wasn't already wrapped.
		if (mAdapter != null) {
			if (!(mAdapter instanceof FooterViewGridAdapter)) {
				mAdapter = new FooterViewGridAdapter(mHeaderViewInfos, mFooterViewInfos, mAdapter);
			}

			// Do not know if this really helps
			notifiyChanged();
		}
	}

	/**
	 * Add a fixed view to appear at the bottom of the list. If addFooterView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * <p>
	 * Note: When first introduced, this method could only be called before
	 * setting the adapter with {@link #setAdapter(ListAdapter)}. Starting with
	 * {@link android.os.Build.VERSION_CODES#KITKAT}, this method may be
	 * called at any time. If the ListView's adapter does not extend
	 * {@link FooterViewGridAdapter}, it will be wrapped with a supporting
	 * instance of {@link android.widget.WrapperListAdapter}.
	 *
	 * @param v The view to add.
	 */
	public void addFooterView(View v) {
		addFooterView(v, null, true);
	}

	public int getFooterViewsCount() {
		return mFooterViewInfos.size();
	}

	/**
	 * Removes a previously-added footer view.
	 *
	 * @param v The view to remove
	 * @return
	 * true if the view was removed, false if the view was not a footer view
	 */
	public boolean removeFooterView(View v) {
		if (mFooterViewInfos.size() > 0) {
			boolean result = false;
			if (mAdapter != null && ((FooterViewGridAdapter) mAdapter).removeFooter(v)) {
				notifiyChanged();
				result = true;
			}
			removeFixedViewInfo(v, mFooterViewInfos);
			return result;
		}
		return false;
	}

	private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
		int len = where.size();
		for (int i = 0; i < len; ++i) {
			FixedViewInfo info = where.get(i);
			if (info.view == v) {
				where.remove(i);
				break;
			}
		}
	}


	@Override
	public void setAdapter(ListAdapter adapter) {
		this.mAdapter = adapter;

		if (mHeaderViewInfos.size() > 0|| mFooterViewInfos.size() > 0) {
			mAdapter = new FooterViewGridAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
		} else {
			mAdapter = adapter;
		}

		super.setAdapter(adapter);
		super.setAdapter(this.mAdapter);

		requestLayout();
	}

	@Override
	public ListAdapter getAdapter() {
		return this.mAdapter;
	}

	public class FixedViewInfo {
		public android.view.View view;
		public ViewGroup viewContainer;
		public java.lang.Object data;
		public boolean isSelectable;
	}

	private class FullWidthFixedViewLayout extends FrameLayout {
		public FullWidthFixedViewLayout(Context context) {
			super(context);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int targetWidth = HeaderGridView.this.getMeasuredWidth()
				- HeaderGridView.this.getPaddingLeft()
				- HeaderGridView.this.getPaddingRight();
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth,
				MeasureSpec.getMode(widthMeasureSpec));
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
