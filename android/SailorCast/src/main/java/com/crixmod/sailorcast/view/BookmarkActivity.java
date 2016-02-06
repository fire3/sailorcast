package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.view.fragments.BookmarkFragment;

public class BookmarkActivity extends BaseToolbarActivity  {


    private BookmarkFragment mBookmarkFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookmarkFragment = BookmarkFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, mBookmarkFragment);
        ft.commit();
        getFragmentManager().executePendingTransactions();
        setTitle(getResources().getString(R.string.channel_bookmark));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bookmark;
    }


    public void alertDelete() {
        DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch( which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mBookmarkFragment.deleteSelectedBookmark();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mBookmarkFragment.setShowDeleteChecker(false);
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
        getMenuInflater().inflate(R.menu.menu_bookmark, menu);
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
            if(mBookmarkFragment.isShowDeleteChecker() == true)
                    alertDelete();
            if(mBookmarkFragment.isShowDeleteChecker() == false)
                mBookmarkFragment.setShowDeleteChecker(true);
        }

        return super.onOptionsItemSelected(item);
    }


    public static void launch(Activity activity) {
        Intent mpdIntent = new Intent(activity, BookmarkActivity.class)
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
