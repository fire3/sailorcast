package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.view.fragments.HistoryFragment;

public class HistoryActivity extends BaseToolbarActivity  {


    private HistoryFragment mHistoryFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHistoryFragment = HistoryFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, mHistoryFragment);
        ft.commit();
        getFragmentManager().executePendingTransactions();
        setTitle(getResources().getString(R.string.channel_history));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_history;
    }


    public void alertDelete() {
        DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch( which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mHistoryFragment.deleteSelectedHistory();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mHistoryFragment.setShowDeleteChecker(false);
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.alert_delete_message))
                .setPositiveButton(getResources().getString(R.string.btn_ok), dialogClick)
                .setNegativeButton(getResources().getString(R.string.btn_cancel), dialogClick).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        }


        if (id == R.id.action_delete) {
            if(mHistoryFragment.isShowDeleteChecker() == true)
                alertDelete();
            if(mHistoryFragment.isShowDeleteChecker() == false)
                mHistoryFragment.setShowDeleteChecker(true);
        }

        return super.onOptionsItemSelected(item);
    }


    public static void launch(Activity activity) {
        Intent mpdIntent = new Intent(activity, HistoryActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(mpdIntent);
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
