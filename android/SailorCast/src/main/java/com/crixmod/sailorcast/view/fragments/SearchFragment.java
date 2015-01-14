package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.view.SearchResultsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    private void fixEditTextPadding(View root) {
        float density = getResources().getDisplayMetrics().density;
        int paddingH = (int) (16 * density);
        EditText editText = (EditText) root.findViewById(R.id.search_input);
        editText.setPadding(paddingH,0,paddingH,0);
    }


    private void setSearchButton(View root) {
        Button button = (Button) root.findViewById(R.id.search_ok);
        final EditText editText = (EditText) root.findViewById(R.id.search_input);
        final Activity activity = getActivity();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = editText.getText().toString();
                if(!key.isEmpty())
                    SearchResultsActivity.launch(activity, key);
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        fixEditTextPadding(view);
        setSearchButton(view);
        return view;
    }
}
