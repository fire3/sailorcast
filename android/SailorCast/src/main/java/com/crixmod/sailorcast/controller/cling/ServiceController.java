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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crixmod.sailorcast.model.cling.UpnpService;
import com.crixmod.sailorcast.model.cling.UpnpServiceController;
import org.fourthline.cling.model.meta.LocalDevice;

public class ServiceController extends UpnpServiceController
{
	private static final String TAG = "Cling.ServiceController";

	private final ServiceListener upnpServiceListener;
	private Activity activity = null;

	public ServiceController(Context ctx)
	{
		super();
		upnpServiceListener = new ServiceListener(ctx);
	}

	@Override
	protected void finalize()
	{
		pause();
	}

	@Override
	public ServiceListener getServiceListener()
	{
		return upnpServiceListener;
	}


    @Override
	public void pause()
	{
		super.pause();
		activity.unbindService(upnpServiceListener.getServiceConnexion());
		activity = null;
	}

	@Override
	public void resume(Activity activity)
	{
		super.resume(activity);
		this.activity = activity;

		// This will start the UPnP service if it wasn't already started
		Log.d(TAG, "Start upnp service");
		activity.bindService(new Intent(activity, UpnpService.class), upnpServiceListener.getServiceConnexion(),
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public void addDevice(LocalDevice localDevice) {
		upnpServiceListener.getUpnpService().getRegistry().addDevice(localDevice);
	}

	@Override
	public void removeDevice(LocalDevice localDevice) {
		upnpServiceListener.getUpnpService().getRegistry().removeDevice(localDevice);
	}

}
