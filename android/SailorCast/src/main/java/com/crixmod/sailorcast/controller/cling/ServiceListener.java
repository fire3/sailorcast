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

package com.crixmod.sailorcast.controller.cling;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crixmod.sailorcast.model.cling.CDevice;
import com.crixmod.sailorcast.model.cling.CRegistryListener;
import com.crixmod.sailorcast.model.upnp.ICallableFilter;
import com.crixmod.sailorcast.model.upnp.IRegistryListener;
import com.crixmod.sailorcast.model.upnp.IServiceListener;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;
import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Device;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("rawtypes")
public class ServiceListener implements IServiceListener
{
	private static final String TAG = "Cling.ServiceListener";

	protected AndroidUpnpService upnpService;
	protected ArrayList<IRegistryListener> waitingListener;

	private Context ctx = null;

	public ServiceListener(Context ctx)
	{
		waitingListener = new ArrayList<IRegistryListener>();
		this.ctx = ctx;
	}

	@Override
	public void refresh()
	{
		upnpService.getControlPoint().search();
	}

	@Override
	public Collection<IUpnpDevice> getDeviceList()
	{
		ArrayList<IUpnpDevice> deviceList = new ArrayList<IUpnpDevice>();
		if(upnpService != null && upnpService.getRegistry() != null) {
			for (Device device : upnpService.getRegistry().getDevices()) {
				deviceList.add(new CDevice(device));
			}
		}
		return deviceList;
	}

	@Override
	public Collection<IUpnpDevice> getFilteredDeviceList(ICallableFilter filter)
	{
		ArrayList<IUpnpDevice> deviceList = new ArrayList<IUpnpDevice>();
		try
		{
			if(upnpService != null && upnpService.getRegistry() != null) {
				for (Device device : upnpService.getRegistry().getDevices()) {
					IUpnpDevice upnpDevice = new CDevice(device);
					filter.setDevice(upnpDevice);

					if (filter.call())
						deviceList.add(upnpDevice);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return deviceList;
	}

	protected ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			Log.d(TAG, "Service connexion");
			upnpService = (AndroidUpnpService) service;

			for (IRegistryListener registryListener : waitingListener)
			{
				addListenerSafe(registryListener);
			}

			// Search asynchronously for all devices, they will respond soon
			upnpService.getControlPoint().search();
		}

		@Override
		public void onServiceDisconnected(ComponentName className)
		{
			Log.d(TAG, "Service disconnected");
			upnpService = null;
		}
	};

	@Override
	public ServiceConnection getServiceConnexion()
	{
        Log.d(TAG,"getServiceConnexion");

        return serviceConnection;
	}

	public AndroidUpnpService getUpnpService()
	{
		return upnpService;
	}

	@Override
	public void addListener(IRegistryListener registryListener)
	{
		Log.d(TAG, "Add Listener !");
		if (upnpService != null)
			addListenerSafe(registryListener);
		else
			waitingListener.add(registryListener);
	}

	private void addListenerSafe(IRegistryListener registryListener)
	{
		assert upnpService != null;
		Log.d(TAG, "Add Listener Safe !");

		// Get ready for future device advertisements
		upnpService.getRegistry().addListener(new CRegistryListener(registryListener));

		// Now add all devices to the list we already know about
		for (Device device : upnpService.getRegistry().getDevices())
		{
			registryListener.deviceAdded(new CDevice(device));
		}
	}

	@Override
	public void removeListener(IRegistryListener registryListener)
	{
		Log.d(TAG, "remove listener");
		if (upnpService != null)
			removeListenerSafe(registryListener);
		else
			waitingListener.remove(registryListener);
	}

	private void removeListenerSafe(IRegistryListener registryListener)
	{
		assert upnpService != null;
		Log.d(TAG, "remove listener Safe");
		upnpService.getRegistry().removeListener(new CRegistryListener(registryListener));
	}

	@Override
	public void clearListener()
	{
		waitingListener.clear();
	}
}
