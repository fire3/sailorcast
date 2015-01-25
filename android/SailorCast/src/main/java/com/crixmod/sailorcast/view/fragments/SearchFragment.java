package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.siteapi.YouKuApi;
import com.crixmod.sailorcast.uiutils.DelayAutoCompleteTextView;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.crixmod.sailorcast.view.SearchResultsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        AutoCompleteTextView editText = (AutoCompleteTextView) root.findViewById(R.id.search_input);
        editText.setPadding(paddingH,0,paddingH,0);
    }


    private void setSearchButton(View root) {

        final DelayAutoCompleteTextView searchInput = (DelayAutoCompleteTextView) root.findViewById(R.id.search_input);
        searchInput.setThreshold(1);
        searchInput.setAdapter(new AutoCompleteAdapter(getActivity())); // 'this' is Activity instance
        searchInput.setLoadingIndicator(
                (android.widget.ProgressBar) root.findViewById(R.id.pb_loading_indicator));
        searchInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String res = (String) adapterView.getItemAtPosition(position);
                searchInput.setText(res);
            }
        });

        Button button = (Button) root.findViewById(R.id.search_ok);
        final AutoCompleteTextView editText = (AutoCompleteTextView) root.findViewById(R.id.search_input);
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


    public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

        private static final int MAX_RESULTS = 10;
        private Context mContext;
        private List<String> resultList = new ArrayList<String>();

        public AutoCompleteAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.simple_dropdown_item_2line, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position));
            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        List<String> results = findSuggestions(mContext, constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        resultList = (List<String>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }

        /**
         * Returns a search result for the given book title.
         */
        private List<String> findSuggestions(Context context, String key) {
            // GoogleBooksProtocol is a wrapper for the Google Books API
            String url = new YouKuApi().getKeyWordsSuggestionUrl(key);
            String results = HttpUtils.syncGet(url);

            try {
                JSONObject retJson = new JSONObject(results);
                String status = retJson.optString("status");
                if(status!=null && status.equals("success")) {
                    List<String> ret = new ArrayList<>();
                    JSONArray retsJson = retJson.optJSONArray("results");
                    for (int i = 0; i < retsJson.length(); i++) {
                        JSONObject retJ  = retsJson.getJSONObject(i);
                        String keyword = retJ.optString("keyword");
                        if(keyword!=null)
                            ret.add(keyword);
                    }
                    if(ret.size() > 0)
                        return ret;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

}
