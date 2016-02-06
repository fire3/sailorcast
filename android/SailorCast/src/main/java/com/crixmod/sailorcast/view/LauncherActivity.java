package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.view.fragments.LauncherFragment;
import com.umeng.update.UmengUpdateAgent;

public class LauncherActivity extends BaseToolbarActivity {

    private LauncherFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(getString(R.string.app_name));
        mFragment = LauncherFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, mFragment);
        ft.commit();
        getFragmentManager().executePendingTransactions();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_launcher;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            onSearchRequested();
        }

        return super.onOptionsItemSelected(item);
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
