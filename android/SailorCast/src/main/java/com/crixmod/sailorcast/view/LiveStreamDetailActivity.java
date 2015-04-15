package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.upnp.CallableRendererFilter;
import com.crixmod.sailorcast.model.upnp.IRendererCommand;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;
import com.crixmod.sailorcast.siteapi.LetvApi;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamWeekDaysListener;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;
import com.crixmod.sailorcast.view.fragments.LiveStreamProgramFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LiveStreamDetailActivity extends BaseToolbarActivity implements OnGetLiveStreamPlayUrlListener, OnGetLiveStreamWeekDaysListener {

    private SCLiveStream mStream;
    ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private PagerAdapter mPagerAdapter;
    private Handler mHandler;

    private Button mPlaySuperButton;
    private Button mPlayNorButton;
    private Button mPlayHighButton;
    private Button mDlnaSuperButton;
    private Button mDlnaHighButton;
    private Button mDlnaNorButton;


    private void findViews() {
        mPlayHighButton = (Button) findViewById(R.id.btn_phone_high);
        mPlayNorButton = (Button) findViewById(R.id.btn_phone_normal);
        mPlaySuperButton = (Button) findViewById(R.id.btn_phone_super);
        mDlnaHighButton = (Button) findViewById(R.id.btn_dlna_high);
        mDlnaNorButton = (Button) findViewById(R.id.btn_dlna_normal);
        mDlnaSuperButton = (Button) findViewById(R.id.btn_dlna_super);

    }

    private void hideAllPlayButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaHighButton.setVisibility(View.GONE);
                mDlnaNorButton.setVisibility(View.GONE);
                mDlnaSuperButton.setVisibility(View.GONE);
                mPlayHighButton.setVisibility(View.GONE);
                mPlayNorButton.setVisibility(View.GONE);
                mPlaySuperButton.setVisibility(View.GONE);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStream = SCLiveStream.fromJson(getIntent().getStringExtra("stream"));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        setTitle(mStream.getChannelName());
        findViews();
        new LetvApi().doGetLiveStreamPlayUrl(mStream, this);
        new LetvApi().doGetLiveStreamWeekDays(mStream, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SailorCast.upnpServiceController.resume(this);
        MobclickAgent.onResume(this);
    }

	@Override
	public void onPause()
	{
		super.onPause();
        SailorCast.upnpServiceController.pause();
        SailorCast.upnpServiceController.getServiceListener().getServiceConnexion().onServiceDisconnected(null);
        MobclickAgent.onPause(this);
	}


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_live_stream_detail;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_stream_detail, menu);
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


    public static void launch(Activity activity, SCLiveStream stream) {
        Intent mpdIntent = new Intent(activity, LiveStreamDetailActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("stream",stream.toJson());

        activity.startActivity(mpdIntent);
    }

    @Override
    public void onGetLiveStreamPlayUrlNormal(final SCLiveStream v, final String urlNormal) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaNorButton.setVisibility(View.VISIBLE);
                mPlayNorButton.setVisibility(View.VISIBLE);


                mPlayNorButton.setTag(R.id.key_video_url, urlNormal);
                mPlayNorButton.setTag(R.id.key_video, v);
                mDlnaNorButton.setTag(R.id.key_video_url, urlNormal);
                mDlnaNorButton.setTag(R.id.key_video, v);

            }
        });

    }

    @Override
    public void onGetLiveStreamPlayUrlHigh(final SCLiveStream v, final String urlHigh) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaHighButton.setVisibility(View.VISIBLE);
                mPlayHighButton.setVisibility(View.VISIBLE);

                mPlayHighButton.setTag(R.id.key_video_url,urlHigh);
                mPlayHighButton.setTag(R.id.key_video,v);
                mDlnaHighButton.setTag(R.id.key_video_url,urlHigh);
                mDlnaHighButton.setTag(R.id.key_video,v);
            }
        });

    }

    @Override
    public void onGetLiveStreamPlayUrlSuper(final SCLiveStream v, final String urlSuper) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaSuperButton.setVisibility(View.VISIBLE);
                mPlaySuperButton.setVisibility(View.VISIBLE);

                mPlaySuperButton.setTag(R.id.key_video_url, urlSuper);
                mPlaySuperButton.setTag(R.id.key_video, v);
                mDlnaSuperButton.setTag(R.id.key_video_url, urlSuper);
                mDlnaSuperButton.setTag(R.id.key_video, v);
            }
        });


    }

    @Override
    public void onGetLiveStreamPlayUrlFailed(SCFailLog reason) {
        hideAllPlayButton();
        MobclickAgent.reportError(this, reason.toJson());
    }



    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onPlayButtonClick(View button) {
        final String url = (String) button.getTag(R.id.key_video_url);
        final SCLiveStream v = (SCLiveStream) button.getTag(R.id.key_video);
        boolean isWifi = SailorCast.isNetworkWifi();
        final Activity from = this;
        if(url != null) {
            if(isWifi) {
                BaiduPlayerActivity.launch(from, v, url);
            }
            else {
                final SweetAlertDialog pDialog =  new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("是否播放");
                pDialog.setContentText("继续播放可能会耗费您的流量");
                pDialog.setConfirmText("继续播放");
                pDialog.setCancelable(true);
                pDialog.setCancelText("取消");
                pDialog.show();
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.hide();
                        pDialog.hide();
                        BaiduPlayerActivity.launch(from, v, url);
                    }
                });
            }
        }
    }

    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onDlnaButtonClick(View button) {
        final String url = (String) button.getTag(R.id.key_video_url);
        final SCLiveStream v = (SCLiveStream) button.getTag(R.id.key_video);

		final Collection<IUpnpDevice> upnpDevices = SailorCast.upnpServiceController.getServiceListener()
				.getFilteredDeviceList(new CallableRendererFilter());
        if(upnpDevices.size() > 0) {

            FragmentManager fm = getSupportFragmentManager();
            RendererDialog dialog = new RendererDialog();
            dialog.setCallback(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    launchRenderer(v, url);
                    return null;
                }
            });
            dialog.show(fm, "Render");
        } else {
            Toast.makeText(this, getResources().getString(R.string.noRenderer), Toast.LENGTH_SHORT).show();
        }
    }


	private void launchRenderer(SCLiveStream stream, String url)
	{
		IRendererCommand rendererCommand = SailorCast.factory.createRendererCommand(SailorCast.factory.createRendererState());
		rendererCommand.launchSCLiveStream(stream, url);
	}

    @Override
    public void onGetLiveStreamWeekDaysSuccess(SCLiveStream stream) {

        final Context context = this;
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), context,mStream);
                mViewPager.setAdapter(mPagerAdapter);
                mSlidingTabLayout.setViewPager(mViewPager);
            }
        });
    }

    @Override
    public void onGetLiveStreamWeekDaysFailed(SCFailLog failReason) {

    }


    private class PagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private HashMap<Integer,LiveStreamProgramFragment> mPageReferenceMap;
        private SCLiveStream mStream;

        public PagerAdapter(FragmentManager fm, Context context, SCLiveStream stream) {
            super(fm);
            this.mContext = context;
            mPageReferenceMap = new HashMap<>();
            this.mStream = stream;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if(obj instanceof LiveStreamProgramFragment) {
                mPageReferenceMap.put(position, (LiveStreamProgramFragment) obj);
            }
            return obj;
        }

        @Override
        public LiveStreamProgramFragment getItem(int position) {
            LiveStreamProgramFragment fragment  = LiveStreamProgramFragment.newInstance(mStream,mStream.getWeekDays().get(position));
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public LiveStreamProgramFragment getFragment(int position) {
            return mPageReferenceMap.get(position);
        }

        @Override
        public int getCount() {
            return mStream.getWeekDays().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return SiteApi.getSiteName(positionToSiteID(position), mContext);
            return mStream.getWeekDays().get(position).weekDayName;
        }
    }


}
