package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.view.fragments.SearchResultFragment;

public class SearchResultsActivity extends BaseToolbarActivity {

    private static final String EXTRA_KEYWORD = "SearchResultsActivity:keyword";

    private FrameLayout mFragContainer;
    private String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());



    }

    private void doMySearch(String mKeyword) {
        if(mKeyword != null) {
            mFragContainer = (FrameLayout) findViewById(R.id.fragment_container);
            if (mFragContainer != null) {
                Fragment fragment = SearchResultFragment.newInstance(mKeyword);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        }
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search_results;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void launch(Activity activity, String keyword) {
        Intent mpdIntent = new Intent(activity, SearchResultsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(SearchResultsActivity.EXTRA_KEYWORD, keyword);

        activity.startActivity(mpdIntent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mKeyword = query;
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            //Uri data = intent.getData();
            String dataString = intent.getDataString();
            mKeyword = dataString;
        }
        doMySearch(mKeyword);
    }
        @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    
}

