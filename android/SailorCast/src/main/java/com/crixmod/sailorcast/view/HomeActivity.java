package com.crixmod.sailorcast.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.view.fragments.BookmarkFragment;
import com.crixmod.sailorcast.view.fragments.CompassFragment;
import com.crixmod.sailorcast.view.fragments.HistoryFragment;
import com.crixmod.sailorcast.view.fragments.SearchFragment;

public class HomeActivity extends BaseToolbarActivity {
    BottomBar mBottomBar;

    public HomeActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(getString(R.string.app_name));
        mBottomBar = new BottomBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class BottomBar {
        private LinearLayout mSearchLayout;
        private ImageView mSearchIcon;
        private TextView mSearchIconText;

        private LinearLayout mBookmarkLayout;
        private ImageView mBookmarkIcon;
        private TextView mBookmarkIconText;

        private LinearLayout mHistoryLayout;
        private ImageView mHistoryIcon;
        private TextView mHistoryIconText;

        private LinearLayout mCompassLayout;
        private ImageView mCompassIcon;
        private TextView mCompassIconText;

        private int mSeleted = 0;

        public final int SEARCH = 0;
        public final int BOOKMARK = 1;
        public final int HISTORY = 2;
        public final int COMPASS = 3;

        private FrameLayout mFragContainer;

        private Fragment mSearchFragment;
        private Fragment mBookmarkFragment;
        private Fragment mHistoryFragment;
        private Fragment mCompassFragment;

        public BottomBar() {
            mFragContainer = (FrameLayout) findViewById(R.id.home_fragment_container);

            mSearchFragment = SearchFragment.newInstance();
            mBookmarkFragment = BookmarkFragment.newInstance("", "");
            mHistoryFragment = HistoryFragment.newInstance("", "");
            mCompassFragment = CompassFragment.newInstance("", "");

            mSearchLayout = (LinearLayout) findViewById(R.id.bottom_action_search);
            mSearchIcon = (ImageView) findViewById(R.id.bottom_action_search_ic);
            mSearchIconText = (TextView) findViewById(R.id.bottom_action_search_text);

            mBookmarkLayout = (LinearLayout) findViewById(R.id.bottom_action_bookmark);
            mBookmarkIcon = (ImageView) findViewById(R.id.bottom_action_bookmark_ic);
            mBookmarkIconText = (TextView) findViewById(R.id.bottom_action_bookmark_text);


            mCompassLayout = (LinearLayout) findViewById(R.id.bottom_action_compass);
            mCompassIcon = (ImageView) findViewById(R.id.bottom_action_compass_ic);
            mCompassIconText = (TextView) findViewById(R.id.bottom_action_compass_text);

            mHistoryLayout = (LinearLayout) findViewById(R.id.bottom_action_history);
            mHistoryIcon = (ImageView) findViewById(R.id.bottom_action_history_ic);
            mHistoryIconText = (TextView) findViewById(R.id.bottom_action_history_text);

            mSearchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelected(SEARCH);
                }
            });

            mBookmarkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelected(BOOKMARK);
                }
            });

            mHistoryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelected(HISTORY);
                }
            });
            mCompassLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelected(COMPASS);
                }
            });

            setSelected(mSeleted);
        }


        public void setSelected(int selected) {

            switch(mSeleted) {
                case SEARCH:
                    deselectSearch();
                    break;
                case BOOKMARK:
                    deselectBookmark();
                    break;
                case HISTORY:
                    deselectHistory();
                    break;
                case COMPASS:
                    deselectCompass();
                    break;
                default:
                    break;
            }
            mSeleted = selected;

            switch (selected) {
                case SEARCH:
                    selectSearch();
                    break;
                case BOOKMARK:
                    selectBookmark();
                    break;
                case HISTORY:
                    selectHistory();
                    break;
                case COMPASS:
                    selectCompass();
                    break;
                default:
                    break;
            }
        }

        private void deselectSearch() {
            mSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
            mSearchIconText.setTextColor(getResources().getColor(R.color.bottom_text));
        }
        private void selectSearch() {
            mSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_hightlight));
            mSearchIconText.setTextColor(getResources().getColor(R.color.bottom_text_hightlight));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_fragment_container, mSearchFragment);
            ft.commit();
            getFragmentManager().executePendingTransactions();
        }

        private void deselectBookmark() {
            mBookmarkIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
            mBookmarkIconText.setTextColor(getResources().getColor(R.color.bottom_text));
        }
        private void selectBookmark() {
            mBookmarkIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_hightlight));
            mBookmarkIconText.setTextColor(getResources().getColor(R.color.bottom_text_hightlight));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_fragment_container, mBookmarkFragment);
            ft.commit();
            getFragmentManager().executePendingTransactions();
        }

        private void deselectHistory() {
            mHistoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_history));
            mHistoryIconText.setTextColor(getResources().getColor(R.color.bottom_text));
        }
        private void selectHistory() {
            mHistoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_history_hightlight));
            mHistoryIconText.setTextColor(getResources().getColor(R.color.bottom_text_hightlight));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_fragment_container, mHistoryFragment);
            ft.commit();
            getFragmentManager().executePendingTransactions();
        }

        private void deselectCompass() {
            mCompassIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_compass));
            mCompassIconText.setTextColor(getResources().getColor(R.color.bottom_text));
        }
        private void selectCompass() {
            mCompassIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_compass_hightlight));
            mCompassIconText.setTextColor(getResources().getColor(R.color.bottom_text_hightlight));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.home_fragment_container, mCompassFragment);
            ft.commit();
            getFragmentManager().executePendingTransactions();
        }


    }

}
