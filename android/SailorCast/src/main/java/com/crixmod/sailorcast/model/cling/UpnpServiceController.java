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

package com.crixmod.sailorcast.model.cling;

import android.app.Activity;
import android.util.Log;


import com.crixmod.sailorcast.controller.upnp.IUpnpServiceController;
import com.crixmod.sailorcast.model.CObservable;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;
import com.crixmod.sailorcast.model.upnp.RendererDiscovery;

import java.util.Observer;

public abstract class UpnpServiceController implements IUpnpServiceController {

	private static final String TAG = "UpnpServiceController";

	protected IUpnpDevice renderer;
	protected IUpnpDevice contentDirectory;

	protected CObservable rendererObservable;
	protected CObservable contentDirectoryObservable;

	private final RendererDiscovery rendererDiscovery;


	@Override
	public RendererDiscovery getRendererDiscovery()
	{
		return rendererDiscovery;
	}

	protected UpnpServiceController()
	{
		rendererObservable = new CObservable();
		contentDirectoryObservable = new CObservable();

		rendererDiscovery = new RendererDiscovery(getServiceListener());
	}

	@Override
	public void setSelectedRenderer(IUpnpDevice renderer)
	{
		setSelectedRenderer(renderer, false);
	}

	@Override
	public void setSelectedRenderer(IUpnpDevice renderer, boolean force)
	{
		// Skip if no change and no force
		if (!force && renderer != null && this.renderer != null && this.renderer.equals(renderer))
			return;

		this.renderer = renderer;
		rendererObservable.notifyAllObservers();
	}


	@Override
	public IUpnpDevice getSelectedRenderer()
	{
		return renderer;
	}

	@Override
	public void addSelectedRendererObserver(Observer o)
	{
		Log.i(TAG, "New SelectedRendererObserver");
		rendererObservable.addObserver(o);
	}

	@Override
	public void delSelectedRendererObserver(Observer o)
	{
		rendererObservable.deleteObserver(o);
	}


	// Pause the service
	@Override
	public void pause()
	{
		rendererDiscovery.pause(getServiceListener());
	}

	// Resume the service
	@Override
	public void resume(Activity activity)
	{
		rendererDiscovery.resume(getServiceListener());
	}

}