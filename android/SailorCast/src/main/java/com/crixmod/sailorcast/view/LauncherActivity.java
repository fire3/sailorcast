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
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengUpdateAgent;

public class LauncherActivity extends BaseToolbarActivity implements LauncherFragment.OnLauncherFragmentInteraction {

    private LauncherFragment mFragment;

    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    // 设置分享内容
    final String mWeixinAppID = "wx5ed52c376b413795";
    final String mWeixinAppSecret = "86025fe7d16c8bbddcf60c19a1af8542";


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
        MobclickAgent.updateOnlineConfig(this);

        //社会化分享
        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,SHARE_MEDIA.TENCENT);
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        setupWeiXinShare();
        setupQQShare();
        setupSinaShare();

        /*

        final Activity activity = this;
        ImageView mHomeLogo = (ImageView) findViewById(R.id.home_logo);
        mHomeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.openShare(activity, false);
            }
        });
        */

    }


    private void setupSinaShare() {
        SinaShareContent sinaShareContent = new SinaShareContent();
        sinaShareContent.setShareContent(getResources().getString(R.string.share_content));
//设置分享title
        sinaShareContent.setTitle(getResources().getString(R.string.share_title));
//设置分享图片
        sinaShareContent.setShareImage(new UMImage(this, R.drawable.icon_launcher));
//设置点击分享内容的跳转链接
        sinaShareContent.setTargetUrl(getResources().getString(R.string.app_website));
        mController.setShareMedia(sinaShareContent);
    }


    private void setupQQShare() {
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104217487",
                "vrwWTAciwok5Dyjo");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1104217487",
                "vrwWTAciwok5Dyjo");
        qZoneSsoHandler.addToSocialSDK();


        QQShareContent qqShareContent = new QQShareContent();
//设置分享文字
        qqShareContent.setShareContent(getResources().getString(R.string.share_content));
//设置分享title
        qqShareContent.setTitle(getResources().getString(R.string.share_title));
//设置分享图片
        qqShareContent.setShareImage(new UMImage(this, R.drawable.icon_launcher));
//设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(getResources().getString(R.string.app_website));
        mController.setShareMedia(qqShareContent);


        QZoneShareContent qzone = new QZoneShareContent();
//设置分享文字
        qzone.setShareContent(getString(R.string.share_content));
//设置点击消息的跳转URL
        qzone.setTargetUrl(getString(R.string.app_website));
//设置分享内容的标题
        qzone.setTitle(getString(R.string.share_title));
//设置分享图片
        qzone.setShareImage(new UMImage(this,R.drawable.icon_launcher));
        mController.setShareMedia(qzone);

    }

    private void setupWeiXinShare() {
        final Activity activity = this;

// 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(activity, mWeixinAppID, mWeixinAppSecret);
        wxHandler.addToSocialSDK();
// 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(activity, mWeixinAppID, mWeixinAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();


        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
//设置分享文字
        weixinContent.setShareContent(getResources().getString(R.string.share_content));
//设置title
        weixinContent.setTitle(getResources().getString(R.string.share_title));
//设置分享内容跳转URL
        weixinContent.setAppWebSite(getResources().getString(R.string.app_website));
        weixinContent.setTargetUrl(getResources().getString(R.string.app_website));
//设置分享图片
        weixinContent.setShareImage(new UMImage(this,R.drawable.icon_launcher));
        mController.setShareMedia(weixinContent);


        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(getResources().getString(R.string.share_content));
//设置朋友圈title
        circleMedia.setTitle(getResources().getString(R.string.share_content));
        circleMedia.setShareImage(new UMImage(this, R.drawable.icon_launcher));
        circleMedia.setTargetUrl(getResources().getString(R.string.app_website));
        mController.setShareMedia(circleMedia);


       SnsPostListener mSnsPostListener  = new SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int stCode,
                                   SocializeEntity entity) {
                if (stCode == 200) {
                    Toast.makeText(LauncherActivity.this, getString(R.string.share_thanks), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };
        mController.registerListener(mSnsPostListener);
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
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
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onSocialShareClicked() {
        mController.openShare(this,false);
    }
}
