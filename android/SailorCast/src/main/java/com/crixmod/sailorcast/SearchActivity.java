package com.crixmod.sailorcast;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.crixmod.sailorcast.ui.SearchResultsActivity;
import com.crixmod.sailorcast.uiutils.SCDrawerActivity;


public class SearchActivity extends SCDrawerActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        createMenuDrawer(R.layout.activity_search);
        setSupportActionBar(getToolbar());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getString(R.string.app_name));
        }

        fixEditTextPadding();
        setSearchButton();

    }


    private void setSearchButton() {
        Button button = (Button) findViewById(R.id.search_ok);
        final EditText editText = (EditText) findViewById(R.id.search_input);
        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = editText.getText().toString();
                if(!key.isEmpty())
                    SearchResultsActivity.launch(activity,key);
            }
        });
    }

    private void fixEditTextPadding() {
        float density = getResources().getDisplayMetrics().density;
        int paddingH = (int) (16 * density);
        EditText editText = (EditText) findViewById(R.id.search_input);
        editText.setPadding(paddingH,0,paddingH,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
