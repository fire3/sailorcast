
package com.crixmod.sailorcast.uiutils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.utils.SCActivityUtils;
import com.sailorcast.android.util.DisplayUtils;
import com.sailorcast.android.util.ListScrollPositionManager;
import com.sailorcast.android.util.ToastUtils;

import com.crixmod.sailorcast.uiutils.DrawerItems.DrawerItem;

/**
 * Base class for Activities that include a standard action bar and menu drawer.
 */
public abstract class SCDrawerActivity extends ActionBarActivity {
    public static final int NEW_BLOG_CANCELED = 10;
    private static final String SCROLL_POSITION_ID = "WPDrawerActivity";
    /**
     * AuthenticatorRequest code used when no accounts exist, and user is prompted to add an
     * account.
     */
    private static final int ADD_ACCOUNT_REQUEST = 100;
    /**
     * AuthenticatorRequest code for reloading menu after returning from  the PreferencesActivity.
     */
    private static final int SETTINGS_REQUEST = 200;
    /**
     * AuthenticatorRequest code for re-authentication
     */
    private static final int AUTHENTICATE_REQUEST = 300;

    private static int[] mBlogIDs;
    private boolean isAnimatingRefreshButton;
    private boolean mShouldFinish;
    private boolean mBlogSpinnerInitialized;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerAdapter mDrawerAdapter;
    private ListView mDrawerListView;
    private Spinner mBlogSpinner;
    private ListScrollPositionManager mScrollPositionManager;

    private static final int OPENED_FROM_DRAWER_DELAY = 250;

