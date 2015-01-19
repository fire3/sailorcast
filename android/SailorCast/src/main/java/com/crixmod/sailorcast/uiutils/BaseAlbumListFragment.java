package com.crixmod.sailorcast.uiutils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.crixmod.sailorcast.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseAlbumListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public abstract class BaseAlbumListFragment extends Fragment {

    private int mCurrentPage;
    private ListView mListView;
    private View mEmptyView;
    private View mListViewFooter;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentPage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

                mCurrentPage = 1;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);


        mListView = (ListView) view.findViewById(android.R.id.list);
        //mProgressbar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mListView.setSaveEnabled(false);

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        // BEGIN_INCLUDE (change_colors)
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        mSwipeRefreshLayout.setColorScheme(
                R.color.blue_extra_light, R.color.blue_light,
                R.color.blue_medium, R.color.blue_dark);
        // END_INCLUDE (change_colors)

        mEmptyView = view.findViewById(android.R.id.empty);

        mListViewFooter = inflater.inflate(R.layout.listview_footer, null,
                false);

        //Before KitKat, we must add FooterView before we setAdapter
        //see: http://stackoverflow.com/questions/12649423/adapter-class-cast-exception-when-removing-a-footer-view
        mListView.addFooterView(mListViewFooter);
        return view;
    }

    public abstract void setListAdapter();
    public abstract void initiateRefresh();
    public abstract void loadContents(int pageNo);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter();

        mListView.removeFooterView(mListViewFooter);
        mListView.setEmptyView(mEmptyView);

        mSwipeRefreshLayout.setSwipeableChildren(view,android.R.id.list);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                initiateRefresh();
            }
        });

        mSwipeRefreshLayout.setOnLoadListener(new MultiSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                loadContents(++mCurrentPage);
            }
        });

        initiateRefresh();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
