package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCChannelFilterItem;

import java.util.ArrayList;

/**
 * Created by fire3 on 15-1-22.
 */
public class AlbumFilterDialog extends DialogFragment {
    private SCChannelFilter mFilter;
    private int mChannelID;
    private int mSiteID;

    private static final String ARG_CHANNEL_ID = "channelID";
    private static final String ARG_FILTER = "filter";
    private static final String ARG_SITE_ID = "siteID";
    private View mRootView;
    private OnAlbumFilterDialogAction mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_filter,container,false);
        mRootView = view;
        fillView(mFilter);
        return view;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAlbumFilterDialogAction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void fillView(SCChannelFilter filter) {
        LinearLayout container = (LinearLayout)mRootView.findViewById(R.id.filter_container);
        LinearLayout.LayoutParams p;

        for (int i = 0; i < filter.getFilterKeys().size(); i++) {


            String key = (String) filter.getFilterKeys().toArray()[i];

            ArrayList<SCChannelFilterItem> items = filter.getFilterItemsByKey(key);

            //Begin add divider
            ImageView divider = new ImageView(getActivity());
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
            p.setMargins(0, 5, 0, 5);
            divider.setBackgroundColor(getResources().getColor(R.color.grey_medium_light));
            divider.setLayoutParams(p);
            container.addView(divider);
            //End add divider


            //Begin add Table
            TableLayout tableLayout = new TableLayout(getActivity());
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(10,0,10,0);
            tableLayout.setLayoutParams(p);
            tableLayout.setStretchAllColumns(true);

            int columnCount = 5;
            int cateCount = items.size();
            int count = ((cateCount % columnCount) == 0) ? cateCount / columnCount : (cateCount / columnCount + 1);

            //计算最宽的一行字数
            int lineLength = 0;
            int maxLength = 0;
            for (int j = 0; j < count; j++) {
                lineLength = 0;
                for (int k = 0; k < columnCount; k++) {
                    int index = j * columnCount + k;
                    if (index < cateCount) {
                        lineLength = lineLength + items.get(index).getDisplayName().length();
                    }
                }

                if(lineLength > maxLength)
                    maxLength = lineLength;
            }

            //如果最宽的一行超过13个字，则设置成3列显示
            if(maxLength > 13)
                columnCount = 3;

            //重新根据3列进行计算。
            cateCount = items.size();
            count = ((cateCount % columnCount) == 0) ? cateCount / columnCount : (cateCount / columnCount + 1);

            for (int j = 0; j < count; j++) {
                TableRow row = new TableRow(getActivity());
                TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 3, 0, 3);
                row.setLayoutParams(lp);
                tableLayout.addView(row);
                for (int k = 0; k < columnCount; k++) {
                    int index = j * columnCount + k;
                    if (index < cateCount) {
                        CheckedTextView text = new CheckedTextView(getActivity());
                        TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        param.setMargins(3, 3, 3, 3);
                        text.setLayoutParams(param);
                        text.setTextSize(15);
                        text.setEllipsize(TextUtils.TruncateAt.END);
                        text.setText(items.get(index).getDisplayName());
                        text.setTag(R.id.key_filter_item, items.get(index));
                        items.get(index).setChecked(false);
                        int[][] states = new int[][]{
                                new int[]{android.R.attr.state_checked}, // checked
                                new int[]{-android.R.attr.state_checked}, // unchecked
                        };

                        int[] colors = new int[]{
                                getResources().getColor(R.color.item_checked),
                                getResources().getColor(R.color.grey_medium_light),
                        };

                        ColorStateList myList = new ColorStateList(states, colors);
                        text.setTextColor(myList);
                        text.setClickable(true);
                        text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TableLayout table = (TableLayout) view.getParent().getParent();
                                for (int l = 0; l < table.getChildCount(); l++) {
                                    TableRow row = (TableRow) table.getChildAt(l);
                                    for (int i = 0; i < row.getChildCount(); i++) {
                                        ((CheckedTextView) (row.getChildAt(i))).setChecked(false);
                                        SCChannelFilterItem item = (SCChannelFilterItem) row.getChildAt(i).getTag(R.id.key_filter_item);
                                        item.setChecked(false);
                                    }
                                }
                                ((CheckedTextView) view).setChecked(true);
                                SCChannelFilterItem item = (SCChannelFilterItem) view.getTag(R.id.key_filter_item);
                                item.setChecked(true);
                            }
                        });
                        if (j == 0 && k == 0) {
                            text.setChecked(true);
                            SCChannelFilterItem item = (SCChannelFilterItem) text.getTag(R.id.key_filter_item);
                            item.setChecked(true);
                        }
                        row.addView(text);
                    }
                }
            }
            container.addView(tableLayout);
            //End add table
        }
        //Add OK and cancel Button

        if(filter.getFilterKeys().size() > 0) {

            LinearLayout buttonContainer = new LinearLayout(getActivity());
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0,20,0,5);
            buttonContainer.setLayoutParams(p);
            buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
            buttonContainer.setGravity(Gravity.CENTER_HORIZONTAL);

            Button okButton = new Button(getActivity());
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(20, 5, 20, 5);
            //okButton.setBackground(getResources().getDrawable(R.drawable.square_border));
            okButton.setBackgroundResource(R.drawable.square_border);
            okButton.setLayoutParams(p);
            okButton.setText(getResources().getString(R.string.btn_ok));

            Button cancelButton = new Button(getActivity());
            p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(20, 5, 20, 5);
            cancelButton.setLayoutParams(p);
            cancelButton.setBackgroundResource(R.drawable.square_border);
            //cancelButton.setBackground(getResources().getDrawable(R.drawable.square_border));
            cancelButton.setText(getResources().getString(R.string.btn_cancel));

            buttonContainer.addView(cancelButton);
            buttonContainer.addView(okButton);

            container.addView(buttonContainer);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onAlbumFilterSelected(mFilter);
                    dismiss();
                }
            });

        }
    }


    public static AlbumFilterDialog newInstance(int mSiteID, int mChannelID, SCChannelFilter filter) {
        AlbumFilterDialog fragment = new AlbumFilterDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_CHANNEL_ID, mChannelID);
        args.putInt(ARG_SITE_ID, mSiteID);
        args.putString(ARG_FILTER,filter.toJson());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSiteID = getArguments().getInt(ARG_SITE_ID);
            mChannelID = getArguments().getInt(ARG_CHANNEL_ID);
            mFilter = SCChannelFilter.fromJson(getArguments().getString(ARG_FILTER));
        }

    }

    public interface OnAlbumFilterDialogAction {
        public void onAlbumFilterSelected(SCChannelFilter filter);
    }



}