    @Override
    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isStaticMenuDrawer()) {
            setContentView(R.layout.activity_drawer_static);
        } else {
            setContentView(R.layout.activity_drawer);
        }

        setSupportActionBar(getToolbar());
    }

    /*
     * fade out the view containing the current drawer activity
     */
    private void hideActivityView() {
        // activity_container is the parent view which contains the toolbar (first child) and
        // the activity itself (second child)
        ViewGroup container = (ViewGroup) findViewById(R.id.activity_container);
        if (container == null || container.getChildCount() < 2) {
            return;
        }
        final View activityView = container.getChildAt(1);
        if (activityView == null || activityView.getVisibility() != View.VISIBLE) {
            return;
        }
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(activityView, View.ALPHA, 1.0f, 0.0f);
        fadeOut.setDuration(OPENED_FROM_DRAWER_DELAY);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();

        if (isAnimatingRefreshButton) {
            isAnimatingRefreshButton = false;
        }
        if (mShouldFinish) {
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        refreshMenuDrawer();
        if (mDrawerToggle != null) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            mDrawerToggle.syncState();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected boolean isActivityDestroyed() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed());
    }

    void refreshMenuDrawer() {
        if (mDrawerAdapter == null) return;
        // the current blog may have changed while we were away
        setupCurrentBlog();
        updateMenuDrawer();
    }

    /**
     * Create a menu drawer and attach it to the activity.
     *
     * @param contentViewId {@link View} of the main content for the activity.
     */
    protected void createMenuDrawer(int contentViewId) {
        ViewGroup container = (ViewGroup) findViewById(R.id.activity_container);
        //container.addView(getLayoutInflater().inflate(contentViewId, null));
        getLayoutInflater().inflate(contentViewId, container,true);
        initMenuDrawer();
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }

        return mToolbar;
    }

    public boolean isStaticMenuDrawer() {
        return DisplayUtils.isLandscape(this)
                && DisplayUtils.isXLarge(this);
    }

    /**
     * Create menu drawer ListView and listeners
     */
    private void initMenuDrawer() {
        // locate the drawer layout - note that it will not exist on landscape tablets
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            int drawerWidth =
                    isStaticMenuDrawer() ?
                            getResources().getDimensionPixelSize(R.dimen.drawer_width_static) :
                            SCActivityUtils.getOptimalDrawerWidth(this);
            ViewGroup leftDrawer = (ViewGroup) mDrawerLayout.findViewById(R.id.capture_insets_frame_layout);
            leftDrawer.getLayoutParams().width = drawerWidth;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.color_primary_dark));
            }
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,
                    mDrawerLayout,
                    mToolbar,
                    R.string.open_drawer,
                    R.string.close_drawer
            ) {
                public void onDrawerClosed(View view) {
                    invalidateOptionsMenu();
                }
                public void onDrawerOpened(View drawerView) {
                    invalidateOptionsMenu();
                }
            };
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }

        // add listVew header containing spinner if it hasn't already been added - note that
        mDrawerListView = (ListView) findViewById(R.id.drawer_list);
        if (mDrawerListView.getHeaderViewsCount() == 0) {
            View view = getLayoutInflater().inflate(R.layout.drawer_header, mDrawerListView, false);
            mDrawerListView.addHeaderView(view, null, false);
        }
        mScrollPositionManager = new ListScrollPositionManager(mDrawerListView, false);
        View settingsRow = findViewById(R.id.settings_row);
        settingsRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });

        mDrawerAdapter = new DrawerAdapter(this);
        mDrawerListView.setAdapter(mDrawerAdapter);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int menuPosition = position - mDrawerListView.getHeaderViewsCount();
                DrawerItem item = (DrawerItem) mDrawerAdapter.getItem(menuPosition);
                drawerItemSelected(item);
            }
        });

        updateMenuDrawer();
    }

    private void closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void openDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void toggleDrawer() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                closeDrawer();
            } else {
                openDrawer();
            }
        }
    }

    /**
     * called when user selects an item from the drawer
     */
    private void drawerItemSelected(DrawerItem item) {
        // do nothing if item is already selected
        if (item == null || item.isSelected(this)) {
            closeDrawer();
            return;
        }

        // if the item has an activity id, remember it for launch
        ActivityId activityId = item.getDrawerItemId().toActivityId();
        if (activityId != ActivityId.UNKNOWN) {
            ActivityId.trackLastActivity(SCDrawerActivity.this, activityId);
        }

        final Intent intent;
        switch (item.getDrawerItemId()) {
            case READER:
                mShouldFinish = true;
                intent = SCActivityUtils.getIntentForActivityId(this, activityId);
                break;
            case NOTIFICATIONS:
                mShouldFinish = true;
                intent = SCActivityUtils.getIntentForActivityId(this, activityId);
                break;
            default :
                mShouldFinish = false;
                intent = null;
                break;
        }

        if (intent == null) {
            ToastUtils.showToast(this, R.string.reader_toast_err_generic);
            return;
        }

        if (mShouldFinish) {
            // set the ActionBar title to that of the incoming activity - left blank for the
            // reader since it shows a spinner in the toolbar
            if (getSupportActionBar() != null) {
                int titleResId = item.getTitleResId();
                if (titleResId != 0 && activityId != ActivityId.READER) {
                    getSupportActionBar().setTitle(getString(titleResId));
                } else {
                    getSupportActionBar().setTitle(null);
                }
            }

            // close the drawer and fade out the activity container so current activity appears to be going away
            closeDrawer();
            hideActivityView();

            // start the new activity after a brief delay to give drawer time to close
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }, OPENED_FROM_DRAWER_DELAY);
        } else {
            // current activity isn't being finished, so just start the new activity
            closeDrawer();
            startActivity(intent);
        }
    }

    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    /**
     * Update all of the items in the menu drawer based on the current active blog.
     */
    public void updateMenuDrawer() {
        mDrawerAdapter.refresh();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * If the activity has a menu drawer attached that is opened or in the
     * process of opening, the back button press closes it. Otherwise, the
     * normal back action is taken.
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Get the names of all the blogs configured within the application. If a
     * blog does not have a specific name, the blog URL is returned.
     *
     * @return array of blog names
     */
    private static String[] getBlogNames() {
        String[] blogNames = new String[10];
        return blogNames;
    }

    private boolean askToSignInIfNot() {
        return true;
    }

    /**
     * Setup the global state tracking which blog is currently active if the user is signed in.
     */
    public void setupCurrentBlog() {
    }

    private void showReader() {
        /*
        Intent intent = new Intent(SCDrawerActivity.this, ReaderPostListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        */
    }

    private void showSettings() {
//        startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST);
    }

    /*
     * redirect to the Reader if there aren't any visible blogs
     * returns true if redirected, false otherwise
     */
    protected boolean showReaderIfNoBlog() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_ACCOUNT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // new blog has been added, so rebuild cache of blogs and setup current blog
                    getBlogNames();
                    setupCurrentBlog();
                    // If logged in without blog, redirect to the Reader view
                    showReaderIfNoBlog();
                } else {
                    finish();
                }
                break;
            case SETTINGS_REQUEST:
                // user returned from settings - skip if user signed out
                break;
            case AUTHENTICATE_REQUEST:
                break;
        }
    }

    private final OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // http://stackoverflow.com/questions/5624825/spinner-onitemselected-executes-when-it-is-not-suppose-to/5918177#5918177
            if (!mBlogSpinnerInitialized) {
                mBlogSpinnerInitialized = true;
            } else {
                updateMenuDrawer();
                blogChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && mDrawerLayout != null) {
            toggleDrawer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshCurrentBlogContent() {
    }

    /**
     * This method is called when the user changes the active blog or hides all blogs
     */
    private void blogChanged() {
        // the list of items in the drawer may have changed, so check if there's no longer any
        // selected item and if so select the first one
        if (mDrawerAdapter != null && !mDrawerAdapter.hasSelectedItem(this) && mDrawerAdapter.getCount() > 0) {
            DrawerItem drawerItem = (DrawerItem) mDrawerAdapter.getItem(0);
            drawerItemSelected(drawerItem);
        }
        refreshCurrentBlogContent();


    }

    /**
     * This method is called in when the user changes the active blog - descendants should override
     * this to perform activity-specific updates upon blog change
     */
    protected void onBlogChanged() {
    }

    /**
     * this method is called when the user signs out of the app - descendants should override
     * this to perform activity-specific cleanup upon signout
     */
    public void onSignout() {
    }

    /**
     * broadcast receiver which detects when user signs out of the app and calls onSignout()
     * so descendants of this activity can do cleanup upon signout
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        try {
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
            lbm.unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            // exception occurs if receiver already unregistered (safe to ignore)
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null)
                return;

        }
    };
}
