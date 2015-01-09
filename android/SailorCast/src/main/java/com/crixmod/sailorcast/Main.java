/**
 * Copyright (C) 2013 Aurélien Chabot <aurelien@chabot.fr>
 *
 * This file is part of DroidUPNP.
 *
 * DroidUPNP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DroidUPNP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DroidUPNP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.crixmod.sailorcast;

import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crixmod.sailorcast.controller.upnp.IUpnpServiceController;
import com.crixmod.sailorcast.model.upnp.IFactory;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.view.RendererFragment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Observer;

public class Main extends BaseToolbarActivity
{
	private static final String TAG = "Main";


	private DrawerFragment mDrawerFragment;
	private CharSequence mTitle;

	public RendererFragment getRenderer()
	{
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.RendererFragment);
		if(f != null)
			return (RendererFragment) f;
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);


		Fragment rendererFragment = getSupportFragmentManager().findFragmentById(R.id.RendererFragment);
		if (rendererFragment != null && rendererFragment instanceof Observer)
			SailorCast.upnpServiceController.addSelectedRendererObserver((Observer) rendererFragment);
		else
			Log.w(TAG, "No rendererFragment yet !");

		if(getSupportFragmentManager().findFragmentById(R.id.navigation_drawer) instanceof DrawerFragment)
		{
			mDrawerFragment = (DrawerFragment)
					getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
			mTitle = getTitle();

			// Set up the drawer.
			mDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		}
	}

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onResume()
	{
		Log.v(TAG, "Resume activity");
		SailorCast.upnpServiceController.resume(this);
		super.onResume();
	}

	@Override
	public void onPause()
	{
		Log.v(TAG, "Pause activity");
		SailorCast.upnpServiceController.pause();
		SailorCast.upnpServiceController.getServiceListener().getServiceConnexion().onServiceDisconnected(null);
		super.onPause();
	}

	public void refresh()
	{
		SailorCast.upnpServiceController.getServiceListener().refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.menu_refresh:
				refresh();
				break;
			case R.id.menu_settings:
				break;
			case R.id.menu_quit:
				finish();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
        super.onBackPressed();
	}

}
